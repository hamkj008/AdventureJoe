package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Screens.GameScreen;



/**
 * The parent class for all enemies. Inherits from the Character super class.
 */
public class Enemy extends Character {

    // ---- STATES -------------------------
    public enum MovingState { WALKING, RUNNING }
    public enum AttackState { MELEE, PROJECTILE }

    private MovingState movingState       = MovingState.WALKING;
    private AttackState attackState       = AttackState.MELEE;

    // Stats
    private String name;

    private int walkingSpeed              = 100;
    private int runningSpeed              = 200;
    private final int jumpingSpeed        = 200;
    private final int fallingSpeed        = 200;

    private boolean hasRunningState;

    // ===================================================================================================================

    public Enemy() {

        // All enemies start facing left.
        super.setDirection(Direction.LEFT);
    }

    // ===================================================================================================================

    // Checks to see if the enemy is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    public void healthCheck(int damage) {

        if((getHealth() - damage) > 0) {
            super.setCharacterState(CharacterState.HURT);
            super.setHealth(getHealth() - damage);
        }
        else {
            super.setCharacterState(CharacterState.DYING);
            super.setHealth(0);
        }
    }

    // ===================================================================================================================

    // Sets the conditions to enact if an enemy has been killed by the player.
    public void killEnemy() {

        super.setCharacterState(CharacterState.DEAD);
        super.setIsAlive(false);
    }

    // ===================================================================================================================

    // A default set of states for every enemy. Responsible for setting basic movement and applying basic animations
    public void switchStates(Animation<TextureRegion> idleAnimation, Animation<TextureRegion> walkingAnimation,
                             Animation<TextureRegion> hurtAnimation, Animation<TextureRegion> dyingAnimation) {

        switch (super.getCharacterState()) {

            case IDLE:
                super.setCURRENT_MOVEMENT_SPEED(0);
                super.loopingAnimation(idleAnimation);
                break;

            case MOVING:
                if(movingState == MovingState.WALKING) {
                    super.setCURRENT_MOVEMENT_SPEED(walkingSpeed);
                    super.moveCharacter();
                    super.loopingAnimation(walkingAnimation);
                }
                break;

            case HURT:
                super.setCURRENT_MOVEMENT_SPEED(0);

                if (super.nonLoopingAnimation(hurtAnimation)) {
                    super.setCharacterState(CharacterState.MOVING);
                }
                break;

            case DYING:
                super.setCURRENT_MOVEMENT_SPEED(0);

                if (super.nonLoopingAnimation(dyingAnimation)) {
                    killEnemy();
                }
                break;
        }
    }

    // ===================================================================================================================

    // For enemies with melee attacks, this method is only triggered when the enemy is in an attacking state.
    public void checkDamage() {

        // If the enemy has overlapped the players bounding box while attacking, it has attacked the player.
        if(getSprite().getBoundingRectangle().overlaps(GameScreen.getInstance().getGameStateController().getPlayer().getSprite().getBoundingRectangle())) {
            if (GameScreen.getInstance().getGameStateController().getPlayer().getIsAlive()) {
                GameScreen.getInstance().getGameStateController().getPlayer().healthCheck(getDamage());
            }
        }
    }

    // ===================================================================================================================

    /**
     * A set of default AI states that all enemies will share for some basic behaviour.
     * AI states specific to each enemy can be added in the enemies respective classes,
     * by overriding completely, or in conjunction with a call to super.
     **/
    public void setAIStates(Player player) {

        if (player.getIsAlive()) {

            if (getCharacterState() != CharacterState.HURT &&
                getCharacterState() != CharacterState.DYING &&
                getCharacterState() != CharacterState.ATTACKING){

                if(this.getSprite().getX() > Gdx.graphics.getWidth() || this.getSprite().getX() < 0) {
                    setCharacterState(CharacterState.IDLE);
                }
                else {
                    setCharacterState(CharacterState.MOVING);
                }
            }
//                if(this.getSprite().getX() < (Gdx.graphics.getWidth() - 300)
//                        && this.getSprite().getX() > 20
//                        && getCharacterState() == CharacterState.IDLE
//                        && !(getCharacterState() == CharacterState.HURT ||
//                            getCharacterState() != CharacterState.DYING ||
//                            getCharacterState() != CharacterState.ATTACKING)) {
//                    setCharacterState(CharacterState.MOVING);
//                }

            // Enemy sees the player
            if (hasRunningState && distanceFromPlayer(player) < 900) {
                movingState = MovingState.RUNNING;
            } else {
                movingState = MovingState.WALKING;
            }


            // The enemy will turn around if the player goes past it.
            // It will continue to travel for a short distance before it does so, to avoid the facing both directions at the same time bug.
            if ((GameScreen.getInstance().getHelper().getCenteredSpritePosition(player.getSprite()).x - 200) >
                    GameScreen.getInstance().getHelper().getCenteredSpritePosition(getSprite()).x && distanceFromPlayer(player) < 1000) {
                setDirection(Direction.RIGHT);
            }
            else if ((GameScreen.getInstance().getHelper().getCenteredSpritePosition(player.getSprite()).x + 200) <
                    GameScreen.getInstance().getHelper().getCenteredSpritePosition(getSprite()).x && distanceFromPlayer(player) < 1000) {
                setDirection(Direction.LEFT);
            }
        }
    }

    // ======================================= GETTERS AND SETTERS ==========================================================

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public MovingState getMovingState() { return movingState; }

    public void setMovingState(MovingState movingState) { this.movingState = movingState; }

    public AttackState getAttackState() { return attackState; }

    public void setAttackState(AttackState attackState) { this.attackState = attackState; }

    public int getWalkingSpeed() { return walkingSpeed; }

    public void setWalkingSpeed(int walkingSpeed) { this.walkingSpeed = walkingSpeed; }

    public int getRunningSpeed() { return runningSpeed; }

    public void setRunningSpeed(int runningSpeed) { this.runningSpeed = runningSpeed; }

    public void setHasRunningState(boolean hasRunningState) { this.hasRunningState = hasRunningState; }
}
