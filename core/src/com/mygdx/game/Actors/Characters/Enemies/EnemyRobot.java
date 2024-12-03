package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Screens.GameScreen;


/**
 * Robot has a melee attack
 */
public class EnemyRobot extends Enemy {

    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> runningAnimation;
    private final Animation<TextureRegion> attackingAnimation;
    private final Animation<TextureRegion> hurtAnimation;
    private final Animation<TextureRegion> dyingAnimation;


    // ===================================================================================================================

    public EnemyRobot() {

        super.setName("Robot");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

        super.setAttackState(AttackState.MELEE);
        super.setHasRunningState(true);

        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Idle.png", 6, 3, 18);
        walkingAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Walking.png", 6, 4, 24);
        runningAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Running.png", 3, 6, 18);
        attackingAnimation  = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Attacking.png", 4, 3, 12);
        hurtAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Hurt.png", 3, 4, 12);
        dyingAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Dying.png", 3, 4, 12);
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {
        switchCustomStates();
    }

    // ===================================================================================================================

    /**
     * First handles default states with a call to super.switchStates in Enemy class.
     * Then custom states are specified. Not all enemies have these states, they are specific to the enemy.
     **/
    public void switchCustomStates() {

        // Switch states in Enemy class has a set of default behaviours for standard animations.
        super.switchStates(idleAnimation, walkingAnimation, hurtAnimation, dyingAnimation);

        // --- Custom states ------
        if(super.getCharacterState() == CharacterState.MOVING) {
            if (super.getMovingState() == MovingState.RUNNING) {
                super.setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                super.moveCharacter();
                super.loopingAnimation(runningAnimation);
            }
        }

        if(super.getCharacterState() == CharacterState.ATTACKING) {
            super.setCURRENT_MOVEMENT_SPEED(0);
            if (super.nonLoopingAnimation(attackingAnimation)) {
                checkDamage();
                super.setCharacterState(CharacterState.MOVING);
            }
        }
    }
}
