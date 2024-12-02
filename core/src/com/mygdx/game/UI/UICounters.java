package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Game.GameStateController;


public class UICounters {

    private Label killLabel;
    private Label livesLabel;
    private Label treasureLabel;
    private final BitmapFont font;


    // ===================================================================================================================

    public UICounters() {

        font = new BitmapFont(Gdx.files.internal("Fonts/ComicSansMS.fnt"));

        createKillCounter(new Vector2(50, 50));
//        createKillCounter(new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 100));
        createLivesCounter(new Vector2(0f, Gdx.graphics.getHeight()));
        createTreasureCounter(new Vector2(0f, Gdx.graphics.getHeight()));
    }

    // ===================================================================================================================

    public void draw(Batch batch, float alpha){

        // Update the counters
        killLabel.setText("Kills:" + GameStateController.enemiesKilled);
        livesLabel.setText("Lives:" + GameStateController.playerLives);
        treasureLabel.setText("$:" + GameStateController.treasureScore);

        killLabel.draw(batch, alpha);
        livesLabel.draw(batch, alpha);
        treasureLabel.draw(batch, alpha);
    }


    public void createKillCounter(Vector2  position) {

        killLabel = new Label("Kills:" + GameStateController.enemiesKilled, new Label.LabelStyle(font, Color.ORANGE));
        killLabel.setFontScale(3f);
        killLabel.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 100);
    }

    public void createLivesCounter(Vector2  position) {

        livesLabel = new Label("Lives:" + GameStateController.playerLives, new Label.LabelStyle(font, Color.ORANGE));
//        livesLabel.setFontScale(3f);
        livesLabel.setPosition(position.x, position.y);
    }

    public void createTreasureCounter(Vector2  position) {

        treasureLabel = new Label("$:" + GameStateController.playerLives, new Label.LabelStyle(font, Color.ORANGE));
//        treasureLabel.setFontScale(3f);
        treasureLabel.setPosition(position.x, position.y);
    }


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
