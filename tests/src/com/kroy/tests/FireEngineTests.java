package com.kroy.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.kroy.game.FireEngine;
import com.kroy.game.Fortress;
import com.kroy.game.Tile;
import com.kroy.game.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class FireEngineTests {
    Tile testTile = new Tile();
    FireEngine testFireEngine;
    Class ReflectionClass = FireEngine.class;

    @Before
    public void init(){
        testFireEngine = new FireEngine(100, 10, 10, testTile, 10, 100);
        Texture engineTexture = new Texture(Gdx.files.internal("fireEngineSprite.png"));
        testFireEngine.setTexture(engineTexture);
    }


    @Test
    public void testRefillAmount() {
        testFireEngine.setWaterAmount(0);
        testFireEngine.refillAmount(50);
        assertEquals(50, testFireEngine.getWaterAmount());
        testFireEngine.refillAmount(5000);
        assertEquals(100, testFireEngine.getWaterAmount());

    }


    @Test
    public void testCanShoot() {

    }


    @Test
    public void testDeath() {
        testFireEngine.takeDamage(testFireEngine.getMaxHealth());
        assertTrue(testFireEngine.isDisabled());
    }

    @Test
    public void testTakeDamage() {
        try {
            testFireEngine.setHealth(100);
            Method takeDamageReflection = ReflectionClass.getDeclaredMethod("takeDamage");
            takeDamageReflection.setAccessible(true);
            takeDamageReflection.invoke(50, takeDamageReflection);
            assertEquals(50, testFireEngine.getHealth());

            takeDamageReflection.invoke(5000, takeDamageReflection);
            assertEquals(0, testFireEngine.getHealth());

        } catch (NoSuchMethodException e) {

        } catch (InvocationTargetException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Test
    public void testTransferTo() {
        Tile newLocation = new Tile(2,2);
        try {
            Method transferToReflection = ReflectionClass.getDeclaredMethod("transferTo");
            transferToReflection.setAccessible(true);
            transferToReflection.invoke(newLocation, transferToReflection);
            assertEquals(testTile.getInhabitant(),null);
            assertEquals(newLocation.getInhabitant(), testFireEngine);

        } catch (NoSuchMethodException e) {

        } catch (InvocationTargetException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Test
    public void testShootTarget() {
        Fortress testFortress = new Fortress(1000, 10,2,testTile, "fortress1");

        testFireEngine.shootTarget(testFortress);

        assertEquals(testFortress.getHealth(), (testFortress.getMaxHealth() - testFireEngine.getDamage()));
        assertEquals(99, testFireEngine.getWaterAmount());
    }
}
