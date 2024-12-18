package com.mygdx.game.Actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;



public class Particle extends Actor {

    private Texture spriteSheet;
    private final TextureRegion[] explosionFrames;

    private Animation<TextureRegion> explosionAnimation;
    private final Vector2 position;
    @SuppressWarnings("FieldCanBeLocal")
    private TextureRegion currentFrame;
    private final Sprite sprite;

    private boolean active = false;
    private float statetime;


    // ===================================================================================================================

    public Particle() {

        explosionFrames = new TextureRegion[16];
        position        = new Vector2();
        sprite          = new Sprite();

//        setZIndex(this.getZIndex() + 1);
        create();
    }

    // ===================================================================================================================

    public void create() {

        spriteSheet = new Texture("explosion.png");

        explosionFrames[0]  = new TextureRegion(spriteSheet, 2, 2, 87, 87);
        explosionFrames[1]  = new TextureRegion(spriteSheet, 94, 2, 87, 87);
        explosionFrames[2]  = new TextureRegion(spriteSheet, 186, 2, 87, 87);
        explosionFrames[3]  = new TextureRegion(spriteSheet, 278, 2, 87, 87);
        explosionFrames[4]  = new TextureRegion(spriteSheet, 370, 2, 87, 87);
        explosionFrames[5]  = new TextureRegion(spriteSheet, 2, 94, 87, 87);
        explosionFrames[6]  = new TextureRegion(spriteSheet, 94, 94, 87, 87);
        explosionFrames[7]  = new TextureRegion(spriteSheet, 186, 94, 87, 87);
        explosionFrames[8]  = new TextureRegion(spriteSheet, 278, 94, 87, 87);
        explosionFrames[9]  = new TextureRegion(spriteSheet, 370, 94, 87, 87);

        explosionFrames[10] = new TextureRegion(spriteSheet, 2, 306, 45, 45);
        explosionFrames[11] = new TextureRegion(spriteSheet, 52, 306, 45, 45);
        explosionFrames[12] = new TextureRegion(spriteSheet, 102, 306, 45, 45);
        explosionFrames[13] = new TextureRegion(spriteSheet, 152, 306, 45, 45);
        explosionFrames[14] = new TextureRegion(spriteSheet, 202, 306, 45, 45);
        explosionFrames[15] = new TextureRegion(spriteSheet, 252, 306, 45, 45);

        explosionAnimation = new Animation<>(0.1f, explosionFrames);
    }

    // ===================================================================================================================

    public void spawnParticle() {
        statetime       = 0;
        active          = true;
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        if (active) {
            statetime += Gdx.graphics.getDeltaTime();
            currentFrame = explosionAnimation.getKeyFrame(statetime, false);
            batch.draw(currentFrame, sprite.getX(), sprite.getY());

            if (explosionAnimation.getKeyFrameIndex(statetime) == 15) {
                active = false;
            }
        }
    }

    // ===================================================================================================================

    public void compensateCamera(float cameraPositionAmount) {
        sprite.translate(cameraPositionAmount, 0);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "particle.dispose");

        spriteSheet.dispose();
    }

    // ===================================================================================================================

    public Sprite getSprite() { return sprite; }

    public Vector2 getPosition() { return position; }

    public boolean getActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
