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
    private Music music;
    private Sound sound;
    private long soundID;
    private float volume;

//==============================================================================
//Methods
//==============================================================================  
    public AssetHelper() {
        manager = new AssetManager();
        manager.load("audio/music/determination.mp3", Music.class);
        manager.load("audio/sounds/fire_1.wav", Sound.class);
        //blocks everything until assets are loaded;
        manager.finishLoading();
        volume = 0.2f;
    }

    public void musicBackground(Boolean setLooping) {
        loadMusic();
        music.setLooping(setLooping);
        music.setVolume(volume);
        music.play();
    }

    public void soundBonfire(Boolean setLooping) {
        loadSound();
        soundID = sound.play(volume);
        sound.setLooping(soundID, setLooping);
    }

    public void stopMusic() {
        if (music.isPlaying()) {
            music.stop();
            music.dispose();
        }
    }

    public void stopSound() {
        sound.stop();
        sound.dispose();
    }

    public void loadMusic() {
        music = manager.get("audio/music/determination.mp3", Music.class);
    }

    public void loadSound() {
        sound = manager.get("audio/sounds/fire_1.wav", Sound.class);
    }
}
