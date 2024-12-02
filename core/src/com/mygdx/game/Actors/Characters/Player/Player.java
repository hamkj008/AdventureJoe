package com.mygdx.game.Actors.Characters.Player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.ProjectileSpawner;
import com.mygdx.game.Game.GameStateController;
import com.mygdx.game.Screens.GameScreen;


/**
 * The player class. Inherits from the Character super class.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Player extends Character {

    private boolean powerUp = false;

    // Set movement speeds
    private final int runningSpeed = 500;
    private final int jumpingSpeed = 600;
    private final int fallingSpeed = 800;

    // Point where the state switches from jumping to falling
    private final int terminal_Velocity = 500;

    // Guard that acts as a check to prevent other states from being enacted before the jump has finished.
    private boolean grounded = true;

    private float playerLevel;


    // ---- STATE ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingStartAnimation;
    private Animation<TextureRegion> jumpingLoopAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    // ---- ALL ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleHandgunAnimation;
    private final Animation<TextureRegion> idleRifleAnimation;
    private final Animation<TextureRegion> runningHandgunAnimation;
    private final Animation<TextureRegion> runningRifleAnimation;
    private final Animation<TextureRegion> jumpingStartHandgunAnimation;
    private final Animation<TextureRegion> jumpingStartRifleAnimation;
    private final Animation<TextureRegion> jumpingLoopHandgunAnimation;
    private final Animation<TextureRegion> jumpingLoopRifleAnimation;
    private final Animation<TextureRegion> attackingHandgunAnimation;
    private final Animation<TextureRegion> attackingRifleAnimation;
    private final Animation<TextureRegion> hurtHandgunAnimation;
    private final Animation<TextureRegion> hurtRifleAnimation;
    private final Animation<TextureRegion> dyingHandgunAnimation;
    private final Animation<TextureRegion> dyingRifleAnimation;


    private final Sound jumpSound;
    private boolean playJumpSound = true;

    private final Sound hurtSound;
    private boolean playHurtSound = true;

    private final Sound dieSound;
    private boolean playDieSound = true;

    private final ProjectileSpawner projectileSpawner;


    // ===================================================================================================================

    public Player() {
        Gdx.app.log("flow", "Player");

        // Initialize start position
        getStartPosition().x = 200;
        playerLevel = getCharacterGroundLevel();
        getStartPosition().y = playerLevel;
        setDirection(Direction.RIGHT);

        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleHandgunAnimation            = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle Blinking - Handgun.png", 9, 2, 18);
        idleRifleAnimation              = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle Blinking - Rifle.png", 6, 3, 18);
        runningHandgunAnimation         = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Handgun.png", 9, 2, 18);
        runningRifleAnimation           = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Rifle.png", 6, 3, 18);
        jumpingStartHandgunAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Handgun.png", 3, 2, 6);
        jumpingStartRifleAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Rifle.png", 3, 2, 6);
        jumpingLoopHandgunAnimation     = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Handgun.png", 3, 2, 6);
        jumpingLoopRifleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Rifle.png", 3, 2, 6);
        attackingHandgunAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Handgun.png", 3, 3, 9);
        attackingRifleAnimation         = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Rifle.png", 4, 1, 4);
        hurtHandgunAnimation            = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Handgun.png", 4, 3, 12);
        hurtRifleAnimation              = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Rifle.png", 4, 3, 12);
        dyingHandgunAnimation           = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Handgun.png", 3, 4, 12);
        dyingRifleAnimation             = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Rifle.png", 4, 3, 12);

        jumpSound   = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Jump.mp3"));
        hurtSound   = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Hurt.mp3"));
        dieSound    = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Dying.mp3"));

        projectileSpawner = new ProjectileSpawner("Game Characters/Player/PlayerProjectile.png", "Audio/Sounds/shot.mp3",
                new Vector2(45, 25), new Vector2(100, 100), 1);
    }

    // ===================================================================================================================

    public void spawnProjectile() {

        projectileSpawner.spawnProjectile(this, GameScreen.getInstance().getGameStateController().getRandomEnemy(),
                500f, getDirection());
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
        if(grounded) {
            super.healthCheck(damage);
            GameScreen.getInstance().getUiController().getPlayerHealthBar().modifyHealth(getHealth());
        }
    }

    // ===================================================================================================================

    // Additional conditions needed for projectiles. This is handled by overriding the draw method inherited from Character super class,
    // calling the super draw method, and then providing the additional conditions.
    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        if (super.getDirection() == Direction.LEFT) {
            projectileSpawner.getOffset().set(0, 100);
        }
        else if (super.getDirection() == Direction.RIGHT) {
            projectileSpawner.getOffset().set(200, 100);
        }

        projectileSpawner.draw(batch, alpha);
        /*
         Once a projectile has been reset this is polled to find out the direction the character is facing and apply that direction to the projectile.
         Once a projectile has been fired, the projectile direction has already been locked in, so it maintains the correct direction once it has been fired.
         Otherwise you see the projectile change direction mid flight if the player does.
         */
//        if(super.getProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
//
//            //Offsets are added to the projectile start position (in projectile reset state) for the projectile to emit from the correct spot on the player.
//            if (super.getDirection() == Direction.LEFT) {
//                super.getProjectile().getOffset().set(0, 100);
//                super.getProjectile().setDirection(Direction.LEFT);
//            }
//            if (super.getDirection() == Direction.RIGHT) {
//                super.getProjectile().getOffset().set(200, 100);
//                super.getProjectile().setDirection(Direction.RIGHT);
//            }
//        }
//        // Draw the projectile if it has been fired.
//        if(super.getProjectile().getProjectileState() == Projectile.ProjectileState.FIRING) {
//            super.getProjectile().draw(batch, alpha);
//        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        Gdx.app.log("debug", "" + projectileSpawner.canSpawn);

//        if(getCharacterState() == CharacterState.ATTACKING && projectileSpawner.canSpawn) {
//            spawnProjectile();
//        }
//        if(getCharacterState() == CharacterState.ATTACKING) {
        if (projectileSpawner.canSpawn) {
            spawnProjectile();
        }
        else {
            projectileSpawner.timerElapsed();
        }
//        }

        projectileSpawner.act(delta);

        switchStates();
    }

    // ===================================================================================================================

    // A state machine. Applies the correct animations, movement and other conditions to the various player states.
    public void switchStates() {

        // Normal player animations have a handgun equipped, but if a power up is enabled, all the animations are set to the more powerful rifle weapon.
        if (powerUp) {
            idleAnimation               = idleRifleAnimation;
            runningAnimation            = runningRifleAnimation;
            jumpingStartAnimation       = jumpingStartRifleAnimation;
            jumpingLoopAnimation        = jumpingLoopRifleAnimation;
            attackingAnimation          = attackingRifleAnimation;
            hurtAnimation               = hurtRifleAnimation;
            dyingAnimation              = dyingRifleAnimation;

            super.setDamage(35);

            // The rifle is faster and does more damage
//            if(getHasProjectile()) {
//                getProjectile().setMovementSpeedX(900f);
//
//            }
        }
        else {
            idleAnimation               = idleHandgunAnimation;
            runningAnimation            = runningHandgunAnimation;
            jumpingStartAnimation       = jumpingStartHandgunAnimation;
            jumpingLoopAnimation        = jumpingLoopHandgunAnimation;
            attackingAnimation          = attackingHandgunAnimation;
            hurtAnimation               = hurtHandgunAnimation;
            dyingAnimation              = dyingHandgunAnimation;

            super.setDamage(20);

//            if(getHasProjectile()) {
//                getProjectile().setMovementSpeedX(500f);
//
//            }
        }


        // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
        switch (super.getCharacterState()) {
            case IDLE:
                // Set the speed and animation for idle
                super.setCURRENT_MOVEMENT_SPEED(0);
                playDieSound = true;
                playHurtSound = true;
                super.loopingAnimation(idleAnimation);
                break;

            case MOVING:
                // Set the speed and animation for running
                super.setCURRENT_MOVEMENT_SPEED(runningSpeed);
                super.loopingAnimation(runningAnimation);
                break;

            case JUMPING:
                Gdx.app.log("mydebug", "playerjump");
                // Grounded is a guard so that the player cannot enter other states untill the jump has finished
                grounded = false;
                super.setCURRENT_MOVEMENT_SPEED(jumpingSpeed);
                if(!grounded) {
                    // Start jumping
                    jumpCharacter();
                    playJumpSound();
                    // Once the animation has finished and terminal velocity has been hit, the player can start falling
                    if (super.nonLoopingAnimation(jumpingStartAnimation)) {
                        if (getSprite().getY() > terminal_Velocity) {
                            super.setCharacterState(CharacterState.FALLING);
                        }
                    }
                }
                break;

            case FALLING:
                super.setCURRENT_MOVEMENT_SPEED(fallingSpeed);
                super.nonLoopingAnimation(jumpingLoopAnimation);
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
                if (super.nonLoopingAnimation(attackingAnimation)) {
                    super.setCharacterState(CharacterState.IDLE);
                }
                break;

            case HURT:
                super.setCURRENT_MOVEMENT_SPEED(0);
                playHurtSound();
                if (super.nonLoopingAnimation(hurtAnimation)) {
                    super.setCharacterState(CharacterState.IDLE);
                }
                break;

            case DYING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                playDieSound();
                if (super.nonLoopingAnimation(dyingAnimation)) {
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
        jumpSound.dispose();
        hurtSound.dispose();
        dieSound.dispose();
    }

    // =========================================== GETTERS AND SETTERS ========================================================

//    public PlayerState getPlayerState() { return playerState; }

//    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

    public boolean getPowerUp() { return powerUp; }

    public void setPowerUp(boolean powerUp) { this.powerUp = powerUp; }

//    public Projectile getPlayerProjectile() { return playerProjectile; }

    public boolean getIsGrounded() { return grounded;}

    public void setIsGrounded(boolean grounded) { this.grounded = grounded; }

    public float getPlayerLevel() { return playerLevel; }

    public void setPlayerLevel(float playerLevel) { this.playerLevel = playerLevel; }

    public ProjectileSpawner getProjectileSpawner() { return projectileSpawner; }
}
