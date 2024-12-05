package com.mygdx.game.Actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Character;
import java.util.ArrayList;



public class ProjectileSpawner extends Actor {

    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Projectile> removedProjectiles; // List collects inactive projectiles for deletion
    private final ArrayList<Particle> particles;
    private final ArrayList<Particle> removedParticles;
    private final String texturePath;
    private final String firingSoundPath;
    private final Vector2 size;
    private float projectileReloadSpeed;
    private float timePeriod                        = 0f;
    public float timer;
    private boolean startTimer                      = false;
    private boolean canSpawn                        = true;     // sets whether a projectile is allowed to be spawned
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxNumberOfProjectiles        = 5;
    private float movementSpeed                     = 0f;

    // ===================================================================================================================

    public ProjectileSpawner(String texturePath, String firingSoundPath, Vector2 size, Float projectileReloadSpeed) {

        this.texturePath            = texturePath;
        this.firingSoundPath        = firingSoundPath;
        this.size                   = size;
        this.projectileReloadSpeed  = projectileReloadSpeed;
        this.timer                  = projectileReloadSpeed;
        this.projectiles            = new ArrayList<>();
        this.removedProjectiles     = new ArrayList<>();
        this.particles              = new ArrayList<>();
        this.removedParticles       = new ArrayList<>();
    }

    // ===================================================================================================================

    public void spawnProjectile(com.mygdx.game.Actors.Characters.Character owner, com.mygdx.game.Actors.Characters.Character overlapCharacter) {
        Gdx.app.log("timer", "spawnProjectile");

        if(projectiles.size() < maxNumberOfProjectiles) {     // Limit the number of projectiles that can be active at once

            Projectile projectile = new Projectile(owner, overlapCharacter, texturePath, firingSoundPath,
                    new Vector2(owner.getSprite().getX(), owner.getSprite().getY()));

            projectile.getProjectileSprite().setSize(size.x, size.y);
            projectile.setMovementSpeedX(this.movementSpeed);
            projectile.setDirection(owner.getDirection());

            if(owner.getDirection() == Character.Direction.LEFT) {
                projectile.getOffset().set(owner.getProjectileOffset().get("leftOffset"));
            }
            else if(owner.getDirection() == Character.Direction.RIGHT) {
                projectile.getOffset().set(owner.getProjectileOffset().get("rightOffset"));
            }
            projectile.getProjectileSprite().setPosition(projectile.getProjectileStartWithOffset().x, projectile.getProjectileStartWithOffset().y);

            projectile.switchState(); // Lock in the start state
            projectile.setProjectileActive(true);
            projectile.setProjectileState(Projectile.ProjectileState.FIRING);
            projectiles.add(projectile);

            canSpawn = false;
        }
    }

    // ===================================================================================================================

    // Timer to control how quickly projectiles can spawn (the reload time)
    public void setTimer() {

        timePeriod += Gdx.graphics.getDeltaTime();

        if(timePeriod >= 0.2) {
            timePeriod = 0;
            timer -= 0.2f;

            if (timer <= 0) {
                timer       = projectileReloadSpeed;
                canSpawn    = true;
                startTimer  = false;
                Gdx.app.log("timer", "timerFinished");
            }
        }
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        removedProjectiles.clear();
        removedParticles.clear();

        // Draw active projectiles
        for(Projectile projectile : projectiles) {
            if(projectile != null) {
                if (projectile.getParticle().getActive()) {
                    particles.add(projectile.getParticle());
                }

                if (projectile.getProjectileActive()) {
                    projectile.draw(batch, alpha);
                }
                else {
                    removedProjectiles.add(projectile); // Mark inactive projectiles for deletion. Cannot remove during iteration without crash
                }
            }
        }

        for(Particle particle : particles) {

            if(particle != null) {
                if(particle.getActive()) {
                    particle.draw(batch, alpha);
                }
                else {
                    removedParticles.add(particle);
                }
            }
        }

        // Remove, delete and clear inactive projectiles
        for(Projectile removedProjectile: removedProjectiles) {
            projectiles.remove(removedProjectile);
            removedProjectile.dispose();
        }

        for(Particle removedParticle: removedParticles) {
            particles.remove(removedParticle);
            removedParticle.dispose();
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        for(Projectile projectile : projectiles) {
            projectile.act(delta);
        }
    }

    // ===================================================================================================================

    // Enemies arent included in the compensation. Only the player amount is ever entered as the cameraPositionAmount
    public void compensateCamera(float cameraPositionAmount) {
        for(Projectile projectile : projectiles) {
            if(projectile.getProjectileActive()) {
                projectile.compensateCamera(cameraPositionAmount);
                projectile.getParticle().compensateCamera(cameraPositionAmount);
            }
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "projectileSpawnerDispose");

        for(Projectile projectile : projectiles) {
            projectile.dispose();
        }
    }

    // ===================================================================================================================

    public boolean getCanSpawn() { return canSpawn; }

    public float getMovementSpeed() { return movementSpeed; }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
    public float getProjectileReloadSpeed() { return projectileReloadSpeed; }

    public void setProjectileReloadSpeed(float projectileReloadSpeed) {
        this.projectileReloadSpeed = projectileReloadSpeed;
        this.timer = projectileReloadSpeed;
    }

    public ArrayList<Projectile> getProjectiles() { return projectiles; }

    public ArrayList<Particle> getParticles() { return particles; }

    public boolean getStartTimer() { return startTimer; }

    public void setStartTimer(boolean startTimer) { this.startTimer = startTimer; }
}
