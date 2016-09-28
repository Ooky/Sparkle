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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Constants;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author mike
 */
public class GameOver implements Screen {

	private Viewport viewport;
	private Stage stage;
	private Game game;
	private BitmapFont bitmapFont = new BitmapFont();

	public GameOver(Game game, int score, int deathCounter) {
		this.game = game;
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, ((Main) game).batch);

		Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);

		Table table = new Table();
		table.center();
		table.setFillParent(true);//take up entire stage

		Label gameOverLabel = new Label("GAME OVER", font);
		Label playAgainLabel = new Label("Press SPACE to play again!", font);
		Label scoreLabel = new Label("Total Score: " + score, font);
		Label killedEnemysLabel = new Label("Total enemys killed: " + deathCounter, font);
//		table.setDebug(true);
		table.add(gameOverLabel).expandX();
		table.row();
		table.add(playAgainLabel).expandX().padTop(10f);
		table.row();
		table.add(scoreLabel).padTop(80).left().padLeft(Constants.getWINDOW_WIDTH() / 2.3f);
		table.row();
		table.add(killedEnemysLabel).left().padLeft(Constants.getWINDOW_WIDTH() / 2.3f);

		stage.addActor(table);

	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			game.setScreen(new PlayScreen((Main) game));
			dispose();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			game.setScreen(new StartGame((Main) game));
			dispose();
		}
		Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	public void update(float dt) {

	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int i, int i1) {
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
