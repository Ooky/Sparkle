package com.coldpixel.sparkle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldpixel.sparkle.screens.StartGame;
import com.coldpixel.sparkle.tools.AssetHelper;

/**
 *
 * @author Creat-if
 */
public class Main extends Game {

//==============================================================================
//Initialization
//==============================================================================   
    public SpriteBatch batch;
    public static final int V_WIDTH = Constants.WINDOW_WIDTH;
    public static final int V_HEIGHT = Constants.WINDOW_HEIGTH;
    public static final float PPM = Constants.PPM; //PixelPerMeter

    public static final short GROUND_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short BONFIRE_BIT = 4;
    public static final short DESTROYED_BIT = 8;
    public static final short OBJECT_BIT = 16;
    public static final short ENEMY_BIT = 32;
    public static final short ENEMYMELEEATTACK_BIT = 64;
    public static final short PLAYERATTACK_BIT = 128;
    public static final short CRYSTAL_BIT = 256;
    public static Boolean cooldownReady = true;

    public static enum elementType {

        WATER, FIRE, EARTH, AIR, NONE
    }
    private AssetHelper assetHelper;

//==============================================================================
//Methods
//==============================================================================
    @Override
    public void create() {
        batch = new SpriteBatch();
        assetHelper = new AssetHelper();
        setScreen(new StartGame(this));
    }

    @Override
    public void render() {
        super.render(); //Delegate the render-methode to the active screen
        assetHelper.dispose();
    }
}

//ToDo DISPOSE shit
//.xcf aufräumen (gimp, paint dot net)
//cleanup code
//paypal
//game over probably better
//Delete unnecessary comments
//Button should link to a paypal account
//Remove unecessary images
//pack images
//assetmanager?
//Credits->How to play->image with fire->green->air->water->fire..
//Change icon(top left corner, CONSTANTS)
//change as much methods from public to private
//Tests if bugs occur
//Add HIT-Sounds?

//CHECK CREDITS->EMAIL?
//CHECK CREDITS->CopyRight?

//Spawn more enemys? more enemys at start array?
//player->generateFrames->cleanup!  //same with shard
//Check enemy line: 103, 104,56, 29
//day'night cycle->maybe change value? or switch it with a key instead of timer?