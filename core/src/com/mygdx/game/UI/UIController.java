package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class UIController extends Actor {

    private final UIControlsOverlay uiControlsOverlay;
    private final UICounters uiCounters;
    private final PlayerHealthBar playerHealthBar;


    // ===================================================================================================================

    public UIController(Stage stage) {

        uiControlsOverlay   = new UIControlsOverlay(stage);
        uiCounters          = new UICounters();
        playerHealthBar     = new PlayerHealthBar();

        stage.addActor(uiControlsOverlay); // Add the stage to the UiControls to register input events on the buttons
    }

    // ===================================================================================================================

    public void draw(Batch batch, float alpha) {
        uiCounters.draw(batch, alpha);
        playerHealthBar.draw(batch);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "UiControllerDispose");

        uiControlsOverlay.dispose();
        playerHealthBar.dispose();
    }

    // ================================= GETTERS AND SETTERS ==================================================================

    public UIControlsOverlay getUIControlsOverlay() { return uiControlsOverlay; }

    public UICounters getUICounters() { return uiCounters; }

    public PlayerHealthBar getPlayerHealthBar() { return playerHealthBar; }
}
