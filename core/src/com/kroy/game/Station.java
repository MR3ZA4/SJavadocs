package com.kroy.game;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/***
 * Class used to handle info about the fireStation
 */

public class Station extends Tile {
    private int repairTime;
    private int refillTime;
    private Instant destructionTime;
    private boolean destroyed = false;


    /**
     * Constructs A Station Tile that repairs and refills surrounding tiles
     *
     * @param X The x position of the tile on the map
     * @param Y The y position of the tile on the map
     */
    public Station(int X, int Y) {
        super(X, Y, "stationTile.png", TileType.TILE_TYPES_STATION);

        repairTime = Constants.getStationRepairAmount();
        refillTime = Constants.getStationRefillAmount();
        destructionTime=Instant.now();
    }


    //Getters
    public int getRepairTime() {
        return repairTime;
    }

    public int getRefillTime() {
        return refillTime;
    }


    /**
     * Count the time since the start of the game.
     */
    public void destructionTimer() {
        destroyed = !((Duration.between(destructionTime, Instant.now()).getSeconds()) < Constants.getFortressDestructionTime());
        if(destroyed){
            System.out.println("STATION HAS BEEN DESTROYED");
            this.texName = "lavaTile.png";
        }
    }

    /**
     * Repair tiles that have fire engines on
     *
     * @param surroundingTiles the tiles you want to repair
     */
    public void repairTiles(ArrayList<Tile> surroundingTiles) {
        for (Tile surroundingTile : surroundingTiles) {
            if(!destroyed) {
                destructionTimer();
                if (surroundingTile.getInhabitant() instanceof FireEngine) {
                    surroundingTile.getInhabitant().setHealth(Math.min(surroundingTile.getInhabitant().getHealth() + Constants.getStationRepairAmount(), surroundingTile.getInhabitant().getMaxHealth()));
                 }
            }
        }
    }

    /**
     * Refill tiles that have fire engines on
     *
     * @param surroundingTiles the tiles you want to refill
     */
    public void refillTiles(ArrayList<Tile> surroundingTiles) {
        for (Tile surroundingTile : surroundingTiles) {
            if (surroundingTile.getInhabitant() instanceof FireEngine && !destroyed) {
                ((FireEngine) surroundingTile.getInhabitant()).setWaterAmount(Math.min(((FireEngine) surroundingTile.getInhabitant()).getWaterAmount() + Constants.getStationRefillAmount(), ((FireEngine) surroundingTile.getInhabitant()).getWaterCapacity()));
            }
        }
    }

    public boolean isDestroyed() { return destroyed; }

    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
