package com.mygdx.game.Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Levels.LevelFactory;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.OptionsScreen;
import com.mygdx.game.Screens.RestartScreen;
import com.mygdx.game.Screens.StartScreen;


/**
 * By Kieran Hambledon
 * Copyright 2024
 */
public class MyGdxGame extends Game implements ApplicationListener {

	public static LevelFactory.LevelNum levelNum;

	public Batch batch;

	public MasterStateController masterStateController;
	public static StartScreen startScreen;
	public static OptionsScreen optionsScreen;
	public static GameScreen gameScreen;
	public static RestartScreen restartScreen;

	// ===================================================================================================================

	@Override
	public void create () {
		Gdx.app.log("dispose", "create");
		batch = new SpriteBatch();

		masterStateController 	= new MasterStateController();
		startScreen 			= new StartScreen(this);
		optionsScreen 			= new OptionsScreen(this);
		restartScreen 			= new RestartScreen(this);

		gameScreen 				= GameScreen.getInstance();
		levelNum 				= LevelFactory.LevelNum.Level1;

		setScreen(startScreen);
	}

	// ===================================================================================================================


	@Override
	public void render () { super.render(); }

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		Gdx.app.log("dispose", "gdxgamedispose");
		super.dispose();

		masterStateController.dispose();
		startScreen.dispose();
		optionsScreen.dispose();
		restartScreen.dispose();
		gameScreen.dispose();
	}
}

