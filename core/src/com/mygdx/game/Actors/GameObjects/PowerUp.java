package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;



/**
 * A collectible Power Up that gives the player a more powerful rifle weapon for a short duration of 20 seconds,
 * and will then deactivate returning to the default weapon. The power up will respawn and can be collected again.
 */
public class PowerUp extends Actor {

    public enum PowerUpState {ACTIVE, INACTIVE}
    private static PowerUpState powerUpState = PowerUpState.INACTIVE;           // All power ups share the same state

    private final Sprite sprite;

    // Sounds
    private final Sound powerUpSound;
    private boolean playPowerUpSound        = false;

    private final Sound powerDownSound;
    private boolean playPowerDownSound      = false;

    private float timePeriod                = 0;
    // Power Up will last for 20 secs
    private int powerUpTimeDuration         = 20;


    // ===================================================================================================================

    public PowerUp() {

        sprite = new Sprite(new Texture("Game Objects/Powerup_Diamond.png"));

        powerUpSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/PowerUp.mp3"));
        powerDownSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/PowerDown.mp3"));
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        if (powerUpState == PowerUpState.INACTIVE) {
            batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY(),
                    sprite.getWidth(), sprite.getHeight());
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        timePeriod += delta;

        // Countdown for Power Up duration
        if (timePeriod > 1) {
            timePeriod = 0;
            powerUpTimeDuration -= 1;

            if (powerUpTimeDuration == 0) {
                powerUpState = PowerUpState.INACTIVE;
                powerUpTimeDuration = 20;
            }
        }
        switchState();
    }

    // ===================================================================================================================

    // Sets whether the power up is activated or not.
    public void switchState() {

        switch (powerUpState) {

            case ACTIVE:
                playPowerDownSound = true;
                playPowerUpSound();
                break;

            case INACTIVE:
                playPowerUpSound = true;
                playPowerDownSound();
                break;
        }
    }

    // ===================================================================================================================

    // Check for collisions
    public void checkCollided(Player player) {

        if (player.getSprite().getBoundingRectangle().overlaps(sprite.getBoundingRectangle())) {
            powerUpState = PowerUpState.ACTIVE;
            // Notify the player to change the weapon type
            if(player.getWeaponType() != Player.WeaponType.RIFLE) {
                player.setWeapon(Player.WeaponType.RIFLE);
            }
        }

        if (powerUpState == PowerUpState.INACTIVE) {
            if(player.getWeaponType() != Player.WeaponType.HANDGUN) {
                // Notify the player to change the weapon type
                player.setWeapon(Player.WeaponType.HANDGUN);
            }
        }
    }

    // ===================================================================================================================

    public void playPowerUpSound() {
        if (playPowerUpSound) {
            powerUpSound.play();
            playPowerUpSound = false;
        }
    }

    // ===================================================================================================================

    public void playPowerDownSound() {
        if (playPowerDownSound) {
            powerDownSound.play();
            playPowerDownSound = false;
        }
    }

    // ===================================================================================================================

    // Moves the power up in the opposite direction to the players camera movement, giving the appearance of being a static object.
    public void compensateCamera(float cameraPositionAmount) {

        sprite.translate(cameraPositionAmount, 0);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "powerup.dispose");

        powerUpSound.dispose();
        powerDownSound.dispose();
    }

    // ===================================================================================================================

    public Sprite getSprite() { return sprite; }
}