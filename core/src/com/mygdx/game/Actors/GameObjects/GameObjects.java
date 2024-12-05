package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Levels.LevelFactory;
import java.util.ArrayList;
import java.util.Random;



/**
 * A collection of objects needed for each level.
 * Collecting them together allows them to be processed together.
 */
public class GameObjects extends Actor {

    private final ArrayList<Coin> coinList;
    private final Chest[] chestList;
    private final PowerUp[] powerUpList;
    private final LevelEnd levelEnd;

    private final ArrayList<Coin> removedCoins;

    // ===================================================================================================================

    public GameObjects(int numberOfCoins, int numberOfChests, int numberOfPowerUps, int levelXBoundary) {

        coinList            = new ArrayList<>(numberOfCoins);
        chestList           = new Chest[numberOfChests];
        powerUpList         = new PowerUp[numberOfPowerUps];
        levelEnd            = new LevelEnd();
        removedCoins        = new ArrayList<>();

        for(int i = 0; i < numberOfCoins; i++) {
            coinList.add(new Coin());
            Vector2 position = createRandomPosition(levelXBoundary);
            coinList.get(i).getSprite().setPosition(position.x, position.y);
            Gdx.app.log("debug", "" + position.x);
        }
        for(int i = 0; i < numberOfChests; i++) {
            chestList[i]        = ChestCreator.createRandomChest();
            Vector2 position    = createRandomPosition(levelXBoundary);
            chestList[i].setAnimations(position.x, position.y);
        }
        for(int i = 0; i < numberOfPowerUps; i++) {
            powerUpList[i]      = new PowerUp();
            Vector2 position    = createRandomPosition(levelXBoundary);
            powerUpList[i].getSprite().setPosition(position.x, position.y);
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
                chest.draw(batch, alpha);
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

    public void reset() {

//        for(PowerUp powerUp : powerUpList) {
//            powerUp.reset();
//        }

        levelEnd.reset();
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

    /**
     * Creates a random position for game objects, making sure they are above ground level
     * and dispersed along the x up to the x boundarylimit
     **/
    public static Vector2 createRandomPosition(int levelXBoundary) {

        Random rand     = new Random();
        float randX     = 200 + (levelXBoundary - 200) * rand.nextFloat();
        float randY     = LevelFactory.getCurrentGroundLevel() + ((Gdx.graphics.getHeight() - 100) - LevelFactory.getCurrentGroundLevel()) * rand.nextFloat();
        return new Vector2(randX, randY);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "GameObjectsDispose");

        for(Chest chest : chestList) {
            chest.dispose();
        }
        for(PowerUp powerUp : powerUpList) {
            powerUp.dispose();
        }
    }

    // ===================================================================================================================

    public LevelEnd getLevelEnd() { return levelEnd; }

    public PowerUp[] getPowerUpList() { return powerUpList; }

    public Chest[] getChestList() { return chestList; }

    public ArrayList<Coin> getCoinList() { return coinList; }
}
