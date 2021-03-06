package com.kroy.game.minigameHelpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kroy.game.Character;
import com.kroy.game.Constants;

/***
 * Added by Septagon
 * Class for the bullets that are used in the minigame
 */
public class MinigameBullet {

    private ShapeRenderer bulletRenderer;
    private float x, y;
    private float velY;
    private Sprite sprite;
    private boolean up;
    private boolean hasFired = false;

    /**
     * Constructor to pass values to the bullet
     * @param sprite The sprite that the bullet is attached to (engine or alien)
     * @param up Whether the bullet should be firing up or down
     */
    public MinigameBullet(Sprite sprite, boolean up){
        this.sprite = sprite;
        this.up = up;
        bulletRenderer = new ShapeRenderer();
    }

    /***
     * Called to fire the bullet and make it move depending on the sprite that owns it
     */
    public void fire(){
        x = sprite.getX() + sprite.getWidth() / 2;
        y = sprite.getY() + sprite.getHeight() / 2;

        if(up){
            velY = Constants.getMinigameBulletSpeed();
        }else{
            velY = -Constants.getMinigameBulletSpeed();
        }

        hasFired = true;
    }

    /***
     * Updates the bullet and also checks its bounds once it has been fired
     */
    public void update(){
        if(hasFired){
            y += velY;
        }else{
            x = sprite.getX() + sprite.getWidth() / 2;
            y = sprite.getY() + sprite.getHeight() / 2;
        }

        if(hasFired) {
            if (y + Constants.getMinigameBulletHeight() >= Constants.getResolutionHeight() / 2) {
                hasFired = false;
                this.x = sprite.getX() + sprite.getWidth() / 2;
                this.y = sprite.getY() + sprite.getHeight() / 2;
            } else if (y <= -Constants.getResolutionHeight() / 2) {
                hasFired = false;
                this.x = sprite.getX() + sprite.getWidth() / 2;
                this.y = sprite.getY() + sprite.getHeight() / 2;
            }
        }
    }

    /***
     * Renders the bullet
     * @param cam Games camera
     */
    public void render(OrthographicCamera cam){
        if(hasFired)
        {
            bulletRenderer.setProjectionMatrix(cam.combined);
            bulletRenderer.begin(ShapeRenderer.ShapeType.Filled);

            if(up)
                bulletRenderer.setColor(Color.BLUE);
            else
                bulletRenderer.setColor(Color.RED);
            bulletRenderer.rect(x, y, Constants.getMinigameBulletWidth(), Constants.getMinigameBulletHeight());

            bulletRenderer.end();
        }
    }

    //Getters and Setters
    public boolean isHasFired() { return hasFired; }
    public boolean isUp() { return up; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVelY() { return velY; }
    public Sprite getSprite() { return sprite; }
    public ShapeRenderer getBulletRenderer() { return bulletRenderer; }

    public void setHasFired(boolean hasFired) { this.hasFired = hasFired; }
    public void setY(float y) { this.y = y; }
}
