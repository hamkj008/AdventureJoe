package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Game.MyGdxGame;


public class VictoryScreen2 implements Screen {

    private Texture backgroundTexture;
    private Texture restartTexture;
    private Texture exitTexture;
    private Stage   stage;

    private final MyGdxGame game;


    // ===================================================================================================================

    public VictoryScreen2(MyGdxGame game) {
        this.game = game;
    }

    // ===================================================================================================================

    private void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture               = new Texture(Gdx.files.internal("player/victory.jpg"));
        restartTexture                  = new Texture(Gdx.files.internal("player/restart.jpg"));
        exitTexture                     = new Texture(Gdx.files.internal("player/exit.jpg"));
        Image background                = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        TextureRegion textureRegion1    = new TextureRegion(restartTexture,0,80,1024,400);
        TextureRegion textureRegion2    = new TextureRegion(exitTexture,0,40,600,250);
        Image beginBtn                     = new Image(textureRegion1);
        Image exitBtn                      = new Image(textureRegion2);

        beginBtn.setSize(300,100);
        beginBtn.setPosition(Gdx.graphics.getWidth() / 4f,Gdx.graphics.getHeight() / 4f);
        exitBtn.setSize(300,100);
        exitBtn.setPosition(Gdx.graphics.getWidth() / 3f * 2,Gdx.graphics.getHeight() / 4f);

        stage.addActor(background);
        stage.addActor(beginBtn);
        stage.addActor(exitBtn);

        beginBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.levelNum = LevelFactory.LevelNum.Level1;
                game.setScreen(GameScreen.getInstance());
                return true;
            }
        });

        exitBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
    }

    // ===================================================================================================================


    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

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
        backgroundTexture.dispose();
        restartTexture.dispose();
        exitTexture.dispose();
        stage.dispose();
    }
}
