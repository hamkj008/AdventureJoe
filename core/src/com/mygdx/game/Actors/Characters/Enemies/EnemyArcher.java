package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Actors.ProjectileSpawner;
import com.mygdx.game.Screens.GameScreen;



/**
 * Archer has a projectile attack
 */
public class EnemyArcher extends Enemy {

    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> attackingAnimation;
    private final Animation<TextureRegion> hurtAnimation;
    private final Animation<TextureRegion> dyingAnimation;


    @SuppressWarnings("FieldCanBeLocal")
    private final float projectileMovementSpeed = 400f;
    @SuppressWarnings("FieldCanBeLocal")
    private final float projectileReloadSpeed   = 2f;


    // ===================================================================================================================

    public EnemyArcher() {
        super.setName("Archer");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

        super.setAttackState(AttackState.PROJECTILE);

        // ---- ANIMATIONS -------------------------
        idleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Archer 02/Idle.png", 4, 6, 24, 0.8f);
        walkingAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Archer 02/Walking.png", 8, 3, 24, 0.8f);
        attackingAnimation  = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Archer 02/Attacking.png", 5, 2, 10, 0.8f);
        hurtAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Archer 02/Hurt.png", 6, 2, 12, 0.8f);
        dyingAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Archer 02/Dying.png", 6, 2, 12, 0.8f);

        super.getProjectileOffset().put("leftOffset", new Vector2(-10, 90));
        super.getProjectileOffset().put("rightOffset", new Vector2(200, 90));
        super.setProjectileSpawner(new ProjectileSpawner(this, "Game Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3",
                new Vector2(70, 50), projectileReloadSpeed));
        super.getProjectileSpawner().setMovementSpeed(projectileMovementSpeed);

    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        super.act(delta);           // Only enemies with projectiles have to call super
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
        if(super.getCharacterState() == CharacterState.ATTACKING) {
            super.setCURRENT_MOVEMENT_SPEED(0);
            if (super.nonLoopingAnimation(attackingAnimation)) {
                super.setCharacterState(CharacterState.MOVING);
            }
        }
    }

    // ===================================================================================================================

    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        // The dragon fires at the enemy wherever it is
        if (distanceFromPlayer(player) < 1000 && distanceFromPlayer(player) > 500 && super.getProjectileSpawner().getCanSpawn()) {
            super.setCharacterState(CharacterState.ATTACKING);
            super.getProjectileSpawner().setStartTimer(true);
        }
    }
}
