package com.coldpixel.sparkle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    
    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short BONFIRE_BIT = 4;
    public static final short DESTROYED_BIT = 8;
    public static final short OBJECT_BIT = 16;
    public static final short ENEMY_BIT = 3;
    
    private AssetManager manager;
//==============================================================================
//Methods
//==============================================================================
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("audio/music/determination.mp3", Music.class);
        manager.load("audio/sounds/fire_1.wav", Sound.class);
        //blocks everything until assets are loaded;
        manager.finishLoading();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render(); //Delegate the render-methode to the active screen
        manager.update();
    }

    public AssetManager getManager() {
        return manager;
    }    
}
