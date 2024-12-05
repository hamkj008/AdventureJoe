package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;


public class Coin extends Actor {

    private boolean collected = false;

    // Chest animations
    private final Texture[] keyframes;
    private final Animation<Texture> animation;
    private final Sprite sprite;

    protected int value;

    private final Sound coinCollectedSound;
    private boolean playCoinCollectedSound = false;
    private float stateTime = 0f;


    // ===================================================================================================================

    public Coin() {

        sprite = new Sprite();
        keyframes = new Texture[6];

        keyframes[0] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/01.png");
        keyframes[1] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/02.png");
        keyframes[2] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/03.png");
        keyframes[3] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/04.png");
        keyframes[4] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/05.png");
        keyframes[5] = new Texture("Game Objects/Chests & Coins PNG/Coins/Gold Skull Coin/06.png");

        animation           = new Animation<>(0.1f, keyframes);
        coinCollectedSound  = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Coin.mp3"));

        sprite.setSize(100, 100);
    }

    // ===================================================================================================================

    public void checkCollided(Player player) {

        if(player.getSprite().getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            Gdx.app.log("debug", "overlap" + collected);
            playCoinCollectedSound();
            collected = true;
        }
        else {
            playCoinCollectedSound = true;
        }
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha){

        stateTime += Gdx.graphics.getDeltaTime();
        Texture currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    // ===================================================================================================================

    public void playCoinCollectedSound() {

        if(playCoinCollectedSound) {
            coinCollectedSound.play();
            playCoinCollectedSound = false;
        }
    }

    // ===================================================================================================================

    // Moves the power up in the opposite direction to the players camera movement, giving the appearance of being a static object.
    public void compensateCamera(float cameraPositionAmount) {

        sprite.translate(cameraPositionAmount, 0);
    }

    // ===================================================================================================================

    public void dispose(){
        Gdx.app.log("dispose", "CoinDispose");

        this.coinCollectedSound.dispose();
        for(Texture texture : keyframes) {
            texture.dispose();
        }
    }

    // ===================================================================================================================

    public Sprite getSprite() { return sprite; }

    public int getValue() { return value; }

    public boolean getCollected() { return collected; }
}
