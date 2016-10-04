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
import com.coldpixel.sparkle.tools.AssetHelper;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mike
 */
public class GameOver implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private BitmapFont bitmapFont = new BitmapFont();
	private ArrayList<String> deathMessages = new ArrayList<String>();
	private Random rnd = new Random();
	private int score = 0;
	
    public GameOver(Game game, int score, int deathCounter, AssetHelper assetHelper) {
        this.game = game;
		this.score = score;
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
		initDeathMessages();

        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);//take up entire stage

        Label gameOverLabel = new Label("GAME OVER", font);
		Label deathMessage = new Label(rndArrItem(),font);
		deathMessage.setColor(new Color(Color.CHARTREUSE));
        Label playAgainLabel = new Label("Press SPACE to play again!", font);
        Label scoreLabel = new Label("Total Score: " + score, font);
        Label killedEnemysLabel = new Label("Total enemys killed: " + deathCounter, font);
//		table.setDebug(true);
        table.add(gameOverLabel).expandX();
        table.row();
		table.add(deathMessage).padTop(5);
		table.row();
        table.add(playAgainLabel).expandX().padTop(20f);
        table.row();
        table.add(scoreLabel).padTop(80).left().padLeft(Constants.getWINDOW_WIDTH() / 2.3f);
        table.row();
        table.add(killedEnemysLabel).left().padLeft(Constants.getWINDOW_WIDTH() / 2.3f);

        stage.addActor(table);
        assetHelper.stopMusic();
        assetHelper.stopSound();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            game.setScreen(new PlayScreen((Main) game));
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new StartGame((Main) game));
            dispose();
        }
        stage.draw();
    }
	
	private void initDeathMessages(){
		deathMessages.add("Leave and shame or play and gain.");
		deathMessages.add("Wanna try again?");
		deathMessages.add("You can do it if you really try!");
		deathMessages.add("You've came so far, to lose it all.");
		deathMessages.add("Dont lose hope.");
		deathMessages.add("You died...nothing more to say.");
		deathMessages.add("Did you even try?");
		deathMessages.add("Holy Crap! You died!");
		deathMessages.add("This is the Game Over screen, which means you lost.");
		deathMessages.add("Git gud");
		deathMessages.add("Don't turn your back on me now");
		deathMessages.add("So many dumb ways to die");
		deathMessages.add("This ain't Hogwarts, this is serious");
		deathMessages.add("Get your magic together");
		deathMessages.add("Next time I wanna be stronger, next time ...");
		deathMessages.add("Once a loser, always a loser");
		deathMessages.add("I heard a rumor that there is a surprise  if you get a score over 9000");
	}
	
	private String rndArrItem(){
		String deathMessage = "";
		if(score < 9000){
			int index = rnd.nextInt(deathMessages.size());
			deathMessage = "I heard a rumor that there is a surprise  if you get a score of over 9000";
		}else{
			deathMessage = "I guess you are here for the boobs. So here you go: (.)(.)";
		}
		return deathMessage;
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
