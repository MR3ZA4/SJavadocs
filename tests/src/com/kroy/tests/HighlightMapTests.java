package com.kroy.tests;

import com.kroy.game.HighlightMap;
import com.kroy.game.Tile;
import com.kroy.game.TileType;
import org.junit.Test;

import static org.junit.Assert.*;

public class HighlightMapTests {

    @Test
    /**
     * Tests the constructor of the highlightMap correctly assigns class variables
     */
    public void testDefaultConstructor(){
        HighlightMap testHighlightMap = new HighlightMap(5,6);

        assertEquals(5, testHighlightMap.getMapWidth());
        assertEquals(6, testHighlightMap.getMapHeight());
    }


    @Test
    /***
     * Tests whether the highlightMap can be correctly generated and correctly assigns map data
     */
    public void testGenerateMap(){

        HighlightMap testHighlightMap = new HighlightMap(3,3);
        testHighlightMap.generateMap();

        Tile testTile = testHighlightMap.getMapData()[1][1];

        assertEquals(1, testTile.getMapX());
        assertEquals(1, testTile.getMapY());
        assertEquals("HighlightTexture/blank.png", testTile.getTexName());
        assertEquals(TileType.TILE_TYPES_HIGHLIGHT, testTile.getType());
    }

    @Test
    /***
     * Tests whether you are correctly able to change the tiles at locations in the highlight map
     */
    public void testSetTile(){

        HighlightMap testHighlightMap = new HighlightMap(3,3);
        Tile tile1 = new Tile(0,0);
        Tile tile2 = new Tile(1,0);
        Tile[][] testMapData = {{tile1, tile2}};
        testHighlightMap.setMapData(testMapData);

        testHighlightMap.setTile(0,0,TileType.TILE_TYPES_FORTRESS);
        testHighlightMap.setTile(1,0,TileType.TILE_TYPES_ROAD);


        Tile fortressTile = testHighlightMap.getMapData()[0][0];
        Tile roadTile = testHighlightMap.getMapData()[0][1];

        assertEquals("HighlightTexture/attack.png", fortressTile.getTexName());
        assertEquals("HighlightTexture/move.png", roadTile.getTexName());


    }


    @Test
    public void testResetMap() {

        HighlightMap testHighlightMap = new HighlightMap(2,1);

        Tile tile1 = new Tile(0,0,"whoCares",TileType.TILE_TYPES_FORTRESS);
        Tile tile2 = new Tile(1,0,"whoCares",TileType.TILE_TYPES_ROAD);

        Tile[][] testMapData = {{tile1, tile2}};
        testHighlightMap.setMapData(testMapData);


        testHighlightMap.resetMap();

        Tile fortressTile = testHighlightMap.getMapData()[0][0];
        Tile roadTile = testHighlightMap.getMapData()[0][1];

        assertEquals("HighlightTexture/blank.png", fortressTile.getTexName());
        assertEquals("HighlightTexture/blank.png", roadTile.getTexName());

    }
}
