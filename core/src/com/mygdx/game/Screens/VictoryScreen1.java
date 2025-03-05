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
import com.mygdx.game.UI.UICounters;


public class VictoryScreen1 implements Screen {

    private Texture backgroundTexture;
    private Texture startTexture;
    private Texture exitTexture;
    private Stage   stage;

    private final MyGdxGame game;


    // ===================================================================================================================

    public VictoryScreen1(MyGdxGame game) {
        this.game = game;
    }

    // ===================================================================================================================

    private void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture               = new Texture("player/victory1.jpg");
        startTexture                    = new Texture("player/next.jpg");
        exitTexture                     = new Texture("player/exit.jpg");

        Image background                = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        TextureRegion textureRegion1    = new TextureRegion(startTexture,0,80,636,200);
        TextureRegion textureRegion2    = new TextureRegion(exitTexture,0,40,600,250);
        Image startButton               = new Image(textureRegion1);
        Image exitButton                = new Image(textureRegion2);

        startButton.setSize(300,100);
        startButton.setPosition(Gdx.graphics.getWidth() / 4f,Gdx.graphics.getHeight() / 4f);
        exitButton.setSize(300,100);
        exitButton.setPosition(Gdx.graphics.getWidth() / 3f * 2,Gdx.graphics.getHeight() / 4f);

        stage.addActor(background);
        stage.addActor(startButton);
        stage.addActor(exitButton);

        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(GameScreen.getInstance());
                return true;
            }
        });

        exitButton.addListener(new InputListener(){
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
        startTexture.dispose();
        exitTexture.dispose();
        stage.dispose();
    }
}
