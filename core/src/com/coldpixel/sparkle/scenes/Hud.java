package com.coldpixel.sparkle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Constants;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public class Hud implements Disposable {

//==============================================================================
//Initialization
//==============================================================================   
	//Scene2D.ui Stage and its own Viewport for HUD
	public Stage stage;
	private final Viewport viewport;
	private final Table tableTop;
	private Table tableBottom;

	private int playerLife;
	private int seconds;
	private int minutes;
	private int hours;
	private int scoreValue;
	private int maxLife;
	private final float gap;

	private final ShapeRenderer shaperenderer;
	private final float lifebarWidth;
	private float lifebarLength;
	private final float lifebarHeight;

	private Label playerLifeLabel;
	private Label timeValueLabel;

	//  private Label scoreLabel;
	private Label scoreValueLabel;

	private long startTime = 0;
	private int timeValue;
	private long startTimeElements = 0;
	private int timeValueElements = 0;

	private Texture actionBar;

	private Label cooldownLabel1;
	private Label cooldownLabel2;
	private Label cooldownLabel3;
	private Label cooldownLabel4;
	public int cooldownValue;
	private Color grey;

//==============================================================================
//Methods
//==============================================================================
	public Hud(SpriteBatch sb) {
		maxLife = 100;
		playerLife = 100;
		scoreValue = 0;
		timeValue = 0 //Testvalue = 86390
				;
		seconds = 0;
		minutes = 0;
		hours = 0;
		grey = new Color(0.05f, 0.05f, 0.05f, 0.8f);

		cooldownValue = 3;
		cooldownLabel1 = new Label(String.format("%01d", cooldownValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		cooldownLabel2 = new Label(String.format("%01d", cooldownValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		cooldownLabel3 = new Label(String.format("%01d", cooldownValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		cooldownLabel4 = new Label(String.format("%01d", cooldownValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		gap = Gdx.graphics.getHeight() / 48;
		shaperenderer = new ShapeRenderer();
		startTime = TimeUtils.nanoTime();
		switch (Main.V_WIDTH) {
			case 3840:
				lifebarWidth = 440;
				break;
			case 1920:
				lifebarWidth = 240;
				break;
			case 1280:
				lifebarWidth = 180;
				break;
			case 1024:
				lifebarWidth = 156;
				break;
			case 800:
				lifebarWidth = 135;
				break;
			case 720:
				lifebarWidth = 125;
				break;
			case 640:
				lifebarWidth = 120;
				break;
			case 480:
				lifebarWidth = 110;
				break;
			default:
				lifebarWidth = Gdx.graphics.getWidth() / 8;
		}

		lifebarHeight = 20;

		viewport = new StretchViewport(Constants.getWINDOW_WIDTH(), Constants.getWINDOW_HEIGTH(), new OrthographicCamera());
		stage = new Stage(viewport, sb);

		tableTop = new Table();
		tableTop.top();
		tableTop.setFillParent(true);//=size of the stage
		playerLifeLabel = new Label(String.format("%03d", playerLife) + " / " + maxLife, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
		//scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		timeValueLabel = new Label("00:00:00", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreValueLabel = new Label(String.format("%06d", scoreValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

//        tableTop.setDebug(true);
		tableTop.drawDebug(shaperenderer);
		tableTop.add(playerLifeLabel).left().padTop(gap).padLeft(Gdx.graphics.getWidth() / 10);
		tableTop.add(timeValueLabel).expandX().padTop(gap);
		tableTop.add(scoreValueLabel).right().padTop(gap).padRight(gap);
		tableTop.row();

		stage.addActor(tableTop);

		actionBar = new Texture(Gdx.files.internal("Graphics/Hud/Actionbar.png"));
	}

	public void drawLifebar() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		lifebarLength = lifebarWidth / 100 * playerLife;
		playerLifeLabel.setText(String.format("%03d", playerLife) + " / " + maxLife);
		shaperenderer.setProjectionMatrix(stage.getCamera().combined);
		shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
		//Behind the Lifebar
		shaperenderer.setColor(new Color(0, 1, 0, 0.2f));
		shaperenderer.rect(gap - 4,//x
				playerLifeLabel.getY(),//y
				lifebarWidth, lifebarHeight);//width,height
		//Actuall Lifebar
		shaperenderer.setColor(Color.GREEN);
		shaperenderer.rect(gap - 4,//x
				playerLifeLabel.getY(),//y
				lifebarLength, lifebarHeight);//width,height
		shaperenderer.setColor(Color.BLACK);
		shaperenderer.end();
		//Black Border around Lifebar
		shaperenderer.begin(ShapeRenderer.ShapeType.Line);
		shaperenderer.rectLine(gap - 4, Constants.getWINDOW_HEIGTH() - lifebarHeight - 4, lifebarWidth + gap - 4, Constants.getWINDOW_HEIGTH() - lifebarHeight - 4, lifebarHeight);
		shaperenderer.end();

//                System.out.println(playerLifeLabel.getMinWidth());
//                System.out.println("Lifebar: " + lifebarWidth);
	}

	public void drawHUD(int playerHealth) {
		//Make sure to draw first the lifebar
		playerLife = playerHealth;
		drawLifebar();
		timer();
		stage.draw();
	}

	public void drawActionbar(SpriteBatch batch, Main.elementType currentElement) {

		batch.begin();
		batch.draw(actionBar, (Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2), 0);
		batch.end();
		shaperenderer.setAutoShapeType(true);
		shaperenderer.begin();
		//First Spell
		if (currentElement == Main.elementType.WATER) {
			shaperenderer.setColor(Color.BLUE);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 8, 8, 32, 32);
			shaperenderer.setColor(Color.CYAN);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 9, 9, 30, 30);
		} //Second Spell
		else if (currentElement == Main.elementType.FIRE) {
			shaperenderer.setColor(Color.RED);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 16 + 32, 8, 32, 32);
			shaperenderer.setColor(Color.SALMON);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 18 + 31, 9, 30, 30);
		} //Third Spell
		else if (currentElement == Main.elementType.EARTH) {
			shaperenderer.setColor(Color.GREEN);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 24 + 64, 8, 32, 32);
			shaperenderer.setColor(Color.CHARTREUSE);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 27 + 62, 9, 30, 30);
		} //Fourth Spell
		else if (currentElement == Main.elementType.AIR) {
			shaperenderer.setColor(Color.YELLOW);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 32 + 96, 8, 32, 32);
			shaperenderer.setColor(Color.WHITE);
			shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 36 + 93, 9, 30, 30);
		}
		shaperenderer.end();

	}

	public void drawCooldown(Main.elementType currentElement) {
		tableBottom = new Table();
		tableBottom.bottom();
		tableBottom.setFillParent(true);
		tableBottom.padBottom(14);
		tableBottom.add(cooldownLabel1).width(10).padLeft(14).padRight(14);
		tableBottom.add(cooldownLabel2).width(10).padLeft(14).padRight(14);
		tableBottom.add(cooldownLabel3).width(10).padLeft(14).padRight(14);
		tableBottom.add(cooldownLabel4).width(10).padLeft(14).padRight(14);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
		shaperenderer.setColor(grey);
		if (Main.cooldownReady == false) {
			timeValueElements = 0;
			cooldownValue = 3;
			Main.cooldownReady = true;
		}
		if (TimeUtils.timeSinceNanos(startTimeElements) > 1000000000) {//Every second
			if (timeValueElements < 3) {//3 Seconds
				timeValueElements++;
				cooldownValue--;
			}
			startTimeElements = TimeUtils.nanoTime();
		}
		/*  if () {
		 // cooldownReady = true;
           
		 }*/
		if (cooldownValue <= 0) {
			emptyLabel();
		} else {
			switch (currentElement) {
				case WATER:
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 7 + 40, 7, 113, 33);
					cooldownLabel1.setText("");
					cooldownLabel2.setText(String.format("%01d", cooldownValue));
					cooldownLabel3.setText(String.format("%01d", cooldownValue));
					cooldownLabel4.setText(String.format("%01d", cooldownValue));
					break;
				case FIRE:
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 7, 7, 33, 33);
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 87, 7, 73, 33);
					cooldownLabel1.setText(String.format("%01d", cooldownValue));
					cooldownLabel2.setText("");
					cooldownLabel3.setText(String.format("%01d", cooldownValue));
					cooldownLabel4.setText(String.format("%01d", cooldownValue));

					break;
				case EARTH:
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 7, 7, 73, 33);
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 127, 7, 33, 33);
					cooldownLabel1.setText(String.format("%01d", cooldownValue));
					cooldownLabel2.setText(String.format("%01d", cooldownValue));
					cooldownLabel3.setText("");
					cooldownLabel4.setText(String.format("%01d", cooldownValue));

					break;
				case AIR:
					shaperenderer.rect(Constants.WINDOW_WIDTH / 2 - actionBar.getWidth() / 2 + 7, 7, 113, 33);
					cooldownLabel1.setText(String.format("%01d", cooldownValue));
					cooldownLabel2.setText(String.format("%01d", cooldownValue));
					cooldownLabel3.setText(String.format("%01d", cooldownValue));
					cooldownLabel4.setText("");
					break;
			}
		}
		shaperenderer.end();
		stage.addActor(tableBottom);
		stage.draw();

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void addScore(int value) {
		scoreValue += value;
		scoreValueLabel.setText(String.format("%06d", scoreValue));
	}

	public void timer() {
		if (TimeUtils.timeSinceNanos(startTime) > 1000000000) {//Every second
			if (timeValue < 86400) {//1day 
				timeValue++;
				seconds = timeValue % 60;
				minutes = (timeValue / 60) % 60;
				hours = (timeValue / 60 / 60) % 60;
				timeValueLabel.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
			} else if (timeValue == 86400) {
//                timeValue= 0;
				timeValueLabel.setText("over 9000");
			}
			startTime = TimeUtils.nanoTime();
		}
	}

	private void emptyLabel() {
		cooldownLabel1.setText("");
		cooldownLabel2.setText("");
		cooldownLabel3.setText("");
		cooldownLabel4.setText("");
	}

	/*
	 public void setCooldownReady(Boolean cooldownReady) {
	 this.cooldownReady = cooldownReady;
	 }*/
	public int getScoreValue() {
		return scoreValue;
	}
}
