package com.coldpixel.sparkle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.screens.StartGame;
import com.coldpixel.sparkle.tools.AssetHelper;

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
//Texturen ändern
//code aufräumen
//credits
//paypal
//resize->fuck it
//Fix music(start new game-> go back, start new game
//game over
//start game
//game explanation
//cleanup unused import/variables
//array with end game notes(leave and shame or play again)
//Delete unnecessary comments
//credits geben
//Link should be clickable
