package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Game.GameHelper;
import com.mygdx.game.Game.GameStateController;
import com.mygdx.game.UI.UIController;


/*
The screen that contains the actual game. It is a singleton, allowing other classes to access its elements.
 */
public class GameScreen implements Screen {

    private GameHelper helper;
    private GameStateController gameStateController;
    private Stage stage;
    private SpriteBatch uiBatch;
    private ShapeRenderer shapeRenderer;

    private UIController uiController;

    private static GameScreen INSTANCE = null;

    // ===================================================================================================================


    // --- Singleton ---------------
    private GameScreen() {}

    public static GameScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }
    // ----------------------------------


    // ===================================================================================================================

    @Override
    public void show() {
        create();
    }

    public void create() {
        Gdx.app.log("flow", "GameScreen:create");

        // Shape renderer to draw bounding boxes.
        shapeRenderer = new ShapeRenderer();

        uiBatch = new SpriteBatch();
        helper = new GameHelper();

        // ----- STAGE ----------
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        gameStateController = new GameStateController(stage);
        gameStateController.create();

        uiController = new UIController(stage);
        stage.addActor(uiController);

        // --- START NEW GAME ---------
        gameStateController.newLevel();
    }

    // ===================================================================================================================

    private void update() {
        gameStateController.update(uiController);
    }

    // ===================================================================================================================

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        update();

        try {
            // Check the stage for null actors
            checkStageForNull(stage);

            if (stage != null) {
                stage.act(delta);
                gameStateController.getLevelFactory().getCurrentLevel().renderMap(gameStateController.getPlayer());


                // ----------------- ** Render the bounding boxes. ** Very useful for debugging ** ---------------------
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.rect(gameStateController.getPlayer().getSprite().getX(), gameStateController.getPlayer().getSprite().getY(),
                        gameStateController.getPlayer().getSprite().getWidth(), gameStateController.getPlayer().getSprite().getHeight());

//                shapeRenderer.rect(uiController.getUICounters().getKillCounter().getX(), uiController.getUICounters().getKillCounter().getY(),
//                        uiController.getUICounters().getKillCounter().getWidth(), uiController.getUICounters().getKillCounter().getHeight());
//
//                shapeRenderer.rect(gameStateController.getPlayer().getProjectile().getProjectileSprite().getX(),
//                        gameStateController.getPlayer().getProjectile().getProjectileSprite().getY(),
//                        gameStateController.getPlayer().getProjectile().getProjectileSprite().getWidth(),
//                        gameStateController.getPlayer().getProjectile().getProjectileSprite().getHeight());
//
//                shapeRenderer.rect(randomEnemy.getSprite().getX(), gameStateController.getRandomEnemy().getSprite().getY(),
//                        gameStateController.getRandomEnemy().getSprite().getWidth(), gameStateController.getRandomEnemy().getSprite().getHeight());
//
//                shapeRenderer.rect(levelFactory.getGameObjects().getLevelEnd().getSprite().getX(),
//                        levelFactory.getGameObjects().getLevelEnd().getSprite().getY(),
//                        levelFactory.getGameObjects().getLevelEnd().getSprite().getWidth(),
//                        levelFactory.getGameObjects().getLevelEnd().getSprite().getHeight());

//                shapeRenderer.rect(gameStateController.getLevelFactory().getGameObjects().getLevelEnd().getSprite().getX(),
//                        gameStateController.getLevelFactory().getGameObjects().getLevelEnd().getSprite().getY(),
//                        gameStateController.getLevelFactory().getGameObjects().getLevelEnd().getSprite().getWidth(),
//                        gameStateController.getLevelFactory().getGameObjects().getLevelEnd().getSprite().getHeight());
//
//                for (int i = 0; i < levelFactory.getCurrentLevel().getCollisionSprites1().length; i++) {
//                    shapeRenderer.rect(levelFactory.getCurrentLevel().getCollisionSprites1()[i].getX(),
//                            levelFactory.getCurrentLevel().getCollisionSprites1()[i].getY(),
//                            levelFactory.getCurrentLevel().getCollisionSprites1()[i].getWidth(),
//                            levelFactory.getCurrentLevel().getCollisionSprites1()[i].getHeight());
//                    shapeRenderer.rect(levelFactory.getCurrentLevel().getCollisionSprites2()[i].getX(),
//                            levelFactory.getCurrentLevel().getCollisionSprites2()[i].getY(),
//                            levelFactory.getCurrentLevel().getCollisionSprites2()[i].getWidth(),
//                            levelFactory.getCurrentLevel().getCollisionSprites2()[i].getHeight());
//                }
//                shapeRenderer.rect(levelFactory.getCurrentLevel().getGroundRectangle().getX(),
//                        levelFactory.getCurrentLevel().getGroundRectangle().getY(),
//                        levelFactory.getCurrentLevel().getGroundRectangle().getWidth(),
//                        levelFactory.getCurrentLevel().getGroundRectangle().getHeight());
//
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.end();
                // ----------------------------------------------------------------------------------------------------

                // Render the stage actors
                stage.draw();
            }
        } catch (NullPointerException e) {
            Gdx.app.error("Error", "A null pointer was encountered in render.", e);
        }
    }

    // ===================================================================================================================

    // If an actor is null and the stage tries to draw it, the program will crash without notice.
    // This checks to see if any actors are null and gives warning along with try catch block that gives the stack trace.
    public void checkStageForNull(Stage stage) {
        if (stage == null) {
            Gdx.app.log("Error", "Stage is null.");
            return;
        }

        // Iterate through all actors in the stage
        for (Actor actor : stage.getActors()) {
            if (actor == null) {
                Gdx.app.log("Error", "Actor is null in stage.");
            } else {
                Gdx.app.log("ActorLog", "Actor found: " + actor.getClass().getSimpleName());
            }
        }
    }

    // ===================================================================================================================

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        Gdx.app.log("dispose", "GameScreenDispose");
        // MyGDXGame only calls a screens create method if setScreen().
        // If a screens create method has not executed the stage may be null
        if(stage != null) {
            gameStateController.dispose();
            uiBatch.dispose();
            stage.dispose();
        }
    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public GameHelper getHelper() { return helper; }

    public GameStateController getGameStateController() { return gameStateController; }

    public UIController getUiController() { return uiController; }
}
