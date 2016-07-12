package com.coldpixel.sparkle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldpixel.sparkle.screens.PlayScreen;

public class Main extends Game {
    
//==============================================================================
//Initialization
//==============================================================================   
    public SpriteBatch batch;
    public static final int V_WIDTH =Constants.WINDOW_WIDTH;
    public static final int V_HEIGHT=Constants.WINDOW_HEIGTH;
    public static final float PPM = Constants.PPM; //PixelPerMeter

//==============================================================================
//Methods
//==============================================================================
    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render(); //Delegate the render-methode to the active screen
    }
}
