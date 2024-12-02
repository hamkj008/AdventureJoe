package com.mygdx.game.Actors.Characters.Enemies;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Screens.GameScreen;


/**
 * Dragon has a projectile attack
 */
public class EnemyDragon extends Enemy {


//    private Projectile dragonProjectile;


    // ---- ANIMATIONS -------------------------
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkingAnimation;
    private final Animation<TextureRegion> attackingAnimation;
    private final Animation<TextureRegion> hurtAnimation;
    private final Animation<TextureRegion> dyingAnimation;

//    private final ProjectileSpawner projectileSpawner;
    private final float projectileMovementSpeed = 400f;


    public EnemyDragon() {
        super.setName("Dragon");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

//        super.setHasProjectile(true);
        super.setAttackState(AttackState.PROJECTILE);


        // ---- PROJECTILE -------------------------
        // Initialize Projectile
//        super.setProjectile(new Projectile(this, GameScreen.getInstance().getGameStateController().getRandomEnemy(),
//                "Game Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3"));
//
//        dragonProjectile.getProjectileSprite().setSize(70f, 50f);
//        dragonProjectile.setMovementSpeedX(400f);


        // ---- ANIMATIONS -------------------------
        idleAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Idle.png", 3, 6, 18);
        walkingAnimation    = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Walking.png", 3, 6, 9);
        attackingAnimation  = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Cartoon Dragon 01/Attacking.png", 3, 6, 18);
        hurtAnimation       = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Hurt.png", 3, 6, 18);
        dyingAnimation      = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Dying.png", 3, 3, 9);

//        projectileSpawner = new ProjectileSpawner("Game Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3",
//                new Vector2(70, 50), new Vector2(100, 100), 10);
    }

    // ===================================================================================================================

    public void spawnProjectile() {

//        projectileSpawner.spawnProjectile(this, GameScreen.getInstance().getGameStateController().getPlayer(),
//                projectileMovementSpeed, getDirection());
    }

    // ===================================================================================================================


    // Has projectile so overrides draw method inherited from Enemy super class.
    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

//        if(super.getProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
//            // Set the projectile to be launched in the same direction the character is facing. Reverses the offset and speed accordingly.
//            if (super.getDirection() == Direction.LEFT) {
//                super.getProjectile().getOffset().set(0, 100);
//                super.getProjectile().setDirection(Direction.LEFT);
//            }
//            if (super.getDirection() == Direction.RIGHT) {
//                super.getProjectile().getOffset().set(200, 100);
//                super.getProjectile().setDirection(Direction.RIGHT);
//            }
//        }
//
//        // Draw the projectile if the enemy is attacking
//        if(super.getProjectile().getProjectileState() == Projectile.ProjectileState.FIRING) {
//            super.getProjectile().draw(batch, alpha);
//        }
    }


    @Override
    public void act(float delta) {

//        if(getCharacterState() == CharacterState.ATTACKING && projectileSpawner.timerElapsed()) {
//            spawnProjectile();
//        }
//        projectileSpawner.act(delta);

        switchCustomStates();

        // Updates the projectile to emit from wherever the character is.
//        dragonProjectile.getProjectileStartPosition().x = getSprite().getX();
//        dragonProjectile.getProjectileStartPosition().y = getSprite().getY();
//        super.getProjectile().act(delta);
    }


    /*
     First handles default states with a call to super.switchStates in Enemy class.
     Then custom states are specified. Not all enemies have these states, they are specific to the enemy.
     */
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
        if (distanceFromPlayer(player) < 1000 && distanceFromPlayer(player) > 500) {
            super.setCharacterState(CharacterState.ATTACKING);
//            if (super.getProjectile().getProjectileState() == Projectile.ProjectileState.START) {
//
//            }
        }

//        if(dragonProjectile.getProjectileSprite().getBoundingRectangle().overlaps(GameScreen.getInstance().getGameStateController().getPlayer().getSprite().getBoundingRectangle())) {
//            if(dragonProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
//                if(GameScreen.getInstance().getGameStateController().getPlayer().getIsAlive()) {
//                    // If the projectile hits the player it does damage
//                    GameScreen.getInstance().getGameStateController().getPlayer().healthCheck(getDamage());
//                    dragonProjectile.setProjectileState(Projectile.ProjectileState.RESET);
//                    dragonProjectile.switchState();
//
//                }
//            }
//        }
    }

    // ===================================================================================================================

//    @Override
//    public Projectile getEnemyProjectile() {
//        return dragonProjectile;
//    }

}
