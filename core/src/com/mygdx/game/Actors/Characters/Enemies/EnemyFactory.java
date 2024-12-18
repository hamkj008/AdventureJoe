package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.UI.UICounters;
import java.util.ArrayList;
import java.util.Objects;



public class EnemyFactory extends Actor {

    ArrayList<Enemy> enemyArray;
    ArrayList<Enemy> removedEnemies;

    private final String[] enemyNameList = {
            "Dragon",
            "Devil Guy",
            "Yeti",
            "Robot",
            "Wolf",
            "Archer",
            "Black Wizard",
            "Santa",
            "Crab",
            "Ninja"
    };

    // ===================================================================================================================

    public EnemyFactory() {}

    // ===================================================================================================================


    public void spawnEnemies(int levelXBoundary, int numberOfEnemies, int minPositionDistance) {

        enemyArray                      = new ArrayList<>(numberOfEnemies);
        removedEnemies                  = new ArrayList<>();
        ArrayList<Vector2> positions    = GameScreen.getInstance().getHelper().generateRandomMinDistancePositions(
                                                                            levelXBoundary,numberOfEnemies, minPositionDistance);
        for(int i = 0; i < numberOfEnemies; i++) {
            Enemy enemy = spawnRandomEnemy();
            enemy.getSprite().setPosition(positions.get(i).x, LevelFactory.getCurrentGroundLevel());
            enemyArray.add(enemy);
        }
    }

    // ===================================================================================================================

    @Override
    public void draw(Batch batch, float alpha) {

        for (Enemy enemy : enemyArray) {
            enemy.draw(batch, alpha);
        }
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        for(Enemy enemy : enemyArray) {
            enemy.act(delta);

            if (enemy.getCharacterState() == Character.CharacterState.DEAD) {
                UICounters.enemiesKilled += 1;          // Add a kill count
                removedEnemies.add(enemy);              // remove enemy from the list
            }
        }

        for(Enemy removedEnemy: removedEnemies) {
            enemyArray.remove(removedEnemy);
            removedEnemy.dispose();
        }
        removedEnemies.clear();
    }

    // ===================================================================================================================

    /**
     *  Moves the characters in the opposite direction to oppose the cameras movement,
     *  giving the impression that they are not moving if they are static objects.
     **/
    public void compensateCamera(float cameraPositionAmount) {

        for(Enemy enemy : enemyArray) {
            enemy.compensateCamera(cameraPositionAmount);
        }
    }

    // ===================================================================================================================


    /**
     *  Selects a random enemy name from the enemyArray and then creates that enemy.
     *  This approach creates the enemy only when it is actually needed to save on processing, as opposed to creating and preloading all enemies at the start
     **/
    public Enemy spawnRandomEnemy() {

        Enemy enemy           = new Enemy();

        int randomIndex       = MathUtils.random(0, enemyNameList.length - 1);
        String selectedEnemy  = enemyNameList[randomIndex];

        if(Objects.equals(selectedEnemy, "Dragon")) {
            return new EnemyDragon();
        }
        else if(Objects.equals(selectedEnemy, "Devil Guy")) {
            return new EnemyDevilGuy();
        }
        else if(Objects.equals(selectedEnemy, "Wolf")) {
            return new EnemyWolf();
        }
        else if(Objects.equals(selectedEnemy, "Yeti")) {
            return new EnemyYeti();
        }
        else if(Objects.equals(selectedEnemy, "Robot")) {
            return new EnemyRobot();
        }
        else if(Objects.equals(selectedEnemy, "Archer")) {
            return new EnemyArcher();
        }
        else if(Objects.equals(selectedEnemy, "Black Wizard")) {
            return new EnemyBlackWizard();
        }
        else if(Objects.equals(selectedEnemy, "Santa")) {
            return new EnemySantaClaus();
        }
        else if(Objects.equals(selectedEnemy, "Crab")) {
            return new EnemyCrab();
        }
        else if(Objects.equals(selectedEnemy, "Ninja")) {
            return new EnemyNinja();
        }
        return enemy;
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "enemyFactory.dispose");

        if(enemyArray != null) {
            for(Enemy enemy : enemyArray) {
                enemy.dispose();
            }
        }
    }

    // ===================================================================================================================

    public ArrayList<Enemy> getEnemies() { return enemyArray; }
}
