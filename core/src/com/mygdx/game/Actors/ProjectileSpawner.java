package com.mygdx.game.Actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Character;

import java.util.ArrayList;


public class ProjectileSpawner extends Actor {

    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Projectile> removedProjectiles;         // List collects inactive projectiles for deletion
    private final String texturePath;
    private final String firingSoundPath;
    private final Vector2 size;
    private final Vector2 offset;
    private float timePeriod = 0f;
    private float projectileDuration = 0f;
    private float timer = 0f;
    public boolean canSpawn = true;     // sets whether a projectile is allowed to be spawned

    // ===================================================================================================================

    public ProjectileSpawner(String texturePath, String firingSoundPath, Vector2 size, Vector2 offset, float projectileDuration) {

        this.texturePath        = texturePath;
        this.firingSoundPath    = firingSoundPath;
        this.size               = size;
        this.offset             = offset;
        this.projectileDuration = projectileDuration;
        this.timer              = projectileDuration;
        this.projectiles        = new ArrayList<>();
        this.removedProjectiles = new ArrayList<>();
    }

    // ===================================================================================================================

    public void spawnProjectile(com.mygdx.game.Actors.Characters.Character owner, com.mygdx.game.Actors.Characters.Character overlapCharacter, float movementSpeed, Character.Direction direction) {

//        Gdx.app.log("debug", "projectile size" + projectiles.size());
        if(projectiles.size() < 10) {     // Limit the number of projectiles that can be active at once

            Projectile projectile = new Projectile(owner, overlapCharacter, texturePath, firingSoundPath,
                    new Vector2(owner.getSprite().getX(), owner.getSprite().getY()), offset);

            projectile.getProjectileSprite().setSize(size.x, size.y);
            projectile.setMovementSpeedX(movementSpeed);
            projectile.getProjectileSprite().setPosition(projectile.getProjectileStartWithOffset().x, projectile.getProjectileStartWithOffset().y);
            projectile.setDirection(direction);

            projectile.switchState(); // Lock in the start state

            projectile.setProjectileActive(true);
            projectile.setProjectileState(Projectile.ProjectileState.FIRING);
            projectiles.add(projectile);

            canSpawn = false;
        }
    }

    // ===================================================================================================================

    public void timerElapsed() {

        timePeriod += Gdx.graphics.getDeltaTime();

        if(timePeriod > 1) {
            timePeriod = 0;
            timer -= 1;

            if (timer == 0) {
                timer = projectileDuration;
                canSpawn = true;
            }
        }
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        // Draw active projectiles
        for(Projectile projectile : projectiles) {
            if (projectile.getProjectileActive()) {
                projectile.draw(batch, alpha);
            } else {
                removedProjectiles.add(projectile); // Mark inactive projectiles for deletion. Cannot remove during iteration without crash
            }
        }
        // Remove, delete and clear inactive projectiles
        projectiles.removeAll(removedProjectiles);
        for(Projectile removedProjectile: removedProjectiles) {
            removedProjectile.dispose();
        }
        removedProjectiles.clear();
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        for(Projectile projectile : projectiles) {
            projectile.act(delta);
        }
    }

    // ===================================================================================================================

    public void compensateCamera(float cameraPositionAmount) {
        for(Projectile projectile : projectiles) {
            projectile.getProjectileSprite().translate(cameraPositionAmount, 0);
        }
    }

    public Vector2 getOffset() { return offset; }


}