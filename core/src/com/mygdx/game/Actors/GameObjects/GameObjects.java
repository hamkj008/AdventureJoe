package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Random;



/**
 * A collection of objects needed for each level.
 * Collecting them together allows them to be processed together.
 */
public class GameObjects extends Actor {

    private ArrayList<Coin>     coinList;
    private ArrayList<Coin>     removedCoins;
    private ArrayList<Chest>    chestList;
    private ArrayList<Chest>    removedChests;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<Object>   combinedList;
    private PowerUp[]           powerUpList;
    private LevelEnd            levelEnd;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<Vector2>  positions;
    @SuppressWarnings("FieldCanBeLocal")
    private final int           minPositionDistance = 500;

    // ===================================================================================================================

    public GameObjects() {}

    // ===================================================================================================================

    public void spawnGameObjects(int numberOfCoins, int numberOfChests, int numberOfPowerUps, int levelXBoundary) {

        coinList            = new ArrayList<>(numberOfCoins);
        chestList           = new ArrayList<>(numberOfChests);
        powerUpList         = new PowerUp[numberOfPowerUps];
        levelEnd            = new LevelEnd();
        removedCoins        = new ArrayList<>();
        removedChests       = new ArrayList<>();
        combinedList        = new ArrayList<>();

        // Create all the objects
        for(int i = 0; i < numberOfChests; i++) {
            chestList.add(ChestCreator.createRandomChest());
            combinedList.add(chestList.get(i));
        }
        for(int i = 0; i < numberOfPowerUps; i++) {
            powerUpList[i] = new PowerUp();
            combinedList.add(powerUpList[i]);
        }
        for(int i = 0; i < numberOfCoins; i++) {
            coinList.add(new Coin());
            combinedList.add(coinList.get(i));
        }

        // Generate all the positions
        positions = GameScreen.getInstance().getHelper().generateRandomMinDistancePositions(levelXBoundary, combinedList.size(), minPositionDistance);

        // Assign positions to objects
        for(int i =0; i < combinedList.size(); i++) {

            if(combinedList.get(i) instanceof Chest) {
                ((Chest) combinedList.get(i)).setAnimations(positions.get(i).x, positions.get(i).y);
            }

            else if(combinedList.get(i) instanceof PowerUp) {
                ((PowerUp) combinedList.get(i)).getSprite().setPosition(positions.get(i).x, positions.get(i).y);
            }

            else if(combinedList.get(i) instanceof Coin) {
                ((Coin) combinedList.get(i)).getSprite().setPosition(positions.get(i).x, positions.get(i).y);
            }
        }
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        // Same strategy to draw active coins and remove collected ones as used in projectile spawner
        for(Coin coin : coinList) {
            if(coin != null) {
                if(!coin.getCollected()) {
                    coin.draw(batch, alpha);
                }
                else {
                    removedCoins.add(coin);
                }
            }
            else {
                Gdx.app.log("error", "Error: coin is null in draw");
            }
        }

        for(Chest chest : chestList) {
            if(chest != null) {
                if(!chest.getChestCollected()) {
                    chest.draw(batch, alpha);
                }
                else {
                    removedChests.add(chest);
                }
            }
            else {
                Gdx.app.log("error", "Error: chest is null in draw");
            }
        }

        for(PowerUp powerUp : powerUpList) {
            if(powerUp != null) {
                powerUp.draw(batch, alpha);
            }
            else {
                Gdx.app.log("error", "Error: powerUp is null in draw");
            }
        }

        if(levelEnd != null) {
            levelEnd.draw(batch, alpha);
        }
        else {
            Gdx.app.log("error", "Error: levelEnd is null in draw");
        }

        for(Coin removedCoin: removedCoins) {
            coinList.remove(removedCoin);
            removedCoin.dispose();
        }
        removedCoins.clear();

        for(Chest removedChest: removedChests) {
            chestList.remove(removedChest);
            removedChest.dispose();
        }
        removedChests.clear();
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        for(Chest chest : chestList) {
            chest.act(delta);
        }

        for(PowerUp powerUp : powerUpList) {
            powerUp.act(delta);
        }

        levelEnd.act(delta);
    }

    // ===================================================================================================================

    public void checkCollided(Player player) {

        for(Coin coin : coinList) {
            coin.checkCollided(player);
        }

        for(Chest chest : chestList) {
            chest.checkCollided(player);
        }

        for(PowerUp powerUp : powerUpList) {
            powerUp.checkCollided(player);
        }
    }

    // ===================================================================================================================

    /**
     *  Moves the characters in the opposite direction to oppose the cameras movement,
     *  giving the impression that they are not moving if they are static objects.
     **/
    public void compensateCamera(float cameraPositionAmount) {

        for(Coin coin : coinList) {
            coin.compensateCamera(cameraPositionAmount);
        }
        for(Chest chest : chestList) {
            chest.compensateCamera(cameraPositionAmount);
        }
        for(PowerUp powerUp : powerUpList) {
            powerUp.compensateCamera(cameraPositionAmount);
        }

        levelEnd.compensateCamera(cameraPositionAmount);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "GameObjects.dispose");

        if(chestList != null) {
            for (Chest chest : chestList) {
                chest.dispose();
            }
        }
        if(powerUpList != null) {
            for (PowerUp powerUp : powerUpList) {
                powerUp.dispose();
            }
        }

        if(coinList != null) {
            for (Coin coin : coinList) {
                coin.dispose();
            }
        }
    }

    // ===================================================================================================================

    public LevelEnd getLevelEnd() { return levelEnd; }

    public PowerUp[] getPowerUpList() { return powerUpList; }

    public ArrayList<Chest> getChestList() { return chestList; }

    public ArrayList<Coin> getCoinList() { return coinList; }
}
