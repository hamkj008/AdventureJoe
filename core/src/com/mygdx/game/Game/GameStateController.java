package com.mygdx.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Actors.Characters.Character;
import com.mygdx.game.Actors.Characters.Enemies.Enemy;
import com.mygdx.game.Actors.Characters.Player.Player;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.UI.UIController;
import com.mygdx.game.UI.UICounters;




/**
 *  Stores objects and data relevant to the game state.
 **/
public class GameStateController {

    private boolean start               = false;
    private final float startingPoint   = 200f;
    private float mapPosition           = startingPoint;
    private String keyboardControl      = "0";

    public enum GameState { PLAYING, RESTART, GAMEOVER }
    private GameState gameState = GameState.PLAYING;

    private Player player;
    private LevelFactory levelFactory;

    private final Stage stage;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean checkTouch;
    private boolean keyboardPaused;


    // ===================================================================================================================

    public GameStateController(Stage stage) {
        this.stage = stage;
    }

    // ===================================================================================================================

    public void create() {

        levelFactory    = new LevelFactory();
        player          = new Player();

        stage.addActor(player);
        stage.addListener(new MyInputListener());
    }

    // ===================================================================================================================

    /**
     *  Starts a fresh new level with all game objects reset ready for play.
     */
    public void newLevel() {

        gameState = GameState.PLAYING;

        levelFactory.dispose();
        levelFactory.createCurrentLevel();

        stage.addActor(levelFactory.getGameObjects());
        stage.addActor(levelFactory.getEnemyFactory());

        player.getStartPosition().y = levelFactory.getPlayerYStartPosition();
        player.reset();
        mapPosition = startingPoint;
        GameScreen.getInstance().getUiController().reset();
    }

    // ===================================================================================================================

    // If the player is killed the game is restarted.
    public void playerDied() {

        gameState = GameState.RESTART;
    }

    // ===================================================================================================================

    public void update(UIController uiController) {

        checkTouch = Gdx.input.isTouched();
        uiController.getUIControlsOverlay().update(checkTouch, Gdx.input.getX(), Gdx.input.getY());

        switch (gameState) {
            case PLAYING:

                // ----------------- RESTRICT PLAYER MOVEMENT --------------------------
                // Prevent player from going off screen to the left, or Prevent player from going too far to the right

                // Map position remembers the players starting point on the map
                if(mapPosition <= startingPoint) {
                    mapPosition = startingPoint;
                    start = true;
                }
                else {
                    start = false;
                }

                // -------------------- TOUCH CONTROLS ---------------------------------------------
                if (checkTouch) {
                    // Move Left
                    if (uiController.getUIControlsOverlay().getLeftButton().isDown() && player.getCharacterState() != Character.CharacterState.FALLING) {
                        movePlayerLeft();
                    }
                    // Move Right
                    if (uiController.getUIControlsOverlay().getRightButton().isDown() && player.getCharacterState() != Character.CharacterState.FALLING
                     && mapPosition < levelFactory.getCurrentLevel().getLevelXBoundary()) {
                        movePlayerRight();
                    }
                    // Jump
                    if (uiController.getUIControlsOverlay().getJumpButton().isDown()) {
                        jumpPlayer();
                    }
                    // Shoot
                    if (uiController.getUIControlsOverlay().getShootButton().isDown()) {
                        playerShoot();
                    }
                }
                /*
                 *  If the screen is no longer being touched while the character is running, the
                 *  running immediately stops and is idle.
                 *  Other animation states have to play out their animations before stopping.
                 */
                if (!checkTouch) {
                    if (player.getCharacterState() == Character.CharacterState.MOVING) {
                        player.setCharacterState(Character.CharacterState.IDLE);
                    }
                }

                // --- Keyboard Controls --
                switch (keyboardControl) {
                    case "Left":
                        movePlayerLeft();
                        break;
                    case "Right":
                        movePlayerRight();
                        break;
                    case "Stop":
                        checkTouch = false;
                        break;
                }
                if(keyboardControl.equals("Space")) {
                    jumpPlayer();
                }
                if(keyboardControl.equals("Enter")) {
                    playerShoot();
                }

                // ---------- GAME CONDITIONS ----------------------

                // If all lives are lost it is game over
                if (UICounters.playerLives <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                if (!player.getIsAlive()) {
                    playerDied();
                }

                // Check if the conditions have been met to clear the level.
//                checkVictoryConditions();


                // ------------ Check for collisions and setAI states--------------------
                levelFactory.getGameObjects().checkCollided(player);
                levelFactory.getCurrentLevel().checkMapPlatformCollision(player);
                levelFactory.getGameObjects().getLevelEnd().setAIStates(player);

                // Check if projectiles have collided enemies
                for(Enemy enemy : levelFactory.getEnemyFactory().getEnemies()) {
                    enemy.setAIStates(player);

                    player.getProjectileSpawner().checkCollided(enemy);         // Check if player projectile collided with enemy
                    if(enemy.getProjectileSpawner() != null) {
                        enemy.getProjectileSpawner().checkCollided(player);     // Check if enemy projectile collided with player
                    }
                }
                break;

            // -------------------------------------------------------------------------------------

            case RESTART:
                newLevel();
                break;

            case GAMEOVER:
                MasterStateController.toggleMusic(false);
                MyGdxGame.startScreen.setStartScreen();
                break;
        }
    }

    // ===================================================================================================================

    public void movePlayerLeft() {

        if (player.getIsGrounded()) {
            // Set the player to running and move the player to the new position.
            player.setDirection(Character.Direction.LEFT);
            player.setCharacterState(Character.CharacterState.MOVING);

            if (!start) {
                // Move the player
                player.moveCharacter();
                // Map position keeps track of how far the player is moving so it can return back to the starting point on the map.
                mapPosition -= player.getPositionAmount().x;

                // ------ CAMERA COMPENSATE -------------------------------
                // Move the camera with the player, and compensate for the movement on other objects
                levelFactory.getCurrentLevel().moveCamera(player);
                levelFactory.getCurrentLevel().collisionCompensateCamera(player.getPositionAmount().x);
                levelFactory.getGameObjects().compensateCamera(player.getPositionAmount().x);
                levelFactory.getEnemyFactory().compensateCamera(player.getPositionAmount().x);
                player.getProjectileSpawner().compensateCamera(player.getPositionAmount().x);
            }
        }
    }

    // ===================================================================================================================

    public void movePlayerRight() {

        if (player.getIsGrounded() && player.getSprite().getX() < levelFactory.getCurrentLevel().getLevelXBoundary()) {
            // Set the player to running and move the player to the new position.
            player.setDirection(Character.Direction.RIGHT);
            player.setCharacterState(Character.CharacterState.MOVING);

            // Move the player
            player.moveCharacter();
            mapPosition += player.getPositionAmount().x;

            // ------ CAMERA COMPENSATE -------------------------------
            // Move the camera with the player, and compensate for the movement on other objects
            levelFactory.getCurrentLevel().moveCamera(player);
            levelFactory.getCurrentLevel().collisionCompensateCamera(-player.getPositionAmount().x);
            levelFactory.getGameObjects().compensateCamera(-player.getPositionAmount().x);
            levelFactory.getEnemyFactory().compensateCamera(-player.getPositionAmount().x);
            player.getProjectileSpawner().compensateCamera(-player.getPositionAmount().x);
        }
    }

    // ===================================================================================================================

    public void jumpPlayer() {
        player.setCharacterState(Character.CharacterState.JUMPING);
    }

    // ===================================================================================================================


    public void playerShoot() {

        if (player.getIsGrounded()) {
            player.setCharacterState(Character.CharacterState.ATTACKING);
            player.getProjectileSpawner().setStartTimer(true);
        }
    }

    // ===================================================================================================================

    // The player must kill the correct number of enemies, collect enough treasure. Only after that can rescue the levelEnd.
    public void checkVictoryConditions() {

        // Rescue the levelEnd
        if(levelFactory.getGameObjects().getLevelEnd().getEndLevel()) {
            MyGdxGame.startScreen.setVictoryScreen1();
        }
    }

    // ===================================================================================================================

    public static void togglePauseGame(boolean pauseGame) {

        if(pauseGame) {
            Gdx.graphics.setContinuousRendering(false);
        }
        else {
            Gdx.graphics.setContinuousRendering(true);
            Gdx.graphics.requestRendering();
        }
    }

    // ===================================================================================================================

    // Listener for Keyboard controls
    private class MyInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.P:
                    keyboardControl = "Pause";
                    keyboardPaused = !keyboardPaused;
                    togglePauseGame(keyboardPaused);
                    break;
                case Input.Keys.A:
                    keyboardControl = "Left";
                    break;
                case Input.Keys.D:
                    keyboardControl = "Right";
                    break;
                case Input.Keys.S:
                    keyboardControl = "Stop";
                    break;
                case Input.Keys.SPACE:
                    keyboardControl = "Space";
                    break;
                case Input.Keys.ENTER:
                    keyboardControl = "Attack";
                    break;
            }
            return false;
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "GameStateControllerDispose");

        levelFactory.dispose();
        player.dispose();
    }

    // ====================================== GETTERS AND SETTERS ================================================================

    public Player getPlayer() { return player; }

    public LevelFactory getLevelFactory() { return levelFactory; }
}
