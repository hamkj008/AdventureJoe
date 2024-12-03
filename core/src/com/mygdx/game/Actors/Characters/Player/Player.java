package com.mygdx.game.Actors.Characters.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.ProjectileSpawner;
import com.mygdx.game.Game.GameStateController;
import com.mygdx.game.Screens.GameScreen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * The player class. Inherits from the Character super class.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Player extends Character {

    // Set movement speeds
    private final int runningSpeed          = 500;
    private final int jumpingSpeed          = 600;
    private final int fallingSpeed          = 800;

    // Point where the state switches from jumping to falling
    private final int terminal_Velocity     = 500;

    // Guard that acts as a check to prevent other states from being enacted before the jump has finished.
    private boolean grounded                = true;

    private float playerLevel;

    public enum WeaponType { HANDGUN, RIFLE };
    private WeaponType weaponType = WeaponType.HANDGUN;

    // ---- STATE ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingStartAnimation;
    private Animation<TextureRegion> jumpingLoopAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

//
//    // ---- ALL ANIMATIONS -------------------------
//    private Animation<TextureRegion> idleHandgunAnimation;
//    private Animation<TextureRegion> idleRifleAnimation;
//    private Animation<TextureRegion> runningHandgunAnimation;
//    private Animation<TextureRegion> runningRifleAnimation;
//    private Animation<TextureRegion> jumpingStartHandgunAnimation;
//    private Animation<TextureRegion> jumpingStartRifleAnimation;
//    private Animation<TextureRegion> jumpingLoopHandgunAnimation;
//    private Animation<TextureRegion> jumpingLoopRifleAnimation;
//    private Animation<TextureRegion> attackingHandgunAnimation;
//    private Animation<TextureRegion> attackingRifleAnimation;
//    private Animation<TextureRegion> hurtHandgunAnimation;
//    private Animation<TextureRegion> hurtRifleAnimation;
//    private Animation<TextureRegion> dyingHandgunAnimation;
//    private Animation<TextureRegion> dyingRifleAnimation;

    private Map<String, Animation<TextureRegion>> weaponAnimations;
    private final Map<String, Animation<TextureRegion>> rifleAnimations;
    private final Map<String, Animation<TextureRegion>> handgunAnimations;


    private final Sound jumpSound;
    private boolean playJumpSound = true;

    private final Sound hurtSound;
    private boolean playHurtSound = true;

    private final Sound dieSound;
    private boolean playDieSound = true;

    private final float handgunSpeed          = 500f;
    private final float handgunReloadSpeed    = 2f;
    private final int handgunDamage           = 20;

    private final float rifleSpeed            = 1200f;
    private final float rifleReloadSpeed      = 0.6f; // Increments of 0.2
    private final int rifleDamage             = 35;

    // ===================================================================================================================

    public Player() {
        Gdx.app.log("flow", "Player");

        // Initialize start position
        getStartPosition().x = 200;
        playerLevel = getCharacterGroundLevel();
        getStartPosition().y = playerLevel;
        setDirection(Direction.RIGHT);

        weaponAnimations    = new HashMap<>();
        handgunAnimations   = new HashMap<>();
        rifleAnimations     = new HashMap<>();

        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        handgunAnimations.put("idleAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle Blinking - Handgun.png", 9, 2, 18));
        handgunAnimations.put("runningAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Handgun.png", 9, 2, 18));
        handgunAnimations.put("jumpingStartAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Handgun.png", 3, 2, 6));
        handgunAnimations.put("jumpingLoopAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Handgun.png", 3, 2, 6));
        handgunAnimations.put("attackingAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Handgun.png", 3, 3, 9));
        handgunAnimations.put("hurtAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Handgun.png", 4, 3, 12));
        handgunAnimations.put("dyingAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Handgun.png", 3, 4, 12));

        rifleAnimations.put("idleAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle Blinking - Rifle.png", 6, 3, 18));
        rifleAnimations.put("runningAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Rifle.png", 6, 3, 18));
        rifleAnimations.put("jumpingStartAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Rifle.png", 3, 2, 6));
        rifleAnimations.put("jumpingLoopAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Rifle.png", 3, 2, 6));
        rifleAnimations.put("attackingAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Rifle.png", 4, 1, 4));
        rifleAnimations.put("hurtAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Rifle.png", 4, 3, 12));
        rifleAnimations.put("dyingAnimation", GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Rifle.png", 4, 3, 12));

        jumpSound   = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Jump.mp3"));
        hurtSound   = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Hurt.mp3"));
        dieSound    = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Dying.mp3"));

        super.getProjectileOffset().put("leftOffset", new Vector2(100, 0));
        super.getProjectileOffset().put("rightOffset", new Vector2(200, 100));
        super.setProjectileSpawner(new ProjectileSpawner("Game Characters/Player/PlayerProjectile.png", "Audio/Sounds/shot.mp3",
                new Vector2(45, 25), handgunReloadSpeed));
        super.getProjectileSpawner().setMovementSpeed(handgunSpeed);
    }

    // ===================================================================================================================

    @Override
    public void spawnProjectile() {
        super.getProjectileSpawner().spawnProjectile(this, GameScreen.getInstance().getGameStateController().getRandomEnemy());
    }

    // ===================================================================================================================

    // Resets the player if it has lost a life.
    public void reset() {
        Gdx.app.log("flow", "Player/reset");

        // Player is alive again
        super.setIsAlive(true);
        // Health back to full health
        super.setHealth(getMax_Health());
        // Back to start position and idle
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);
        grounded = true;
        super.setCharacterState(CharacterState.IDLE);
        GameScreen.getInstance().getUiController().getPlayerHealthBar().reset();
    }

    // ===================================================================================================================

    // Checks to see if the player is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    // *** COMMENT OUT THIS METHOD FOR GOD MODE ***
    @Override
    public void healthCheck(int damage) {

        // The player can only get hurt or die when on the ground.
//        if(grounded) {
//            super.healthCheck(damage);
//            GameScreen.getInstance().getUiController().getPlayerHealthBar().modifyHealth(getHealth());
//        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        super.act(delta);
        switchStates();
    }

    // ===================================================================================================================

    // A state machine. Applies the correct animations, movement and other conditions to the various player states.
    public void switchStates() {

        // Normal player animations have a handgun equipped, but if a power up is enabled, all the animations are set to the more powerful rifle weapon.
//        if (powerUp) {
//            idleAnimation               = idleRifleAnimation;
//            runningAnimation            = runningRifleAnimation;
//            jumpingStartAnimation       = jumpingStartRifleAnimation;
//            jumpingLoopAnimation        = jumpingLoopRifleAnimation;
//            attackingAnimation          = attackingRifleAnimation;
//            hurtAnimation               = hurtRifleAnimation;
//            dyingAnimation              = dyingRifleAnimation;
//
//            // The rifle is faster and does more damage
//            super.setDamage(rifleDamage);
//            super.getProjectileSpawner().setMovementSpeed(rifleSpeed);
//            super.getProjectileSpawner().setProjectileReloadSpeed(rifleReloadSpeed);
//        }
//        else {
//            idleAnimation               = idleHandgunAnimation;
//            runningAnimation            = runningHandgunAnimation;
//            jumpingStartAnimation       = jumpingStartHandgunAnimation;
//            jumpingLoopAnimation        = jumpingLoopHandgunAnimation;
//            attackingAnimation          = attackingHandgunAnimation;
//            hurtAnimation               = hurtHandgunAnimation;
//            dyingAnimation              = dyingHandgunAnimation;
//
//            super.setDamage(handgunDamage);
//            super.getProjectileSpawner().setMovementSpeed(handgunSpeed);
//            super.getProjectileSpawner().setProjectileReloadSpeed(handgunReloadSpeed);
//        }

        // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
        switch (super.getCharacterState()) {
            case IDLE:
                // Set the speed and animation for idle
                super.setCURRENT_MOVEMENT_SPEED(0);
                playDieSound = true;
                playHurtSound = true;
                super.loopingAnimation(weaponAnimations.get("idleAnimation"));
                break;

            case MOVING:
                // Set the speed and animation for running
                super.setCURRENT_MOVEMENT_SPEED(runningSpeed);
                super.loopingAnimation(weaponAnimations.get("runningAnimation"));
                break;

            case JUMPING:
                // Grounded is a guard so that the player cannot enter other states untill the jump has finished
                grounded = false;
                super.setCURRENT_MOVEMENT_SPEED(jumpingSpeed);
                if(!grounded) {
                    // Start jumping
                    jumpCharacter();
                    playJumpSound();
                    // Once the animation has finished and terminal velocity has been hit, the player can start falling
                    if (super.nonLoopingAnimation(weaponAnimations.get("jumpingStartAnimation"))) {
                        if (getSprite().getY() > terminal_Velocity) {
                            super.setCharacterState(CharacterState.FALLING);
                        }
                    }
                }
                break;

            case FALLING:
                super.setCURRENT_MOVEMENT_SPEED(fallingSpeed);
                super.nonLoopingAnimation(weaponAnimations.get("jumpingLoopAnimation"));
                // Start falling
                fallCharacter();
                playJumpSound = true;

                // Once the player has returned back to ground level, it is set at ground level to prevent falling offscreen.
                if (getSprite().getY() < getCharacterGroundLevel()) {
                    getSprite().setPosition(getSprite().getX(), getCharacterGroundLevel());
                    grounded = true;
                    playerLevel = getCharacterGroundLevel();
                    super.setCharacterState(CharacterState.IDLE);
                }
                break;

            case ATTACKING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                // Reset to idle after animation has finished
                if (super.nonLoopingAnimation(weaponAnimations.get("attackingAnimation"))) {
                    super.setCharacterState(CharacterState.IDLE);
                }
                break;

            case HURT:
                super.setCURRENT_MOVEMENT_SPEED(0);
                playHurtSound();
                // Reset to idle after animation has finished
                if (super.nonLoopingAnimation(weaponAnimations.get("hurtAnimation"))) {
                    super.setCharacterState(CharacterState.IDLE);
                }
                break;

            case DYING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                playDieSound();
                if (super.nonLoopingAnimation(weaponAnimations.get("dyingAnimation"))) {
                    super.setIsAlive(false);
                    super.setCharacterState(CharacterState.DEAD);
                    GameStateController.playerLives -= 1;
                }
                break;
        }
    }

    // ========================================== MOVEMENT =================================================================

    /**
     Takes the current movement speed and uses Game Helper to apply deltaTime giving the total speed.
     Can then apply this to find the new position for the sprite which is then translated to that position.
     Player needs its own version of moveCharacter.
     It overrides the super.moveCharacter so that other characters don't have the camera speed added to their movement
     */
    @Override
    public void moveCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = 0;

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }
    }

    // ===================================================================================================================

    // Same as moveCharacter but applied to jumping. Adds the jumping speed to the Y axis.
    public void jumpCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }
    }

    // ===================================================================================================================

    // Same as moveCharacter but applied to falling. Subtracts the falling speed to the Y axis.
    public void fallCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = GameScreen.getInstance().getHelper().setMovement(-getCURRENT_MOVEMENT_SPEED());

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }
    }

    // ===================================================================================================================

    public void setWeapon() {

        switch (weaponType) {
            case HANDGUN:
                weaponAnimations = handgunAnimations;
                super.setDamage(handgunDamage);
                super.getProjectileSpawner().setMovementSpeed(handgunSpeed);
                super.getProjectileSpawner().setProjectileReloadSpeed(handgunReloadSpeed);
                break;

            case RIFLE:
                weaponAnimations = rifleAnimations;
                super.setDamage(rifleDamage);
                super.getProjectileSpawner().setMovementSpeed(rifleSpeed);
                super.getProjectileSpawner().setProjectileReloadSpeed(rifleReloadSpeed);
                break;
        }
    }


//    public void setRifle() {
//        idleAnimation               = idleRifleAnimation;
//        runningAnimation            = runningRifleAnimation;
//        jumpingStartAnimation       = jumpingStartRifleAnimation;
//        jumpingLoopAnimation        = jumpingLoopRifleAnimation;
//        attackingAnimation          = attackingRifleAnimation;
//        hurtAnimation               = hurtRifleAnimation;
//        dyingAnimation              = dyingRifleAnimation;
//
//        // The rifle is faster and does more damage
//        super.setDamage(rifleDamage);
//        super.getProjectileSpawner().setMovementSpeed(rifleSpeed);
//        super.getProjectileSpawner().setProjectileReloadSpeed(rifleReloadSpeed);
//    }

    // ===================================================================================================================

//    public void setHandgun() {
//        idleAnimation               = idleHandgunAnimation;
//        runningAnimation            = runningHandgunAnimation;
//        jumpingStartAnimation       = jumpingStartHandgunAnimation;
//        jumpingLoopAnimation        = jumpingLoopHandgunAnimation;
//        attackingAnimation          = attackingHandgunAnimation;
//        hurtAnimation               = hurtHandgunAnimation;
//        dyingAnimation              = dyingHandgunAnimation;
//
//        super.setDamage(handgunDamage);
//        super.getProjectileSpawner().setMovementSpeed(handgunSpeed);
//        super.getProjectileSpawner().setProjectileReloadSpeed(handgunReloadSpeed);
//    }

    // ======================================== SOUNDS =================================================================

    public void playJumpSound() {
        if(playJumpSound) {
            jumpSound.play();
            playJumpSound = false;
        }
    }

    public void playHurtSound() {
        if(playHurtSound) {
            hurtSound.play();
            playHurtSound = false;
        }
    }

    public void playDieSound() {
        if(playDieSound) {
            dieSound.play();
            playDieSound = false;
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "playerDispose");

        super.dispose();
        jumpSound.dispose();
        hurtSound.dispose();
        dieSound.dispose();
    }

    // =========================================== GETTERS AND SETTERS ========================================================

    public boolean getIsGrounded() { return grounded;}

    public void setIsGrounded(boolean grounded) { this.grounded = grounded; }

    public float getPlayerLevel() { return playerLevel; }

    public void setPlayerLevel(float playerLevel) { this.playerLevel = playerLevel; }

    public WeaponType getWeaponType() { return weaponType; }

    public void setWeaponType(WeaponType weaponType) { this.weaponType = weaponType; }
}
