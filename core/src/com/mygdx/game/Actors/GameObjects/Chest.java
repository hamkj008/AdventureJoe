package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;


/**
 * A Treasure chest collectible that can be collected by the player.
 */
public class Chest extends Actor {

    public enum ChestType { Chest01, Chest02, Chest03, Chest04 }
    private final ChestType chestType = ChestType.Chest01;

    // Chest animations
    protected Sprite[] animations;
    private int animationIndex;

    private int value = 0;

    private final Sound chestCollectedSound;
    private boolean playChestCollectedSound = false;

    // Chest states
    protected enum ChestState{ OPEN, CLOSE }

    private ChestState state = ChestState.CLOSE;

    private float elapsedTime = 0f;
    private final float cycleTime = 0.5f;


    // ===================================================================================================================

    public Chest() {

        animations          = new Sprite[4];
        chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));
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

    @Override
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

    @Override
    public void draw(Batch batch, float alpha){

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

    public void setAnimations(float positionX, float positionY) {

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

    public void setValue(int value) { this.value = value; }
}
