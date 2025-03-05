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
import com.mygdx.game.Game.MyGdxGame;


public class RestartScreen implements Screen {
    private Texture texture;
    private Texture restartTexture;
    private Texture exitTexture;
    private Stage   stage;

    private final MyGdxGame game;

    public RestartScreen(MyGdxGame game) {
        this.game = game;
    }


    private void init() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        texture                         = new Texture("player/defeated.jpg");
        restartTexture                  = new Texture("player/restart.jpg");
        exitTexture                     = new Texture("player/exit.jpg");

        TextureRegion textureRegion1    = new TextureRegion(restartTexture,0,80,1024,400);
        TextureRegion textureRegion2    = new TextureRegion(exitTexture,0,40,600,250);
        Image background                = new Image(texture);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Image restart                     = new Image(textureRegion1);
        Image exit                      = new Image(textureRegion2);

        restart.setSize(300,100);
        restart.setPosition(Gdx.graphics.getWidth() / 4f,Gdx.graphics.getHeight() / 4f);
        exit.setSize(300,100);
        exit.setPosition(Gdx.graphics.getWidth() / 3f * 2,Gdx.graphics.getHeight() / 4f);

        stage.addActor(background);
        stage.addActor(restart);
        stage.addActor(exit);

        restart.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(GameScreen.getInstance());
                return true;
            }
        });

        exit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
    }


    @Override
    public void show() {
        init();
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
        Gdx.app.log("dispose", "RestartScreenDispose");

        texture.dispose();
        restartTexture.dispose();
        exitTexture.dispose();

        // MyGDXGame only calls a screens create method if setScreen().
        // If a screens create method has not executed the stage may be null
        if(stage != null) {
            stage.dispose();
        }
    }
}
