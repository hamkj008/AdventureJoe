package com.mygdx.game.UI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class UICounters extends Actor {

    private Label killLabel;
    private Label livesLabel;
    private Label treasureLabel;
    private final BitmapFont font;

    public static int playerLives       = 3;
    public static int enemiesKilled     = 0;
    public static int treasureScore     = 0;


    // ===================================================================================================================

    public UICounters() {

        font = new BitmapFont(Gdx.files.internal("Fonts/ComicSansMS.fnt"));

        createKillCounter();
        createLivesCounter();
        createTreasureCounter();
    }

    // ===================================================================================================================

    @Override
    public void act(float delta) {

        // Update the counters
        killLabel.setText("Kills:" + enemiesKilled);
        livesLabel.setText("Lives:" + playerLives);
        treasureLabel.setText("$" + treasureScore);
    }

    // ===================================================================================================================

    public void reset() {
        enemiesKilled = 0;
        treasureScore = 0;
    }

    // ===================================================================================================================

    public void createKillCounter() {

        killLabel = new Label("Kills:" + enemiesKilled, new Label.LabelStyle(font, Color.ORANGE));
        killLabel.setFontScale(2f);
    }

    public void createLivesCounter() {

        livesLabel = new Label("Lives:" + playerLives, new Label.LabelStyle(font, Color.ORANGE));
        livesLabel.setFontScale(2f);
    }

    public void createTreasureCounter() {

        treasureLabel = new Label("$" + treasureScore, new Label.LabelStyle(font, Color.RED));
        treasureLabel.setFontScale(2f);
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "UiCounters.dispose");

        font.dispose();
    }

    // ===================================================================================================================

    public Label getKillCounter() {
        return killLabel;
    }

    public Label getLivesCounter() {
        return livesLabel;
    }

    public Label getTreasureCounter() {
        return treasureLabel;
    }

}
