package com.mygdx.game.Actors.GameObjects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;



/**
 * A collectible Power Up that gives the player a more powerful rifle weapon for a short duration of 20 seconds,
 * and will then deactivate returning to the default weapon. The power up will respawn and can be collected again.
 */
public class PowerUp extends Actor {

    public enum PowerUpState { ACTIVE, INACTIVE }
    private PowerUpState powerUpState       = PowerUpState.INACTIVE;

    private final Sprite powerUpSprite;
    private final Vector2 startPosition;

    // Sounds
    private final Sound powerUpSound;
    private boolean playPowerUpSound        = false;

    private final Sound powerDownSound;
    private boolean playPowerDownSound      = false;


    private float timePeriod                = 0;
    // Power Up will last for 20 secs
    private int powerUpTimeDuration         = 20;


    // ===================================================================================================================

    public PowerUp(String filePath, String powerUpSoundPath, String powerDownSoundPath) {

        powerUpSprite = new Sprite(new Texture(filePath));
        startPosition = new Vector2();

        powerUpSound    = Gdx.audio.newSound(Gdx.files.internal(powerUpSoundPath));
        powerDownSound  = Gdx.audio.newSound(Gdx.files.internal(powerDownSoundPath));
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        if(powerUpState == PowerUpState.INACTIVE) {
            batch.draw(powerUpSprite.getTexture(), powerUpSprite.getX(), powerUpSprite.getY(),
                        powerUpSprite.getWidth(), powerUpSprite.getHeight());
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        timePeriod += delta;

        // Countdown for Power Up duration
        if(timePeriod > 1) {
            timePeriod = 0;
            powerUpTimeDuration -= 1;

            if(powerUpTimeDuration == 0) {
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

    public void reset() {
        powerUpSprite.setPosition(startPosition.x, startPosition.y);
    }

    // ===================================================================================================================

    // Check for collisions
    public void checkCollided(Player player) {

        if(player.getSprite().getBoundingRectangle().overlaps(powerUpSprite.getBoundingRectangle())) {
            powerUpState = PowerUpState.ACTIVE;
            player.setWeaponType(Player.WeaponType.RIFLE);
//            player.setWeapon();
//            player.setRifle();
//            player.setPowerUp(true);
        }

        if(powerUpState == PowerUpState.INACTIVE) {
            player.setWeaponType(Player.WeaponType.HANDGUN);
//            player.setWeapon();
//            player.setWeapon(player.setWeaponType(Player.WeaponType.HANDGUN));
//            player.setHandgun();
//            player.setPowerUp(false);
        }
    }

    // ===================================================================================================================

    public void playPowerUpSound() {
        if(playPowerUpSound) {
            powerUpSound.play();
            playPowerUpSound = false;
        }
    }

    // ===================================================================================================================

    public void playPowerDownSound() {
        if(playPowerDownSound) {
            powerDownSound.play();
            playPowerDownSound = false;
        }
    }

    // ===================================================================================================================

    // Moves the power up in the opposite direction to the players camera movement, giving the appearance of being a static object.
    public void compensateCamera(float cameraPositionAmount) {

       powerUpSprite.translate(cameraPositionAmount, 0);
    }

    // ===================================================================================================================

    public void dispose() {
        powerUpSound.dispose();
        powerDownSound.dispose();
    }

    // ===================================================================================================================

    public Vector2 getStartPosition() { return startPosition; }
}
