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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
public class HTPCredits implements Screen {//HowToPlayCredits Screen

	private Viewport viewport;
	private Stage stage;
	private Game game;
	private BitmapFont bitmapFont = new BitmapFont();
	private TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/pack.atlas"));
	private Skin skin = new Skin();

	public HTPCredits(final Game game) {
		this.game = game;
		viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, ((Main) game).batch);

		Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);

		Table table = new Table();
		table.center();
		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);

		Label howToPlayTitle = new Label("HOW TO PLAY", font);
		Label howToPlay = new Label("You deal more Damage depending on your and the enemys current element."
				+ "\nYour goal is to kill as many enemys as possible, you will die anyways."
				+ "\nIf you are in the center, near the crystal, you'll regain some health over time.", font);
		Label elementsControls = new Label(""
				+ "\nWater -> Fire                                                                   WSAD = Movement"
				+ "\nFire -> Earth                                                ARROW-KEYS = Shootdirection"
				+ "\nEarth -> Air                                                                          ESC = Mainmenu"
				+ "\nAir -> Water", font);

		Label creditsTitle = new Label("CREDITS", font);
		Label credits = new Label("We did barely all on our own except the background music and sound."
				+ "\nProbably Email?"
				+ "\nProbably copyright?", font);
		Label supportUSTitle = new Label("WANT TO SUPPORT US?", font);
		Label supportUS = new Label("This is our first released game. It would help us a lot if you donate something."
				+ "\nWe'd appreciate every amount.", font);

		skin.addRegions(buttonAtlas);
		TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = new BitmapFont();
		buttonStyle.up = skin.getDrawable("Blue");
		buttonStyle.down = skin.getDrawable("Grey");

		TextButton button = new TextButton("Donate", buttonStyle);
		button.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
				System.out.println("hi");
				Gdx.net.openURI("https://www.google.com");
			}
		});

		Label stars1 = new Label("************************************************************************************", font);
		Label stars2 = new Label("************************************************************************************", font);
		Label stars3 = new Label("************************************************************************************", font);

		Label escape = new Label("Press \"ESC\" or \"C\" to go back", font);

//		table.setDebug(true);
		table.add(howToPlayTitle).expandX();
		table.row();
		table.add(stars1);
		table.row();
		table.add(howToPlay).expandX();
		table.row();
		table.add(elementsControls).padTop(10f);
		table.row();
		table.add(creditsTitle).expandX().padTop(30f);
		table.row();
		table.add(stars2);
		table.row();
		table.add(credits).expandX().padRight(35);
		table.row();
		table.add(supportUSTitle).expandX().padTop(30f);
		table.row();
		table.add(stars3);
		table.row();
		table.add(supportUS).expandX();
		table.row();
		table.add(button).expandX().padTop(100f);
		table.row();
		table.add(escape).expandX().padTop(120);

		stage.addActor(table);
	}

	@Override
	public void render(float f) {
		Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isKeyJustPressed(Input.Keys.C))) {
			game.setScreen(new StartGame((Main) game));
			dispose();
		}
		stage.draw();
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
