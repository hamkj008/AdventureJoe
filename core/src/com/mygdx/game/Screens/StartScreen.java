package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Game.MyGdxGame;



public class StartScreen implements Screen {


    private Stage stage;
    private final MyGdxGame game;

    private final VictoryScreen1 victoryScreen1;
    private final VictoryScreen2 victoryScreen2;


    // ===============================================================================================================

    public StartScreen(MyGdxGame game) {
        this.game = game;

        victoryScreen1 = new VictoryScreen1(game);
        victoryScreen2 = new VictoryScreen2(game);
    }

    // ===============================================================================================================

    public void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Buttons
        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));
        BitmapFont font = new BitmapFont(Gdx.files.internal("Fonts/ComicSansMS.fnt"));

        Label gameTitle                 = new Label("Adventure Joe", new Label.LabelStyle(font, Color.BLACK));
        final TextButton playButton     = new TextButton("Play", skin, "default");
        final TextButton optionsButton  = new TextButton("Options", skin, "default");
        final TextButton exitButton     = new TextButton("Exit", skin, "default");

        playButton.setColor(Color.GREEN);
        optionsButton.setColor(Color.BLUE);
        exitButton.setColor(Color.RED);

        gameTitle.setFontScale(3f);
        playButton.getLabel().setFontScale(2.5f);
        optionsButton.getLabel().setFontScale(2.5f);
        exitButton.getLabel().setFontScale(2.5f);


        // Root table
        Table root = new Table();
        root.setFillParent(true);

        // Create a texture and set it as the table's background
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();
        root.setBackground(new TextureRegionDrawable(new Texture(pixmap)));

        // Dispose the pixmap as it's no longer needed
        pixmap.dispose();


        // Buttons Table
        Table table = new Table();

        // Add the elements to the table
        gameTitle.setAlignment(Align.center);
        table.add(gameTitle).width(400).height(200).expandX();
        table.row();
        table.add(playButton).height(150).width(300).pad(10).space(20);
        table.row();
        table.add(optionsButton).height(150).width(300).pad(10).space(20);
        table.row();
        table.add(exitButton).height(150).width(300).pad(20);

        // Add the table to the root and the root to the stage.
        root.add(table);
        // Set the table to the middle of the screen
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(root);


        // ------- BUTTON LISTENERS -------
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GameScreen.getInstance());
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(MyGdxGame.optionsScreen);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }


    // ===============================================================================================================

    // ---------- GETTERS AND SETTERS -------------------------------------

    public void setStartScreen() { game.setScreen(MyGdxGame.startScreen); }
    public void setVictoryScreen1() { game.setScreen(MyGdxGame.startScreen.victoryScreen1); }
    public void setVictoryScreen2() { game.setScreen(MyGdxGame.startScreen.victoryScreen2); }

    // ===============================================================================================================

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

    // ===============================================================================================================

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
        stage.dispose();
    }

}
