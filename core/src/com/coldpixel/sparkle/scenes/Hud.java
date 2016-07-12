package com.coldpixel.sparkle.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public class Hud {

//==============================================================================
//Initialization
//==============================================================================   
    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private final Viewport viewport;
    private final Table table;

    private int playerLife;
    private int timeValue;
    private int scoreValue;
    private int maxLife;
    private final float gap;

    private final ShapeRenderer shaperenderer;
    private final float lifebarWidth;
    private final float lifebarHeight;

    Label playerLifeLabel;
    Label timeLabel;
    Label timeValueLabel;
    Label scoreLabel;
    Label scoreValueLabel;

    private BitmapFont font;

//==============================================================================
//Methods
//==============================================================================
    public Hud(SpriteBatch sb) {
        maxLife = 100;
        playerLife = 92;
        scoreValue = 57;
        timeValue = 0;
        gap = Gdx.graphics.getHeight() / 48;
        shaperenderer = new ShapeRenderer();
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
        playerLifeLabel = new Label(String.format("%03d", playerLife) + " / " + maxLife, new Label.LabelStyle(new BitmapFont(), Color.BLACK)); //4 numbers
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeValueLabel = new Label(String.format("%04d", timeValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreValueLabel = new Label(String.format("%06d", scoreValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

//        table.setDebug(true);
        table.drawDebug(shaperenderer);
        table.add(playerLifeLabel).left().padTop(gap).padLeft(Gdx.graphics.getWidth() / 10);
        table.add(timeLabel).expandX().padTop(gap);
        table.add(scoreLabel).right().padTop(gap).padRight(gap);
        table.row();
        table.add();
        table.add(timeValueLabel);
        table.add(scoreValueLabel).right().padRight(gap);
        stage.addActor(table);
    }

    public void drawLifebar() {
        shaperenderer.setProjectionMatrix(stage.getCamera().combined);
        shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        shaperenderer.setColor(Color.GREEN);
        shaperenderer.rect(gap - 4,//x
                playerLifeLabel.getY(),//y
                lifebarWidth, lifebarHeight);//width,height
        shaperenderer.end();

//                System.out.println(playerLifeLabel.getMinWidth());
//                System.out.println("Lifebar: " + lifebarWidth);

    }

    public void drawHUD() {
        //Make sure to draw first the lifebar
        drawLifebar();
        stage.draw();
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
