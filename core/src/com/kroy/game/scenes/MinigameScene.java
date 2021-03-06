package com.kroy.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kroy.game.*;
import com.kroy.game.minigameHelpers.Alien;
import com.kroy.game.minigameHelpers.MinigameBullet;
import com.kroy.game.minigameHelpers.MinigameEngine;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.ConsoleHandler;

/**
 * Class created by Team Septagon
 * This is used to handle all of the features and events of the minigame
 */

public class MinigameScene extends Scene {

    //Stores the engine and patrol that triggered the minigame
    private FireEngine passedEngine;
    private Patrol passedPatrol;

    //Holds the engine that is used for the minigame
    private MinigameEngine minigameEngine;

    //Stores the area of the screen which the minigame can be played in
    private Rectangle playableArea;
    private ShapeRenderer boundsRenderer;

    //Stores all the aliens and their textures
    private Texture[] aliensTextures;
    private ArrayList<Alien> aliens;

    //Used to generate random values
    private Random random;
    private Texture backgroundImage;

    //Variables to handle the countdown before the minigame actually starts when it first loads up
    private int countdownTimer = 180;
    private boolean started = false;
    private int secondsValue = 3;
    private GlyphLayout countdownText;

    //Variables for handling when the player has lost
    private boolean lost = false;
    private GlyphLayout lostText;

    //Variables for handling when the player has won
    private boolean won = false;
    private GlyphLayout wonText;

    private int alienFireTimer = 30;

    private SceneManager sceneManager;

    /***
     * Constructors used to pass vaues to the scene
     * @param font The games font
     * @param cam The game camera
     * @param passedEngine The engine that triggered the minigame
     * @param passedPatrol The patrol that triggered the minigame
     * @param sceneManager SceneManager used to change the scenes of the game
     */
    protected MinigameScene(BitmapFont font, OrthographicCamera cam, FireEngine passedEngine, Patrol passedPatrol, SceneManager sceneManager) {
        super(font, cam);
        this.passedEngine = passedEngine;
        this.passedPatrol = passedPatrol;
        this.sceneManager = sceneManager;
    }

    @Override
    /***
     * Initalises and sets up all objects for the minigame
     */
    public void initScene() {
        sceneType = SceneType.SCENE_TYPE_MINIGAME;

        boundsRenderer = new ShapeRenderer();
        random = new Random();

        //Sets up the area of the screen that the minigame can take place in
        playableArea = new Rectangle();
        playableArea.setSize(Constants.getResolutionHeight(), Constants.getResolutionHeight());
        playableArea.setPosition( -playableArea.getWidth() / 2, -playableArea.getHeight() / 2);

        //Loads in all the different alien textures for the game
        aliensTextures = new Texture[Assets.MINIGAME_TEXTURES.size()];
        for(int i = 0; i < Assets.MINIGAME_TEXTURES.size(); i++){
            aliensTextures[i] = Assets.MINIGAME_TEXTURES.get(i);
        }

        minigameEngine = new MinigameEngine(passedEngine);
        aliens = new ArrayList<Alien>();

        minigameEngine.init();

        float startX = -playableArea.width / 2 + Constants.getMinigameEdgeBuffer();
        float startY = playableArea.height / 2 - Constants.getTileSize() - Constants.getMinigameEdgeBuffer();
        float bufferBetweenAliens = 3;
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < passedPatrol.getHealth(); x++) {
                Alien newAlien = new Alien(passedPatrol.getHealth(), playableArea, passedPatrol, aliensTextures[random.nextInt(aliensTextures.length)]);
                newAlien.setPosition(startX + (x * Constants.getTileSize()) + bufferBetweenAliens, startY - (Constants.getTileSize() * y));
                aliens.add(newAlien);
            }
        }

        for(Alien a: aliens){
            a.init();
        }

        //Sets up the countdown text to start
        font.setColor(Color.RED);
        countdownText = new GlyphLayout();
        countdownText.setText(font, "Starting in " + secondsValue);
        backgroundImage = new Texture(Gdx.files.internal("MinigameBackground.png"));

        lostText = new GlyphLayout();
        lostText.setText(font, "You have lost!!!");

        wonText = new GlyphLayout();
        wonText.setText(font, "You have Won!!!");
    }

    @Override
    /***
     * Updates all objects in the minigame
     */
    public void resolveScene() {
        if(started && !lost && !won) {
            alienFireTimer++;
            //Moves all the aliens
            for (Alien alien : aliens) {
                alien.move();
            }
            moveAliensDown();
            if(alienFireTimer >= 30){
                for(Alien alien: aliens){
                    if(!alien.getBullet().isHasFired()){
                        alien.fire();
                        break;
                    }
                }
                alienFireTimer = 0;
            }
            minigameEngine.update();
            checkHittingAlien();
            checkHittingPlayer();
            if(aliens.size() >= 1 && aliens.get(aliens.size() - 1).isHasLost()) lost = true;

        }else if(!started && !lost && !won){
            countdownTimer -= 1;

            //Works out whether the minigame should be started or if the amount of seconds left should decrease
            if(countdownTimer <= 0){
                started = true;
                countdownTimer = 120;
            }else if(countdownTimer <= 120 && secondsValue == 3){
                secondsValue = 2;
                font.setColor(Color.RED);
                countdownText.setText(font, "Starting in " + secondsValue);
            }else if(countdownTimer <= 60 && secondsValue == 2){
                secondsValue = 1;
                font.setColor(Color.RED);
                countdownText.setText(font, "Starting in " + secondsValue);
            }
        }else if(lost || won){
            countdownTimer--;
            if(countdownTimer <= 0){
                sceneManager.returnToPreviousScene(won);
            }
        }
    }

    @Override
    /***
     * Renders all elements of the screen
     */
    public void renderScene(Batch batch) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Only needed when renderering at half size
        //cam.zoom = 0.25f;

        batch.begin();
        batch.draw(backgroundImage, -Constants.getActualScreenWidth() / 2, -Constants.getActualScreenHeight() /2, Constants.getActualScreenWidth(), Constants.getActualScreenHeight());
        batch.end();

        boundsRenderer.setProjectionMatrix(cam.combined);
        boundsRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Draw background for the playableArea
        boundsRenderer.setColor(Color.LIGHT_GRAY);
        boundsRenderer.rect(playableArea.x, playableArea.y, playableArea.width, playableArea.height);

        boundsRenderer.setColor(Color.BLACK);
        //Draw the 2 boundary lines of the playableArea
        boundsRenderer.rectLine(new Vector2(playableArea.x, playableArea.y), new Vector2(playableArea.x, playableArea.y + playableArea.height), 5);
        boundsRenderer.rectLine(new Vector2(playableArea.x + playableArea.width, playableArea.y), new Vector2(playableArea.x + playableArea.width, playableArea.y + playableArea.height), 5);
        boundsRenderer.rectLine(new Vector2(playableArea.x, playableArea.y), new Vector2(playableArea.x + playableArea.width, playableArea.y),5);
        boundsRenderer.rectLine(new Vector2(playableArea.x, playableArea.y + playableArea.height), new Vector2(playableArea.x + playableArea.width, playableArea.y + playableArea.height),5);
        boundsRenderer.end();

        batch.begin();
        //Renders the engine and all aliens
        for(Alien alien: aliens){
            alien.draw(batch);
        }
        minigameEngine.draw(batch);

        //Draws the countdown text if the game has not yet started
        if(!started){
            font.draw(batch, countdownText, -countdownText.width / 2, countdownText.height / 2);
        }
        else if(lost){
            font.draw(batch, lostText, -lostText.width / 2, lostText.height / 2);
        }
        else if(won){
            font.draw(batch, wonText, -wonText.width / 2, wonText.height / 2);
        }

        batch.end();
        minigameEngine.renderBullets(cam);
        for(Alien a: aliens){
            a.getBullet().render(cam);
        }
    }

    /**
     * Moves the engine sprite by changing the x position of the sprite
     * @param increment The amount the position should be changed by
     */
    public void moveEngine(float increment) {
        minigameEngine.setX(minigameEngine.getX() + increment);

        //Checks that the engine stays within the bounds of the playable area
        if (minigameEngine.getX() <= playableArea.getX()) {
            minigameEngine.setX(playableArea.getX());
        }

        if (minigameEngine.getX() >= playableArea.getX() + playableArea.getWidth() - minigameEngine.getWidth()) {
            minigameEngine.setX(playableArea.getX() + playableArea.getWidth() - minigameEngine.getWidth());
        }
    }

    /***
     * Function to check if the aliens have reached the edge of the screen and if so, move them all down as
     * a group
     */
    public void moveAliensDown(){
        for(Alien a: aliens){
            if(a.isShouldMoveDown()){
                if(!a.isLeft()){
                    for(Alien alien: aliens){
                        alien.setLeft(true);
                        alien.setShouldMoveDown(false);
                        alien.setY(alien.getY() - Constants.getTileSize());
                    }
                }else{
                    for(Alien alien: aliens){
                        alien.setLeft(false);
                        alien.setShouldMoveDown(false);
                        alien.setY(alien.getY() - Constants.getTileSize());
                    }
                }
                break;
            }
        }
    }

    /***
     * Function that checks to see if a bullet from the engine has hit an alien - if so will
     * kill the alien
     */
    private void checkHittingAlien(){
        for(int i = 0; i < minigameEngine.getBullets().length; i++){
            if(minigameEngine.getBullets()[i].isHasFired()){
                for(int j = 0; j < aliens.size(); j++){
                    float x = minigameEngine.getBullets()[i].getX();
                    float y = minigameEngine.getBullets()[i].getY();
                    if(aliens.get(j).getBoundingRectangle().contains(new Vector2(x, y))){
                        aliens.remove(aliens.get(j));
                        minigameEngine.getBullets()[i].setHasFired(false);

                        if(aliens.size() == 0){
                            won = true;
                        }
                    }
                }
            }
        }
    }

    /***
     * Function to check if a bullet from the alien has hit a player - if so will kill
     * the player
     */
    private void checkHittingPlayer(){
        for(int i = 0; i < aliens.size(); i++){
            if(aliens.get(i).getBullet().isHasFired()){
                float x = aliens.get(i).getBullet().getX();
                float y = aliens.get(i).getBullet().getY();
                if(minigameEngine.getBoundingRectangle().contains(new Vector2(x, y))){
                    lost = true;
                }
            }
        }
    }

    //  Getters and Setters
    public boolean isStarted() { return started; }
    public boolean isFinished() { return (won || lost); }
    public boolean isWon() { return won; }
    public Patrol getPassedPatrol() { return passedPatrol; }
    public FireEngine getPassedEngine() { return passedEngine; }

    public MinigameEngine getMinigameEngine() { return minigameEngine; }
}
