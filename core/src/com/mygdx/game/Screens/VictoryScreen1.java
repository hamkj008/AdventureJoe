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


public class VictoryScreen1 implements Screen {

    private Texture texture;
    private Texture nextBtn;
    private Texture exitBtn;
    private TextureRegion textureRegion;
    private TextureRegion textureRegion1;
    private Image backBtn;
    private Image begin;
    private Image exit;
    private Stage stage;

    private final MyGdxGame game;


    // ===================================================================================================================

    public VictoryScreen1(MyGdxGame game) {
        this.game = game;
    }

    // ===================================================================================================================

    private void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        texture     = new Texture("player/victory1.jpg");
        nextBtn     = new Texture("player/next.jpg");
        exitBtn     = new Texture("player/exit.jpg");

        backBtn     = new Image(texture);
        backBtn.setSize(1920,1080);

        textureRegion   = new TextureRegion(nextBtn,0,80,636,200);
        textureRegion1  = new TextureRegion(exitBtn,0,40,600,250);
        begin           = new Image(textureRegion);
        exit            = new Image(textureRegion1);

        begin.setSize(300,100);
        begin.setPosition(Gdx.graphics.getWidth() / 4f,Gdx.graphics.getHeight() / 4f);
        exit.setSize(300,100);
        exit.setPosition(Gdx.graphics.getWidth() / 3f * 2,Gdx.graphics.getHeight() / 4f);

        stage.addActor(backBtn);
        stage.addActor(begin);
        stage.addActor(exit);

        begin.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MyGdxGame.levelNum = LevelFactory.LevelNum.Level2;
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
        texture.dispose();
        nextBtn.dispose();
        exitBtn.dispose();
        stage.dispose();
    }
}
