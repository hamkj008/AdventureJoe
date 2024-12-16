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
    private final ArrayList<Coin> removedCoins;
    private final ArrayList<Chest> chestList;
    private final ArrayList<Chest> removedChests;
    private final ArrayList<Object> combinedList;
    private final PowerUp[] powerUpList;
    private final LevelEnd levelEnd;
    private final ArrayList<Vector2> positions;
    private int minPositionDistance = 1500;

    // ===================================================================================================================

    public GameObjects(int numberOfCoins, int numberOfChests, int numberOfPowerUps, int levelXBoundary) {

        coinList            = new ArrayList<>(numberOfCoins);
        chestList           = new ArrayList<>(numberOfChests);
        powerUpList         = new PowerUp[numberOfPowerUps];
        levelEnd            = new LevelEnd();
        removedCoins        = new ArrayList<>();
        removedChests       = new ArrayList<>();
        positions           = new ArrayList<>();
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
        for(int i = 0; i < 40; i++) {
            coinList.add(new Coin());
            combinedList.add(coinList.get(i));
        }

        // Generate all the positions
        generateRandomMinDistancePositions(levelXBoundary);

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

    /**
     *  Generates a set of random positions for all the objects in the game with a maximum X boundary the end of the level,
     *  and a minimum y boundary of the ground level (so objects should not appear in the ground).
     *  Also sets a minimum distance that any object can be from the next object
     *  @param levelXBoundary the end boundary of the level
     */
    public void generateRandomMinDistancePositions(int levelXBoundary) {

        for(int i =0; i < combinedList.size(); i++) {
            int retries = 0;
            int maxRetries = 10;
            Vector2 position = createRandomPosition(levelXBoundary);

            while (!isValidPosition(position)) {
                retries++;
                if (retries == maxRetries) {
                    Gdx.app.log("position", "Min distance reduced");
                    minPositionDistance -= 50;
                    retries = 0;
                }
                if (minPositionDistance <= 50) {
                    Gdx.app.log("position", "cannot place object. Min distance reached");
                    break;
                }
                position = createRandomPosition(levelXBoundary);
            }
            positions.add(position);
        }
    }

    public boolean isValidPosition(Vector2 positionToCheck) {

        for(Vector2 position : positions) {
            Gdx.app.log("position", "pos: " + positionToCheck.x + "   range: " + (position.x - minPositionDistance) + " <-> " + (position.x + minPositionDistance));
            if(positionToCheck.x > position.x - minPositionDistance && positionToCheck.x < position.x + minPositionDistance) {
                return false;
            }
        }
        return true;
    }

    // ===================================================================================================================

    /**
     * Creates a random position for game objects, making sure they are above ground level
     * and dispersed along the x up to the x boundarylimit
     **/
    public static Vector2 createRandomPosition(int levelXBoundary) {

        Random rand     = new Random();
        float randX     = 200 + (levelXBoundary - 200) * rand.nextFloat();
        float randY     = LevelFactory.getCurrentGroundLevel() + ((Gdx.graphics.getHeight() - 200) - LevelFactory.getCurrentGroundLevel()) * rand.nextFloat();

        return new Vector2(randX, randY);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "GameObjects.dispose");

        for(Chest chest : chestList) {
            chest.dispose();
        }
        for(PowerUp powerUp : powerUpList) {
            powerUp.dispose();
        }
        for(Coin coin : coinList) {
            coin.dispose();
        }
    }

    // ===================================================================================================================

    public LevelEnd getLevelEnd() { return levelEnd; }

    public PowerUp[] getPowerUpList() { return powerUpList; }

    public ArrayList<Chest> getChestList() { return chestList; }

    public ArrayList<Coin> getCoinList() { return coinList; }
}
