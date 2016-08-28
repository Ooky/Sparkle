/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Coldpixel
 */
public class AssetHelper extends AssetManager {

//==============================================================================
//Initialization
//============================================================================== 
    private AssetManager manager;

//==============================================================================
//Methods
//==============================================================================  
    public AssetHelper() {
        manager = new AssetManager();
        manager.load("audio/music/determination.mp3", Music.class);
        manager.load("audio/sounds/fire_1.wav", Sound.class);
        //blocks everything until assets are loaded;
        manager.finishLoading();
    }

//==============================================================================
//Getter
//==============================================================================  
    public AssetManager getManager() {
        return manager;
    }
}
