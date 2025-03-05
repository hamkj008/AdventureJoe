package com.mygdx.game.Levels;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Actors.Characters.Enemies.EnemyFactory;
import com.mygdx.game.Actors.GameObjects.GameObjects;
import com.mygdx.game.Actors.GameObjects.LevelEnd;
import com.mygdx.game.Game.MyGdxGame;



/**
 * Contains the configuration logic needed to create an entire level, with map and game objects.
 * New levels can be easily added and configured here.
 * The correct current level is created automatically by calling createCurrentLevel which will overwrite the current level container.
 **/
public class LevelFactory {

    public enum LevelNum { Level1, Level2 }

    private final LevelCreator currentLevel;
    private final GameObjects currentLevelObjects;
    private static float currentGroundLevel;
    private final EnemyFactory enemyFactory;
    private float playerYStartPosition = 0;

    // ===================================================================================================================

    public LevelFactory() {
        currentLevel        = new LevelCreator();
        enemyFactory        = new EnemyFactory();
        currentLevelObjects = new GameObjects();
    }

    // ===================================================================================================================

    public void createLevel1() {

        int[] background = { 0, 1, 2, 3, 4, 5 };
        int[] foreground = { 6, 7, 8, 9, 10 };
        currentLevel.createLevel("Levels/Level1/Level1_Pixel3_master.tmx", foreground, background, 4, 20000);
        currentGroundLevel = currentLevel.getGroundLevel();
        playerYStartPosition = currentGroundLevel;

        currentLevelObjects.spawnGameObjects(5, 4, 2, currentLevel.getLevelXBoundary());
        currentLevelObjects.getLevelEnd().setGoalType(LevelEnd.GoalType.BABY);

        enemyFactory.spawnEnemies(currentLevel.getLevelXBoundary(), 10, 1000);
    }

    // ===================================================================================================================

    public void createLevel2() {

        int[] background = { 0, 1 };
        int[] foreground = { 2, 3, 4, 5, 6, 7, 8 };
        currentLevel.createLevel("Levels/Level3/Level3.tmx", foreground, background, 3, 20000);
        currentGroundLevel = currentLevel.getGroundLevel();
        playerYStartPosition = currentGroundLevel;

        currentLevelObjects.spawnGameObjects(30, 4, 2, currentLevel.getLevelXBoundary());
        currentLevelObjects.getLevelEnd().setGoalType(LevelEnd.GoalType.PRINCESS);

        enemyFactory.spawnEnemies(currentLevel.getLevelXBoundary(), 10, 1000);
    }

    // ===================================================================================================================

    // Creates whatever level MyGdxGame calls the current level
    public void createCurrentLevel() {
        switch(MyGdxGame.levelNum) {
            case Level1:
                createLevel1();
                break;

            case Level2:
                createLevel2();
                break;
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "levelFactory.dispose");

        currentLevel.dispose();
        enemyFactory.dispose();
        currentLevelObjects.dispose();
    }


    // ================================ GETTERS AND SETTERS ===========================================================================

    public LevelCreator getCurrentLevel() { return currentLevel; }

    public GameObjects getGameObjects() { return currentLevelObjects; }

    public static float getCurrentGroundLevel() { return currentGroundLevel; }

    public EnemyFactory getEnemyFactory() { return enemyFactory; }

    public float getPlayerYStartPosition() { return playerYStartPosition; }
}
