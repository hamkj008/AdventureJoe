package com.mygdx.game.Actors.Characters.Enemies;
import com.badlogic.gdx.math.MathUtils;


public class EnemyFactory {

    @SuppressWarnings("FieldCanBeLocal")
    private Enemy enemy;

    private final String[] enemyArray = {
            "Dragon",
            "DevilGuy",
            "Yeti",
            "Robot",
            "Wolf",
            "Archer",
            "Black Wizard",
            "Santa",
            "Crab",
            "Ninja"
    };


    public EnemyFactory() {}


    /**
     *  Selects a random enemy name from the enemyArray and then creates that enemy.
     *  This approach creates the enemy only when it is actually needed to save on processing, as opposed to creating and preloading all enemies at the start
     **/
    public Enemy spawnRandomEnemy() {

        enemy                   = new Enemy();

        int randomIndex         = MathUtils.random(0, enemyArray.length - 1);
        String selectedEnemy    = enemyArray[randomIndex];

        switch(selectedEnemy) {
            case "Dragon":
                enemy = createEnemyDragon();
                break;
            case "DevilGuy":
                enemy = createEnemyDevilGuy();
                break;
            case "Wolf":
                enemy = createEnemyWolf();
                break;
            case "Yeti":
                enemy = createEnemyYeti();
                break;
            case "Robot":
                enemy = createEnemyRobot();
                break;
            case "Archer":
                enemy = createEnemyArcher();
                break;
            case "Black Wizard":
                enemy = createEnemyBlackWizard();
                break;
            case "Santa":
                enemy = createEnemySantaClaus();
                break;
            case "Crab":
                enemy = createEnemyCrab();
                break;
            case "Ninja":
                enemy = createEnemyNinja();
                break;
        }
        return enemy;
    }



    // --- CREATE ENEMIES ----------------------------
    public Enemy createEnemyDragon() {
        return new EnemyDragon();
    }

    public Enemy createEnemyDevilGuy() {
        return new EnemyDevilGuy();
    }

    public Enemy createEnemyYeti() {
        return new EnemyYeti();
    }

    public Enemy createEnemyRobot() {
        return new EnemyRobot();
    }

    public Enemy createEnemyWolf() {
        return new EnemyWolf();
    }

    public Enemy createEnemyArcher() {
        return new EnemyArcher();
    }

    public Enemy createEnemyBlackWizard() {
        return new EnemyBlackWizard();
    }

    public Enemy createEnemySantaClaus() {
        return new EnemySantaClaus();
    }

    public Enemy createEnemyCrab() {
        return new EnemyCrab();
    }

    public Enemy createEnemyNinja() {
        return new EnemyNinja();
    }
}
