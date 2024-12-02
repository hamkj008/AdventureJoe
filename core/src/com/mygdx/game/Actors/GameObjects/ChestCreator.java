package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 *  A Factory to create chests.
 *  Can create four different types of chests with animations, specified by the chest type.
 **/
public class ChestCreator {


    public static Chest createChest01(int positionX, int positionY) {

        Chest chest = new Chest();
        chest.value = 100;

        chest.animations[0] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_open.png"));

        chest.setAnimations(positionX, positionY);

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest02(int positionX, int positionY) {

        Chest chest = new Chest();
        chest.value = 200;

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_open.png"));

        chest.setAnimations(positionX, positionY);

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest03(int positionX, int positionY) {

        Chest chest = new Chest();
        chest.value = 300;

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_open.png"));

        chest.setAnimations(positionX, positionY);

        return chest;
    }

    // ===================================================================================================================

    public static Chest createChest04(int positionX, int positionY) {

        Chest chest = new Chest();
        chest.value = 400;

        chest.animations[0] = new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_closed.png"));
        chest.animations[1] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_light.png"));
        chest.animations[2] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_coins.png"));
        chest.animations[3] = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_open.png"));

        chest.setAnimations(positionX, positionY);

        return chest;
    }

    // ===================================================================================================================

}
