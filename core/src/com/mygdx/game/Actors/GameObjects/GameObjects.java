package com.mygdx.game.Actors.GameObjects;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;


/**
 * A collection of objects needed for each level.
 * Collecting them together allows them to be processed together.
 */
public class GameObjects extends Actor {

    private Chest chest;
    private final PowerUp powerUp;
    private final LevelEnd levelEnd;


    // ===================================================================================================================

    public GameObjects() {

        powerUp    = new PowerUp("Game Objects/Powerup_Diamond.png", "Audio/Sounds/PowerUp.mp3", "Audio/Sounds/PowerDown.mp3");
        chest      = new Chest();
        levelEnd   = new LevelEnd();
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {
        chest.draw(batch);
        powerUp.draw(batch, alpha);
        levelEnd.draw(batch, alpha);
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {
        powerUp.act(delta);
        levelEnd.act(delta);
        chest.act(delta);
    }

    // ===================================================================================================================

    public void reset() {
        powerUp.reset();
        levelEnd.reset();
    }

    // ===================================================================================================================

    public void checkCollided(Player player) {
        chest.checkCollided(player);
        powerUp.checkCollided(player);
    }

    // ===================================================================================================================

    /*
     * Moves the characters in the opposite direction to oppose the cameras movement,
     * giving the impression that they are not moving if they are static objects.
     */
    public void compensateCamera(float cameraPositionAmount) {
        powerUp.compensateCamera(cameraPositionAmount);
        levelEnd.compensateCamera(cameraPositionAmount);
        chest.compensateCamera(cameraPositionAmount);
    }


    // Creates the desired chest depending on the chest type provided.
    public void configureChest(Chest.ChestType chestType, int positionX, int positionY) {

        switch(chestType) {
            case Chest01:
                this.chest = ChestCreator.createChest01(positionX, positionY);
                break;
            case Chest02:
                this.chest = ChestCreator.createChest02(positionX, positionY);
                break;
            case Chest03:
                this.chest = ChestCreator.createChest03(positionX, positionY);
                break;
            case Chest04:
                this.chest = ChestCreator.createChest04(positionX, positionY);
                break;
        }
    }

    public void dispose() {
        powerUp.dispose();
        chest.dispose();
    }


    public PowerUp getPowerUp() { return powerUp; }

    public Chest getChest() { return chest; }

    public LevelEnd getLevelEnd() { return levelEnd; }
}
