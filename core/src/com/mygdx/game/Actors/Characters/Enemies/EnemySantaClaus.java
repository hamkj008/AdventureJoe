package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Actors.ProjectileSpawner;
import com.mygdx.game.Screens.GameScreen;



/**
 * Santa has a projectile attack and a melee attack
 */
public class EnemySantaClaus extends Enemy {

    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> runningAnimation;
    private final Animation<TextureRegion> meleeAnimation;
    private final Animation<TextureRegion> throwingAnimation;
    private final Animation<TextureRegion> hurtAnimation;
    private final Animation<TextureRegion> dyingAnimation;

    @SuppressWarnings("FieldCanBeLocal")
    private final float projectileMovementSpeed = 400f;
    @SuppressWarnings("FieldCanBeLocal")
    private final float projectileReloadSpeed   = 2f;


    // ===================================================================================================================

    /**
     * The Yeti has a running state and also has both a melee attack and a projectile attack.
     **/
    public EnemySantaClaus() {

        super.setName("Santa");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

        super.setHasRunningState(true);
        super.setAttackState(AttackState.PROJECTILE);

        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Idle.png", 4, 3, 12, 1);
        walkingAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Walking.png", 5, 4, 18, 0.8f);
        runningAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Running.png", 3, 5, 15, 0.8f);
        meleeAnimation      = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Santa Claus/Attacking.png", 4, 3, 12, 0.6f);
        throwingAnimation   = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Throwing.png", 4, 3, 12, 0.5f);
        hurtAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Hurt.png", 4, 3, 12, 0.8f);
        dyingAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Santa Claus/Dying.png", 4, 3, 12, 0.8f);

        super.getProjectileOffset().put("leftOffset", new Vector2(-10, 100));
        super.getProjectileOffset().put("rightOffset", new Vector2(200, 100));
        super.setProjectileSpawner(new ProjectileSpawner(this, "Game Objects/Cartoon Yeti_Snow Ball.png", "Audio/Sounds/shot.mp3",
                new Vector2(70, 50), projectileReloadSpeed));
        super.getProjectileSpawner().setMovementSpeed(projectileMovementSpeed);
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        super.act(delta);               // Only enemies with projectiles have to call super
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

            if (super.getAttackState() == AttackState.MELEE) {
                // If the animation has finished
                if (super.nonLoopingAnimation(meleeAnimation)) {
                    checkDamage();
                    // Set the state to enter into after animation has played.
                    super.setCharacterState(CharacterState.MOVING);
                }
            }
            else if (super.getAttackState() == AttackState.PROJECTILE) {
                // If the animation has finished
                if (super.nonLoopingAnimation(throwingAnimation)) {
                    // Set the state to enter into after animation has played.
                    super.setCharacterState(CharacterState.MOVING);
                }
            }
        }
    }

    // ===================================================================================================================

    // Adds additional AI states specific to this enemy, primarily its Attack state
    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        // The yeti has a projectile attack and a melee attack. If it is far enough away from the player it uses the projectile attack.
        if ( distanceFromPlayer(player) < 1000 && distanceFromPlayer(player) > 700) {
            if(super.getProjectileSpawner().getCanSpawn()) {
                setAttackState(AttackState.PROJECTILE);
                super.setCharacterState(CharacterState.ATTACKING);
                super.getProjectileSpawner().setStartTimer(true);
                super.getProjectileSpawner().setProjectileAttack(true);
            }
        }
        else {
            super.getProjectileSpawner().setProjectileAttack(false);

            // If the enemy is close enough to melee attack.
            if (distanceFromPlayer(player) < 200) {
                super.setAttackState(AttackState.MELEE);
                super.setCharacterState(CharacterState.ATTACKING);
            }
        }
    }

    public void dispose() {
        super.dispose();
        idleAnimation.getKeyFrames()[0].getTexture().dispose();
        walkingAnimation.getKeyFrames()[0].getTexture().dispose();
        runningAnimation.getKeyFrames()[0].getTexture().dispose();
        meleeAnimation.getKeyFrames()[0].getTexture().dispose();
        throwingAnimation.getKeyFrames()[0].getTexture().dispose();
        hurtAnimation.getKeyFrames()[0].getTexture().dispose();
        dyingAnimation.getKeyFrames()[0].getTexture().dispose();
    }
}
