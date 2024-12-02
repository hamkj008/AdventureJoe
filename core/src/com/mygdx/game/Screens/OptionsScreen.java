package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Game.MasterStateController;
import com.mygdx.game.Game.MyGdxGame;

public class OptionsScreen implements Screen {

    private final MyGdxGame game;
    private Stage stage;

    // ===================================================================================================================

    public OptionsScreen(MyGdxGame game) {
        this.game = game;
    }

    // ===================================================================================================================


    public void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));

        final TextButton musicToggleButton = new TextButton(setButtonText(MasterStateController.getMusic().isPlaying()), skin, "default");
        final TextButton backButton = new TextButton("Back", skin, "default");

        musicToggleButton.getLabel().setFontScale(2f);
        backButton.getLabel().setFontScale(2f);


        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("dialogDim"));

        Table table = new Table();


        table.add(musicToggleButton).height(150).width(300).pad(20).space(100);
        table.row();
        table.row();
        table.add(backButton).height(100).width(200).pad(20);

        root.add(table);
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(root);

        musicToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MasterStateController.toggleMusic(MasterStateController.getMusic().isPlaying());
                musicToggleButton.setText(setButtonText(MasterStateController.getMusic().isPlaying()));
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            game.setScreen(MyGdxGame.startScreen);
            }
        });
    }

    // ===================================================================================================================

    public String setButtonText(boolean state) {
        return state ? "Music: ON" : "Music: OFF";
    }

    // ===================================================================================================================

    @Override
    public void show() {
        create();
    }

    // ===================================================================================================================

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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
        Gdx.app.log("dispose", "OptionsScreenDispose");

        // MyGDXGame only calls a screens create method if setScreen().
        // If a screens create method has not executed the stage may be null
        if(stage != null) {
            stage.dispose();
        }
    }
}
