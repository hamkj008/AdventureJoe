package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;



public class UIController extends Actor {

    private final UIControlsOverlay uiControlsOverlay;
    private final UICounters uiCounters;
    private final PlayerHealthBar playerHealthBar;
    public HorizontalGroup counterHBox;

    // ===================================================================================================================

    public UIController(Stage stage) {

        uiControlsOverlay   = new UIControlsOverlay(stage);
        uiCounters          = new UICounters();
        playerHealthBar     = new PlayerHealthBar();
        playerHealthBar.getSprite().setPosition(80, Gdx.graphics.getHeight() - 120);

        counterHBox = new HorizontalGroup();
        counterHBox.space(100);
        counterHBox.addActor(uiCounters.getLivesCounter());
        counterHBox.addActor(uiCounters.getKillCounter());
        counterHBox.addActor(uiCounters.getTreasureCounter());
        counterHBox.setPosition(playerHealthBar.getSprite().getX() + playerHealthBar.getSprite().getWidth() + 500, Gdx.graphics.getHeight() - 80);

        stage.addActor(playerHealthBar);
        stage.addActor(counterHBox);
        stage.addActor(uiControlsOverlay); // Add the stage to the UiControls to register input events on the buttons
    }

    // ===================================================================================================================

    public void reset() {
        uiCounters.reset();
        playerHealthBar.reset();
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {
        uiCounters.act(delta);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "UiController.dispose");

        uiControlsOverlay.dispose();
        playerHealthBar.dispose();
    }

    // ================================= GETTERS AND SETTERS ==================================================================

    public UIControlsOverlay getUIControlsOverlay() { return uiControlsOverlay; }

    public UICounters getUICounters() { return uiCounters; }

    public PlayerHealthBar getPlayerHealthBar() { return playerHealthBar; }
}



