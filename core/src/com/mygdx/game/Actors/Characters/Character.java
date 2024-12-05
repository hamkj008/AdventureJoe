package com.mygdx.game.Actors.Characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Actors.ProjectileSpawner;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Screens.GameScreen;
import java.util.HashMap;
import java.util.Map;



/**
 * The parent superclass for all characters.
 * Both Player and Enemies inherit from this class and it provides the basic template that both of them require.
 */
public class Character extends Actor {

    public enum Direction { LEFT, RIGHT }

    public enum CharacterState { IDLE, MOVING, JUMPING, FALLING, ATTACKING, HURT, DYING, DEAD }
    private CharacterState characterState = CharacterState.IDLE;

    // ---- CHARACTER STATS -------------------------
    private boolean isAlive                     = true;
    private Direction direction                 = Direction.LEFT;

    private final int Max_Health                = 100;
    private int health                          = Max_Health;
    private int damage                          = 20;
    private int CURRENT_MOVEMENT_SPEED;

    // ---- SPRITES -------------------------
    private final Sprite sprite;
    private final Vector2 startPosition;
    private final Vector2 positionAmount;   // The amount that a sprite will be translated by to reach its new position

    // ---- ANIMATION -------------------------
    private TextureRegion currentFrame;

    private float loopingStateTime;
    private float nonLoopingStateTime;

    private ProjectileSpawner projectileSpawner = null;
    private final Map<String, Vector2> projectileOffset;


    // ===================================================================================================================

    public Character() {

        sprite                  = new Sprite();
        sprite.setSize(250, 250);
        currentFrame            = new TextureRegion();
        startPosition           = new Vector2();
        positionAmount          = new Vector2();
        startPosition.y         = LevelFactory.getCurrentGroundLevel();     // Characters cannot be created before the level
        projectileOffset        = new HashMap<>();
        projectileOffset.put("leftOffset", new Vector2());
        projectileOffset.put("rightOffset", new Vector2());
    }

    // ===================================================================================================================

    public void healthCheck(int damage) {

        if((health - damage) > 0) {
            characterState = CharacterState.HURT;
            health = health - damage;
        }
        else {
            characterState = CharacterState.DYING;
            health = 0;
        }
    }

    // ===================================================================================================================

    public void spawnProjectile() {}

    // ===================================================================================================================

    /**
    * The default drawing method. It flips the sprites to face the correct direction.
    * Child classes that may need to override this will still have a call to super to access this method.
     **/
    @Override
    public void draw(Batch batch, float alpha) {

        GameScreen.getInstance().getHelper().flipSprite(direction, currentFrame);

        if(currentFrame != null && sprite != null) {
            batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        }
        else {
            Gdx.app.log("error", "Error: a character is null in draw");
        }

        if(projectileSpawner != null) {
            projectileSpawner.draw(batch, alpha);
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        Gdx.app.log("timer", "" + projectileSpawner.getStartTimer());
        if(projectileSpawner != null) {

            if (characterState == Character.CharacterState.ATTACKING && projectileSpawner.getCanSpawn()) {
                spawnProjectile();
                Gdx.app.log("newtimer", "spawnProjectile");
            }

            if(projectileSpawner.getStartTimer()) {
                projectileSpawner.setTimer();
            }
            projectileSpawner.act(delta);
        }
    }

    // ===================================================================================================================

    /**
     * Processes animations for both looping and non looping versions. Non looping resets the state time so that it only plays once.
     * Applies separate state time to each version so that they don't interfere with each other when the non looping resets.
     **/
    public boolean nonLoopingAnimation(Animation<TextureRegion> animation) {

        nonLoopingStateTime += Gdx.graphics.getDeltaTime();

        if (animation.isAnimationFinished(nonLoopingStateTime)) {
            nonLoopingStateTime = 0;
            return true;
        }
        else {
            currentFrame = animation.getKeyFrame(nonLoopingStateTime, false);
            return false;
        }
    }

    // ===================================================================================================================

    public void loopingAnimation(Animation<TextureRegion> animation) {

        loopingStateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(loopingStateTime, true);
    }

    // ===================================================================================================================

    // Finds out how far away the player is from the enemy sprite.
    public float distanceFromPlayer(Player player) {
        return GameScreen.getInstance().getHelper().getCenteredSpritePosition(this.getSprite()).dst(GameScreen.getInstance().getHelper().getCenteredSpritePosition(player.getSprite()));
    }

    // ===================================================================================================================

    /** A characters movement speed * delta time gives a position amount.
    * This is the amount of distance that the sprite has to travel to to reach the new position.
    * The sprites are then translated to the new position given by the position amount
    **/
    public void moveCharacter() {

        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        if(direction == Direction.LEFT) {
            sprite.translate(-positionAmount.x, positionAmount.y);
        }
        else {
            sprite.translate(positionAmount.x, positionAmount.y);
        }
    }

    // ===================================================================================================================

    /**
     * Moves the characters in the opposite direction to oppose the cameras movement,
     * giving the impression that they are not moving if they are static objects.
     * Characters that are moving also do not have the cameras movement added to their own.
     * All "compensate" methods do this.
     **/
    public void compensateCamera(float cameraPositionAmount) {

        sprite.translate(cameraPositionAmount, positionAmount.y);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "playerDispose");

        if(projectileSpawner != null) {
            projectileSpawner.dispose();
        }
    }

    // ================================== GETTERS AND SETTERS ====================================================================

    public boolean getIsAlive() { return isAlive; }

    public void setIsAlive(boolean alive) { isAlive = alive; }

    public CharacterState getCharacterState() { return characterState; }

    public void setCharacterState(CharacterState characterState) { this.characterState = characterState; }

    public Direction getDirection() { return direction; }

    public void setDirection(Direction direction) { this.direction = direction; }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getMax_Health() { return Max_Health; }

    public int getCURRENT_MOVEMENT_SPEED() { return CURRENT_MOVEMENT_SPEED; }

    public void setCURRENT_MOVEMENT_SPEED(int CURRENT_MOVEMENT_SPEED) { this.CURRENT_MOVEMENT_SPEED = CURRENT_MOVEMENT_SPEED; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public Sprite getSprite() { return sprite; }

    public Vector2 getStartPosition() { return startPosition; }

    public Vector2 getPositionAmount() { return positionAmount; }

    public ProjectileSpawner getProjectileSpawner() { return projectileSpawner; }

    public void setProjectileSpawner(ProjectileSpawner projectileSpawner) { this.projectileSpawner = projectileSpawner; }

    public Map<String, Vector2> getProjectileOffset() { return projectileOffset; }
}
