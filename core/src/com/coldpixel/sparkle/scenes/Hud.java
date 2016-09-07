package com.coldpixel.sparkle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private final Table table;

    private int playerLife;
    private int timeValue;
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

    private BitmapFont font;

    private long startTime = 0;

//==============================================================================
//Methods
//==============================================================================
    public Hud(SpriteBatch sb) {
        maxLife = 100;
        playerLife = 100;
        scoreValue = 57;
        timeValue = 86390;
        seconds = 0;
        minutes = 0;
        hours = 0;

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

        table = new Table();
        table.top();
        table.setFillParent(true);//=size of the stage
        playerLifeLabel = new Label(String.format("%03d", playerLife) + " / " + maxLife, new Label.LabelStyle(new BitmapFont(), Color.BLACK)); 
        //scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeValueLabel = new Label("00:00:00", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreValueLabel = new Label(String.format("%06d", scoreValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

//        table.setDebug(true);
        table.drawDebug(shaperenderer);
        table.add(playerLifeLabel).left().padTop(gap).padLeft(Gdx.graphics.getWidth() / 10);
        table.add(timeValueLabel).expandX().padTop(gap);
//        table.add(scoreLabel).right().padTop(gap).padRight(gap);
        table.add(scoreValueLabel).right().padTop(gap).padRight(gap);
        table.row();
//        table.add();
//        table.add();
//        table.add(scoreValueLabel).right().padRight(gap);
        stage.addActor(table);

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
        shaperenderer.rectLine(gap - 4, Constants.getWINDOW_HEIGTH()-lifebarHeight-4, lifebarWidth+gap-4, Constants.getWINDOW_HEIGTH()-lifebarHeight-4, lifebarHeight);
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

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void addScore(int value) {
        scoreValue += value;
        scoreValueLabel.setText(String.format("&06d", scoreValue));
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
}

//old
//    public Hud(SpriteBatch sb) {
//        maxLife = 100;
//        playerLife = 92;
//        scoreValue = 57;
//        timeValue = 0;
//        gap = Gdx.graphics.getHeight() / 48;
//
//        shaperenderer = new ShapeRenderer();
//        lifebarWidth = Gdx.graphics.getWidth() / 8;
//        lifebarHeight = 20;
//
//        viewport = new StretchViewport(Constants.getWINDOW_WIDTH(), Constants.getWINDOW_HEIGTH(), new OrthographicCamera());
//        stage = new Stage(viewport, sb);
//
//        table = new Table();
//        table.top();
//        table.setFillParent(true);//=size of the stage
//        playerLifeLabel = new Label(String.format("%03d", playerLife) + " / " + maxLife, new Label.LabelStyle(new BitmapFont(), Color.BLACK)); //4 numbers
//        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
//        timeValueLabel = new Label(String.format("%04d", timeValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//        scoreValueLabel = new Label(String.format("%06d", scoreValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//
////        table.setDebug(true);
//        table.drawDebug(shaperenderer);
//        table.add(playerLifeLabel).left().padTop(gap).padLeft(Gdx.graphics.getWidth()/10);
//        table.add(timeLabel).expandX().padTop(gap);
//        table.add(scoreLabel).right().padTop(gap).padRight(gap);
//        table.row();
//        table.add();
//        table.add(timeValueLabel);
//        table.add(scoreValueLabel).right().padRight(gap);
//        stage.addActor(table);
//    }
//
//    public void drawLifebar() {
//        shaperenderer.setProjectionMatrix(stage.getCamera().combined);
//        shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shaperenderer.setColor(Color.GREEN);
//        shaperenderer.rect(gap-4, 
//               playerLifeLabel.getY(),
//                lifebarWidth, lifebarHeight);
//        shaperenderer.end();
//        
//                System.out.println(playerLifeLabel.getMinWidth());
//                System.out.println("Lifebar: " + lifebarWidth);
//
//    }
