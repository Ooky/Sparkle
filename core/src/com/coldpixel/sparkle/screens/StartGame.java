/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public class StartGame implements Screen {

	private Viewport viewport;
	private Stage stage;
	private Game game;
	private TextButton startGameButton;
	private TextButton howToPlay;
	private TextButton credits;
	private TextButton quitGame;
	private TextButtonStyle buttonStyle;
	private BitmapFont font;
	private Skin skin;
	private TextureAtlas buttonAtlas;

	public StartGame(final Game game) {
		this.game = game;
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, ((Main) game).batch);
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/buttons.atlas"));
		skin.addRegions(buttonAtlas);
		buttonStyle = new TextButtonStyle();
		buttonStyle.font = font;
		buttonStyle.up = skin.getDrawable("Blue");
		buttonStyle.down = skin.getDrawable("Grey");
		startGameButton = new TextButton("Start Game", buttonStyle);
		howToPlay = new TextButton("How to Play", buttonStyle);
		credits = new TextButton("Credits", buttonStyle);
		quitGame = new TextButton("Quit Game", buttonStyle);

		Table table = new Table();
		table.center();
		table.setFillParent(true);//take up entire stage
		//		table.setDebug(true);
		table.add(startGameButton);
		table.row();
		table.add(howToPlay).padTop(10);
		table.row();
		table.add(credits).padTop(10);
		table.row();
		table.add(quitGame).padTop(10);
		addListener();

		stage.addActor(table);

	}

	@Override
	public void render(float f) {
//		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
//			game.setScreen(new PlayScreen((Main) game));
//			dispose();
//		}
		Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	private void addListener() {
		//StartGameButton
		startGameButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
				game.setScreen(new PlayScreen((Main) game));
				dispose();
			}
		});
		//HowToPlay
		howToPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
				System.out.println("ADD WINDOW FOR RULES/IMAGE/WHATEVER!");
			}
		});
		//Credits
		credits.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
				System.out.println("ADD WINDOW CREDITS AND PAYPAL");
			}
		});
		//QuitGame
		quitGame.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void resize(int i, int i1) {
	}

	@Override
	public void show() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
