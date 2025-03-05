package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Screens.GameScreen;



/**
 * Crab has a melee attack
 */
public class EnemyCrab extends Enemy {

    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> runningAnimation;
    private final Animation<TextureRegion> attackingAnimation;
    private final Animation<TextureRegion> hurtAnimation;
    private final Animation<TextureRegion> dyingAnimation;


    // ===================================================================================================================

    public EnemyCrab() {

        super.setName("Crab");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100;
        super.getSprite().setX(getStartPosition().x);

        super.setAttackState(AttackState.MELEE);
        super.setHasRunningState(true);
        super.setRunningSpeed(300);

        // ---- ANIMATIONS -------------------------
        idleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Crab/Idle.png", 3, 4, 12, 0.8f);
        walkingAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Crab/Walking.png", 3, 6, 18, 0.8f);
        runningAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Crab/Walking.png", 3, 6, 18, 0.4f);
        attackingAnimation  = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Crab/Attacking.png", 3, 4, 12, 0.6f);
        hurtAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Crab/Hurt.png", 3, 4, 12, 0.6f);
        dyingAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Crab/Hurt.png", 3, 4, 12, 0.6f);
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

    // ===================================================================================================================

    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        if (distanceFromPlayer(player) < 200) {
            super.setCharacterState(CharacterState.ATTACKING);
        }
    }


    public void dispose() {
        super.dispose();
        idleAnimation.getKeyFrames()[0].getTexture().dispose();
        walkingAnimation.getKeyFrames()[0].getTexture().dispose();
        runningAnimation.getKeyFrames()[0].getTexture().dispose();
        attackingAnimation.getKeyFrames()[0].getTexture().dispose();
        hurtAnimation.getKeyFrames()[0].getTexture().dispose();
        dyingAnimation.getKeyFrames()[0].getTexture().dispose();
    }
}
