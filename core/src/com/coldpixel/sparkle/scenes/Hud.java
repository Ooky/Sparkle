package com.coldpixel.sparkle.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Constants;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
    private Viewport viewport;

    private int playerLife;
    private int timeValue;
    private int scoreValue;
    private int maxLife;

    Label playerLifeLabel;
    Label timeLabel;
    Label timeValueLabel;
    Label scoreLabel;
    Label scoreValueLabel;

//==============================================================================
//Methods
//==============================================================================
    public Hud(SpriteBatch sb) {
        maxLife = 100;
        playerLife = 92;
        scoreValue = 57;
        timeValue = 0;

        viewport = new StretchViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGTH, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);//=size of the stage

        playerLifeLabel = new Label(String.format("%03d", playerLife) + " / " + maxLife, new Label.LabelStyle(new BitmapFont(), Color.GREEN)); //4 numbers,)
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.RED));
        scoreLabel = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        timeValueLabel = new Label(String.format("%04d", timeValue), new Label.LabelStyle(new BitmapFont(), Color.RED)); //4 numbers,
        scoreValueLabel = new Label(String.format("%06d", scoreValue), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); //6 numbers

//        table.setDebug(true);
        table.add(playerLifeLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.row();
        table.add();
        table.add(timeValueLabel);
        table.add(scoreValueLabel).expandX();

        stage.addActor(table);

    }
}
