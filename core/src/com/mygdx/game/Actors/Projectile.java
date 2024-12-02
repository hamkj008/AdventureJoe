package com.mygdx.game.Actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Screens.GameScreen;


/**
 * A class for projectiles that need to be fired around by player and enemies. It manages the states, sounds and behaviour of projectiles.
 * Each instance of a projectile is owned by the character that fires it.
 */
public class Projectile extends Actor {

    public enum ProjectileState { START, FIRING }

    private ProjectileState projectileState = ProjectileState.START;
    private com.mygdx.game.Actors.Characters.Character.Direction direction;

    private final TextureRegion textureRegion;    // Used for flipping the sprite
    private final Sprite projectileSprite;
    private final com.mygdx.game.Actors.Characters.Character owner;
    private final com.mygdx.game.Actors.Characters.Character overlapCharacter;
    private float movementSpeedX = 0;
    private float movementSpeedY = 0;
    private final Vector2 projectileStartPosition;
    private final Vector2 offset;
    private final Vector2 projectileStartWithOffset;
    private final Vector2 PROJECTILE_MOVEMENT;
    private boolean projectileActive = false;

    private final Sound firingSound;
    private boolean playFiringSound = true;


    // ===================================================================================================================

    public Projectile(com.mygdx.game.Actors.Characters.Character owner, com.mygdx.game.Actors.Characters.Character overlapCharacter, String texturePath, String firingSoundPath, Vector2 projectileStartPosition, Vector2 offset) {

        this.owner                      = owner;
        this.overlapCharacter           = overlapCharacter;
        textureRegion                   = new TextureRegion(new Texture(texturePath));
        projectileSprite                = new Sprite(textureRegion);
        this.projectileStartPosition    = projectileStartPosition;
        this.offset                     = offset;
        projectileStartWithOffset       = new Vector2();
        PROJECTILE_MOVEMENT             = new Vector2();
        firingSound                     = Gdx.audio.newSound(Gdx.files.internal(firingSoundPath));
    }

    // ===================================================================================================================

    /*
    The default drawing method. It flips the sprites to face the correct direction.
     */
    @Override
    public void draw(Batch batch, float alpha) {
//        Gdx.app.log("debug", "projectile/draw");

        if (projectileState == ProjectileState.FIRING) {
            if (direction == com.mygdx.game.Actors.Characters.Character.Direction.LEFT) {
                if (!textureRegion.isFlipX()) {
                    textureRegion.flip(true, false);
                }
            }
            if (direction == com.mygdx.game.Actors.Characters.Character.Direction.RIGHT) {
                if (textureRegion.isFlipX()) {
                    textureRegion.flip(true,false);
                }
            }
            batch.draw(textureRegion, projectileSprite.getX(), projectileSprite.getY(),
                    projectileSprite.getWidth(), projectileSprite.getHeight());
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        setProjectileBounds();   // Monitor out of bounds
        switchState();          // Monitor state switch
    }

    // ===================================================================================================================

    public void switchState() {

        switch (projectileState) {
            case START:
                playFiringSound = true;
                break;

            case FIRING:
                moveProjectile();
                playFiringSound();
                break;
        }
    }

    // ===================================================================================================================

    public void playFiringSound() {
        if(playFiringSound) {
            firingSound.play();
            playFiringSound = false;
        }
    }

    // ===================================================================================================================

    // If the projectile goes off screen or hits a character it is inactive.
    public void setProjectileBounds() {
        if(getProjectileSprite().getX() > Gdx.graphics.getWidth()) {
            projectileActive = false;
        }
        if(getProjectileSprite().getX() < 0) {
            projectileActive = false;
        }

        if(this.projectileSprite.getBoundingRectangle().overlaps(overlapCharacter.getSprite().getBoundingRectangle())) {
            if(this.projectileState == Projectile.ProjectileState.FIRING && overlapCharacter.getIsAlive()) {
                overlapCharacter.healthCheck(owner.getDamage());
                projectileActive = false;
            }
        }
    }

    // ===================================================================================================================

    /*
     Takes the movement speeds and direction, uses Game Helper to apply deltaTime, then finds the new position for the sprite to move to
     and translates to the new position.
     */
    public void moveProjectile() {
        if(direction == com.mygdx.game.Actors.Characters.Character.Direction.LEFT) {
            PROJECTILE_MOVEMENT.x = GameScreen.getInstance().getHelper().setMovement(-movementSpeedX);
        }
        else {
            PROJECTILE_MOVEMENT.x = GameScreen.getInstance().getHelper().setMovement(movementSpeedX);
        }
        PROJECTILE_MOVEMENT.y = GameScreen.getInstance().getHelper().setMovement(movementSpeedY);
        projectileSprite.translate(PROJECTILE_MOVEMENT.x, PROJECTILE_MOVEMENT.y);
    }

    // ===================================================================================================================

    /*
     * Moves the projectiles in the opposite direction to oppose the cameras movement,
     * so that they do not have the cameras movement added to their own.
     * All "compensate" methods do this.
     */
    public void compensateCamera(float cameraPositionAmount) {

        projectileSprite.translate(cameraPositionAmount, 0);
    }

    // ===================================================================================================================

    public void dispose() {
        firingSound.dispose();
        textureRegion.getTexture().dispose();
    }

    // ================================ GETTERS AND SETTERS ===========================================================================

    public ProjectileState getProjectileState() { return projectileState; }

    public void setProjectileState(ProjectileState projectileState) { this.projectileState = projectileState; }

    public Sprite getProjectileSprite() { return projectileSprite; }

    public float getMovementSpeedX() { return movementSpeedX; }

    public void setMovementSpeedX(float movementSpeedX) { this.movementSpeedX = movementSpeedX; }

    public Vector2 getProjectileStartPosition() { return projectileStartPosition; }

    public Vector2 getProjectileStartWithOffset() {
        projectileStartWithOffset.x = projectileStartPosition.x + offset.x;
        projectileStartWithOffset.y = projectileStartPosition.y + offset.y;
        return projectileStartWithOffset;
    }

    public Vector2 getOffset() { return offset; }

    public com.mygdx.game.Actors.Characters.Character.Direction getDirection() { return direction; }

    public void setDirection(Character.Direction direction) { this.direction = direction; }

    public boolean getProjectileActive() { return projectileActive; }

    public void setProjectileActive(boolean projectileActive) {
        this.projectileActive = projectileActive;
    }
}
