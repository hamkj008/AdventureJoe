package com.mygdx.game.Actors.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;


/**
 * Abstract super class for chests. Provides the template to make chests.
 */
public class Chest extends Actor {

    public enum ChestType { Chest01, Chest02, Chest03, Chest04 }
    private final ChestType chestType = ChestType.Chest01;

    // Chest animations
    protected Sprite[] animations = new Sprite[4];
    private int animationIndex;

    protected int value ;

    private final Sound chestCollectedSound;
    private boolean playChestCollectedSound = false;

    // Chest states
    protected enum ChestState{ OPEN, CLOSE }

    private ChestState state = ChestState.CLOSE;

    private float elapsedTime = 0f;
    private final float cycleTime = 0.5f;


    // ===================================================================================================================

    public Chest() {
        this.chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));
    }

    // ===================================================================================================================

    public void checkCollided(Player player) {

        if(player.getSprite().getBoundingRectangle().overlaps(animations[0].getBoundingRectangle())) {
            playChestCollectedSound();
            state = ChestState.OPEN;
        }
        else {
            playChestCollectedSound = true;
        }
    }

    // ===================================================================================================================

    public void act(float delta) {
        elapsedTime += delta;

        if (state == ChestState.OPEN){
            if (elapsedTime >= cycleTime && animationIndex < (animations.length - 1)){
                animationIndex ++;
                elapsedTime = 0f;
            }
        }
    }

    // ===================================================================================================================

    public void draw(Batch batch){

        if(animationIndex < (animations.length - 1)) {
            animations[animationIndex].draw(batch);
        }
    }

    // ===================================================================================================================

    public void playChestCollectedSound() {

        if(playChestCollectedSound) {
            chestCollectedSound.play();
            playChestCollectedSound = false;
        }
    }

    // ===================================================================================================================

    // Moves the power up in the opposite direction to the players camera movement, giving the appearance of being a static object.
    public void compensateCamera(float cameraPositionAmount) {

        for (Sprite sprite : animations){
            sprite.translate(cameraPositionAmount, 0);
        }
    }

    // ===================================================================================================================

    public int getValue() {
        return value;
    }

    // ===================================================================================================================

    public Sprite getCurrentSprite(){
        if (animationIndex < animations.length){
            return animations[animationIndex];
        }
        return null;
    }

    // ===================================================================================================================

    public void setAnimations(int positionX, int positionY) {

        for (Sprite sprite : animations){
            sprite.setPosition(positionX,positionY);
            sprite.setSize(200, 200);
        }
    }

    // ===================================================================================================================

    public void dispose(){
        this.chestCollectedSound.dispose();
    }

    // ===================================================================================================================

}
