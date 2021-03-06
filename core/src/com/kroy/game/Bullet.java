package com.kroy.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Class that is used to create animations when the engines and fortresses are attacking each other
 */
public class Bullet {

    private double ySPEED, xSPEED; // speed of bullet in x and y direction
    public Texture texture;
    private double deltaY, deltaX; // difference in distance of attacker and targer
    private float xPosition, yPosition;
    private float targetX, targetY;
    public boolean remove = false;
    private Character attacker, target;
    private float updateYPosition, updateXPosition;
    private float relativeXPosition, relativeYPosition;
    private int shiftX, shiftY;

    //Setter Method
    public void setShiftX(int shiftX) { this.shiftX = shiftX; }
    public void setShiftY(int shiftY) { this.shiftY = shiftY; }
    public int getShiftX() { return shiftX; }
    public int getShiftY() { return shiftY; }

    //creates a bullet with attacker and target positions
    public Bullet(Character attacker, Character target, boolean water) {

        this.attacker = attacker;
        this.target = target;

        if ((attacker != null) && (target != null)){

            xPosition = attacker.getLocation().getMapX() * Constants.getTileSize();
            yPosition = attacker.getLocation().getMapY() * Constants.getTileSize();

            targetX = target.getLocation().getMapX() * Constants.getTileSize();
            targetY = target.getLocation().getMapY() * Constants.getTileSize();

            //difference in distance between attacker and target
            deltaY = targetY - yPosition;
            deltaX = targetX - xPosition;

            //calculate relative speed in both x and y directions in order to move from attacker to target
            ySPEED = deltaY / (deltaX * deltaX + deltaY * deltaY);
            xSPEED = deltaX / (deltaX * deltaX + deltaY * deltaY);

            if (water) {
                texture = new Texture("water.png");
            } else {
                texture = new Texture("gunge.png");
            }
        }
        relativeXPosition = 0;
        relativeYPosition = 0;
    }



    /**
     * Move bullets in required directions
     */
    public void update(float deltaTime) {

        updateXPosition = attacker.getLocation().getMapX() * Constants.getTileSize() + (float) Constants.getTileSize()/2 + shiftX;
        updateYPosition = attacker.getLocation().getMapY() * Constants.getTileSize() + (float) Constants.getTileSize()/2 + shiftY;

        targetX = target.getLocation().getMapX() * Constants.getTileSize() + (float) Constants.getTileSize()/2 + shiftX;
        targetY = target.getLocation().getMapY() * Constants.getTileSize() + (float) Constants.getTileSize()/2 + shiftY;

        System.out.print("xPosition " + xPosition + "  yPosition " + yPosition + "  targetX " + targetX +"  targetY " + targetY + "\n");

        relativeXPosition += xSPEED * deltaTime * 100000;
        relativeYPosition += ySPEED * deltaTime * 100000;

        xPosition = relativeXPosition + updateXPosition;
        yPosition = relativeYPosition + updateYPosition;

        if ((deltaX * (targetX - xPosition) < 0) || (deltaY * (targetY - yPosition) < 0)){
            remove = true;
        }
    }


    /**
     * Draws the bullet to the screen
     *
     * @param batch The batch which is used for drawing objects to the screen
     */
    public void renderBullets(Batch batch) {
        batch.draw(texture, xPosition, yPosition);
    }

    public Character getAttacker() { return attacker; }
    public Character getTarget() { return target; }

    public float getxPosition() { return xPosition; }
    public float getyPosition() { return yPosition; }
    public float getTargetX() { return targetX; }
    public float getTargetY() { return targetY; }
    public float getUpdateXPosition() { return updateXPosition; }
    public float getUpdateYPosition() { return updateYPosition;}
}