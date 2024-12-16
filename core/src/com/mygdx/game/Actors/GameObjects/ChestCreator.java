package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Random;



/**
 *  A Factory to create chests randomly.
 *  Can create four different types of chests with animations.
 **/
public class ChestCreator {

    public static Chest createRandomChest() {
        Random rand     = new Random();
        int chestNum    = rand.nextInt(4);

        Chest chest     = new Chest();

        switch(chestNum) {
            case 0:
                chest = ChestCreator.createChest01();
                break;
            case 1:
                chest = ChestCreator.createChest02();
                break;
            case 2:
                chest = ChestCreator.createChest03();
                break;
            case 3:
                chest = ChestCreator.createChest04();
                break;
        }
        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest01() {

        Chest chest = new Chest();
        chest.setValue(100);

        chest.animations[0] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_open.png"));

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest02() {

        Chest chest = new Chest();
        chest.setValue(200);

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_open.png"));

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest03() {

        Chest chest = new Chest();
        chest.setValue(300);

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_open.png"));

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest04() {

        Chest chest = new Chest();
        chest.setValue(400);

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_open.png"));

        return chest;
    }

    // ===================================================================================================================

}
