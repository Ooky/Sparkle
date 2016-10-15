/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Creat-if
 */
public final class AssetHelper extends AssetManager {

//==============================================================================
//Initialization
//============================================================================== 
    private AssetManager manager;
    //Music
    private static Music music;
    private static Sound sound;
    //Sound
    private long soundID;
    private float volume;
    //Shard
    private static Texture allShards = new Texture("Graphics/Attacks/ShardsALL.png");
    private static TextureRegion[] shardAir;
    private static TextureRegion[] shardCollisionAir;
    private static TextureRegion[] shardFire;
    private static TextureRegion[] shardCollisionFire;
    private static TextureRegion[] shardWater;
    private static TextureRegion[] shardCollisionWater;
    private static TextureRegion[] shardEarth;
    private static TextureRegion[] shardCollisionEarth;
    private short numbersOfIceFrames = 8;
    private short shardWidth = 32;
    private short shardHeight = 32;
    private static Array<TextureRegion> shardFrames = new Array<TextureRegion>();
    private static Animation waterShardAnimation;
    private static Animation fireShardAnimation;
    private static Animation earthShardAnimation;
    private static Animation airShardAnimation;
    private static Animation waterCollisionAnimation;
    private static Animation fireCollisionAnimation;
    private static Animation earthCollisionAnimation;
    private static Animation airCollisionAnimation;
    private float shardFlyAnimationSpeed = 0.1f;
    private float shardCollisionAnimationSpeed = .015f;
    //Soldier
    private static Texture textureSoldier = new Texture("Graphics/Enemy/All.png");
    private static TextureRegion[] soldierWalkGreen;
    private static TextureRegion[] soldierAttackGreen;
    private static TextureRegion[] soldierGreenDeathWater;
    private static TextureRegion[] soldierGreenDeathFire;
    private static TextureRegion[] soldierGreenDeathAir;
    private static TextureRegion[] soldierGreenDeathEarth;
    private static TextureRegion[] soldierWalkBlue;
    private static TextureRegion[] soldierAttackBlue;
    private static TextureRegion[] soldierBlueDeathWater;
    private static TextureRegion[] soldierBlueDeathFire;
    private static TextureRegion[] soldierBlueDeathAir;
    private static TextureRegion[] soldierBlueDeathEarth;
    private static TextureRegion[] soldierWalkRed;
    private static TextureRegion[] soldierAttackRed;
    private static TextureRegion[] soldierRedDeathWater;
    private static TextureRegion[] soldierRedDeathFire;
    private static TextureRegion[] soldierRedDeathAir;
    private static TextureRegion[] soldierRedDeathEarth;
    private static TextureRegion[] soldierWalkYellow;
    private static TextureRegion[] soldierAttackYellow;
    private static TextureRegion[] soldierYellowDeathWater;
    private static TextureRegion[] soldierYellowDeathFire;
    private static TextureRegion[] soldierYellowDeathAir;
    private static TextureRegion[] soldierYellowDeathEarth;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation deathAnimation;
    private Animation deathAnimationEarth;
    private Animation deathAnimationFire;
    private Animation deathAnimationWater;
    private Animation deathAnimationAir;
    private Main.elementType element;
    private Array<TextureRegion> soldierFrames = new Array<TextureRegion>();
    private float walkAnimationSpeed = 0.1f;
    private float attackAnimationSpeed = 0.15f;
    private float deathAnimationSpeed = 0.15f;

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
        //Shard
        initializeAllAnimationsShard();
        generateFramesShard();
        //Soldier
//        initializeAllAnimationsSoldier();
    }

    public AssetHelper(Main.elementType element) {
        initializeAllAnimationsSoldier();
        this.element = element;
    }

//Music
//------------------------------------------------------------------------------  
    public void musicBackground(Boolean setLooping) {
        loadMusic();
        music.setLooping(setLooping);
        music.setVolume(volume);
        music.play();
    }

    public void stopMusic() {
        if (music.isPlaying()) {
            music.stop();
            music.dispose();
        }
    }

    public void loadMusic() {
        music = manager.get("audio/music/determination.mp3", Music.class);
    }

//Sound
//------------------------------------------------------------------------------  
    public void soundBonfire(Boolean setLooping) {
        loadSound();
        soundID = sound.play(volume);
        sound.setLooping(soundID, setLooping);
    }

    public void stopSound() {
        sound.stop();
        sound.dispose();
    }

    public void loadSound() {
        sound = manager.get("audio/sounds/fire_1.wav", Sound.class);
    }

//Soldier
//------------------------------------------------------------------------------ 
    private void initializeAllAnimationsSoldier() {
        soldierWalkGreen();
        soldierAttackGreen();
        soldierGreenDeathWater();
        soldierGreenDeathFire();
        soldierGreenDeathAir();
        soldierGreenDeathEarth();
        soldierWalkBlue();
        soldierAttackBlue();
        soldierBlueDeathWater();
        soldierBlueDeathFire();
        soldierBlueDeathAir();
        soldierBlueDeathEarth();
        soldierWalkRed();
        soldierAttackRed();
        soldierRedDeathWater();
        soldierRedDeathFire();
        soldierRedDeathAir();
        soldierRedDeathEarth();
        soldierWalkYellow();
        soldierAttackYellow();
        soldierYellowDeathWater();
        soldierYellowDeathFire();
        soldierYellowDeathAir();
        soldierYellowDeathEarth();
    }

    private TextureRegion[] soldierYellowDeathEarth() {
        soldierYellowDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierYellowDeathEarth[j] = new TextureRegion(textureSoldier, i, 640, 80, 80);
            j++;
        }

        return soldierYellowDeathEarth;
    }

    private TextureRegion[] soldierYellowDeathAir() {
        soldierYellowDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierYellowDeathAir[j] = new TextureRegion(textureSoldier, i, 0, 80, 80);
            j++;
        }
        return soldierYellowDeathAir;
    }

    private TextureRegion[] soldierYellowDeathFire() {
        soldierYellowDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierYellowDeathFire[j] = new TextureRegion(textureSoldier, i, 320, 80, 80);
            j++;
        }
        return soldierYellowDeathFire;
    }

    private TextureRegion[] soldierYellowDeathWater() {
        soldierYellowDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierYellowDeathWater[j] = new TextureRegion(textureSoldier, i, 960, 80, 64);
            j++;
        }

        return soldierYellowDeathWater;
    }

    private TextureRegion[] soldierAttackYellow() {
        soldierAttackYellow = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackYellow[j] = new TextureRegion(textureSoldier, 1152, i, 64, 64);
            j++;
        }
        return soldierAttackYellow;
    }

    private TextureRegion[] soldierWalkYellow() {
        soldierWalkYellow = new TextureRegion[10];
        int j = 0;
        for (int i = 0; i <= 432; i += 48) {
            soldierWalkYellow[j] = new TextureRegion(textureSoldier, i, 1216, 48, 64);
            j++;
        }
        return soldierWalkYellow;
    }

    private TextureRegion[] soldierBlueDeathEarth() {
        soldierBlueDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierBlueDeathEarth[j] = new TextureRegion(textureSoldier, i, 800, 80, 80);
            j++;
        }

        return soldierBlueDeathEarth;
    }

    private TextureRegion[] soldierBlueDeathAir() {
        soldierBlueDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierBlueDeathAir[j] = new TextureRegion(textureSoldier, i, 80, 80, 80);
            j++;
        }
        return soldierBlueDeathAir;
    }

    private TextureRegion[] soldierBlueDeathFire() {
        soldierBlueDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierBlueDeathFire[j] = new TextureRegion(textureSoldier, i, 480, 80, 80);
            j++;
        }
        return soldierBlueDeathFire;
    }

    private TextureRegion[] soldierBlueDeathWater() {
        soldierBlueDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierBlueDeathWater[j] = new TextureRegion(textureSoldier, i, 1024, 80, 64);
            j++;
        }

        return soldierBlueDeathWater;
    }

    private TextureRegion[] soldierAttackBlue() {
        soldierAttackBlue = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackBlue[j] = new TextureRegion(textureSoldier, 1024, i, 64, 64);
            j++;
        }
        return soldierAttackBlue;
    }

    private TextureRegion[] soldierWalkBlue() {
        soldierWalkBlue = new TextureRegion[10];
        int j = 0;
        for (int i = 0; i <= 432; i += 48) {
            soldierWalkBlue[j] = new TextureRegion(textureSoldier, i, 1280, 48, 64);
            j++;
        }
        return soldierWalkBlue;
    }

    private TextureRegion[] soldierRedDeathEarth() {
        soldierRedDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierRedDeathEarth[j] = new TextureRegion(textureSoldier, i, 720, 80, 80);
            j++;
        }
        return soldierRedDeathEarth;
    }

    private TextureRegion[] soldierRedDeathAir() {
        soldierRedDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierRedDeathAir[j] = new TextureRegion(textureSoldier, i, 160, 80, 80);
            j++;
        }
        return soldierRedDeathAir;
    }

    private TextureRegion[] soldierRedDeathFire() {
        soldierRedDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierRedDeathFire[j] = new TextureRegion(textureSoldier, i, 560, 80, 80);
            j++;
        }
        return soldierRedDeathFire;
    }

    private TextureRegion[] soldierRedDeathWater() {
        soldierRedDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierRedDeathWater[j] = new TextureRegion(textureSoldier, i, 1152, 80, 64);
            j++;
        }
        return soldierRedDeathWater;
    }

    private TextureRegion[] soldierAttackRed() {
        soldierAttackRed = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackRed[j] = new TextureRegion(textureSoldier, 1088, i, 64, 64);
            j++;
        }
        return soldierAttackRed;
    }

    private TextureRegion[] soldierWalkRed() {
        soldierWalkRed = new TextureRegion[10];
        int j = 0;
        for (int i = 480; i <= 912; i += 48) {
            soldierWalkRed[j] = new TextureRegion(textureSoldier, i, 1216, 48, 64);
            j++;
        }
        return soldierWalkRed;
    }

    private TextureRegion[] soldierGreenDeathEarth() {
        soldierGreenDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierGreenDeathEarth[j] = new TextureRegion(textureSoldier, i, 880, 80, 80);
            j++;
        }
        return soldierGreenDeathEarth;
    }

    private TextureRegion[] soldierGreenDeathAir() {
        soldierGreenDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierGreenDeathAir[j] = new TextureRegion(textureSoldier, i, 240, 80, 80);
            j++;
        }
        return soldierGreenDeathAir;
    }

    private TextureRegion[] soldierGreenDeathFire() {
        soldierGreenDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierGreenDeathFire[j] = new TextureRegion(textureSoldier, i, 400, 80, 80);
            j++;
        }
        return soldierGreenDeathFire;
    }

    private TextureRegion[] soldierGreenDeathWater() {
        soldierGreenDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierGreenDeathWater[j] = new TextureRegion(textureSoldier, i, 1088, 80, 64);
            j++;
        }
        return soldierGreenDeathWater;
    }

    private TextureRegion[] soldierAttackGreen() {
        soldierAttackGreen = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackGreen[j] = new TextureRegion(textureSoldier, 960, i, 64, 64);
            j++;
        }
        return soldierAttackGreen;
    }

    private TextureRegion[] soldierWalkGreen() {
        soldierWalkGreen = new TextureRegion[10];
        int j = 0;
        for (int i = 640; i <= 1216; i += 64) {
            soldierWalkGreen[j] = new TextureRegion(textureSoldier, 1232, i, 48, 64);
            j++;
        }
        return soldierWalkGreen;
    }

    public void generateFramesSoldier() {

        if (null != element) {
            switch (element) {
                case EARTH:
                    //Earth
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        soldierFrames.add(soldierWalkGreen[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        soldierFrames.add(soldierAttackGreen[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Water
                    for (int i = 0; i < 12; i++) {
                        soldierFrames.add(soldierGreenDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();

                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        soldierFrames.add(soldierGreenDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();

                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        soldierFrames.add(soldierGreenDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();

                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        soldierFrames.add(soldierGreenDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    break;

                case WATER:
                    //Water
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        soldierFrames.add(soldierWalkBlue[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        soldierFrames.add(soldierAttackBlue[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        soldierFrames.add(soldierBlueDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        soldierFrames.add(soldierBlueDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        soldierFrames.add(soldierBlueDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        soldierFrames.add(soldierBlueDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    break;

                case FIRE:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        soldierFrames.add(soldierWalkRed[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        soldierFrames.add(soldierAttackRed[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        soldierFrames.add(soldierRedDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        soldierFrames.add(soldierRedDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        soldierFrames.add(soldierRedDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        soldierFrames.add(soldierRedDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    break;
                case AIR:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        soldierFrames.add(soldierWalkYellow[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        soldierFrames.add(soldierAttackYellow[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        soldierFrames.add(soldierYellowDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        soldierFrames.add(soldierYellowDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        soldierFrames.add(soldierYellowDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        soldierFrames.add(soldierYellowDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, soldierFrames);
                    soldierFrames.clear();
                    break;
            }
        }
    }

    public Animation getWalkAnimation() {
        return walkAnimation;
    }

    public Animation getAttackAnimation() {
        return attackAnimation;
    }

    public Animation getDeathAnimation() {
        return deathAnimation;
    }

    public Animation getDeathAnimationEarth() {
        return deathAnimationEarth;
    }

    public Animation getDeathAnimationFire() {
        return deathAnimationFire;
    }

    public Animation getDeathAnimationWater() {
        return deathAnimationWater;
    }

    public Animation getDeathAnimationAir() {
        return deathAnimationAir;
    }

//Shard
//------------------------------------------------------------------------------  
    public void initializeAllAnimationsShard() {
        shardAir();
        shardCollisionAir();
        shardFire();
        shardCollisionFire();
        shardWater();
        shardCollisionWater();
        shardEarth();
        shardCollisionEarth();
    }

    public void generateFramesShard() {
        createWaterAnimation();
        createAirAnimation();
        createFireAnimation();
        createEarthAnimation();
    }

    private TextureRegion[] shardAir() {
        shardAir = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardAir[j] = new TextureRegion(allShards, i, 0, shardWidth, shardHeight);
            j++;
        }
        return shardAir;
    }

    private TextureRegion[] shardCollisionAir() {
        shardCollisionAir = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardCollisionAir[j] = new TextureRegion(allShards, i, 128, shardWidth, shardHeight);
            j++;
        }
        return shardCollisionAir;
    }

    private TextureRegion[] shardFire() {
        shardFire = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardFire[j] = new TextureRegion(allShards, i, 64, shardWidth, shardHeight);
            j++;
        }
        return shardFire;
    }

    private TextureRegion[] shardCollisionFire() {
        shardCollisionFire = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardCollisionFire[j] = new TextureRegion(allShards, i, 192, shardWidth, shardHeight);
            j++;
        }
        return shardCollisionFire;
    }

    private TextureRegion[] shardWater() {
        shardWater = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardWater[j] = new TextureRegion(allShards, i, 32, shardWidth, shardHeight);
            j++;
        }
        return shardWater;
    }

    private TextureRegion[] shardCollisionWater() {
        shardCollisionWater = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardCollisionWater[j] = new TextureRegion(allShards, i, 160, shardWidth, shardHeight);
            j++;
        }
        return shardCollisionWater;
    }

    private TextureRegion[] shardEarth() {
        shardEarth = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardEarth[j] = new TextureRegion(allShards, i, 98, shardWidth, shardHeight);
            j++;
        }
        return shardEarth;
    }

    private TextureRegion[] shardCollisionEarth() {
        shardCollisionEarth = new TextureRegion[numbersOfIceFrames];
        int j = 0;
        for (int i = 0; i <= 224; i += shardWidth) {
            shardCollisionEarth[j] = new TextureRegion(allShards, i, 224, shardWidth, shardHeight);
            j++;
        }
        return shardCollisionEarth;
    }

    private void createWaterAnimation() {
        //fly Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardWater[i]);
        }
        waterShardAnimation = new Animation(shardFlyAnimationSpeed, shardFrames);
        shardFrames.clear();
        //Collision Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardCollisionWater[i]);
        }
        waterCollisionAnimation = new Animation(shardCollisionAnimationSpeed, shardFrames);
        shardFrames.clear();
    }

    private void createFireAnimation() {
        //fly Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardFire[i]);
        }
        fireShardAnimation = new Animation(shardFlyAnimationSpeed, shardFrames);
        shardFrames.clear();
        //Collision Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardCollisionFire[i]);
        }
        fireCollisionAnimation = new Animation(shardCollisionAnimationSpeed, shardFrames);
        shardFrames.clear();
    }

    private void createEarthAnimation() {
        //fly Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardEarth[i]);
        }
        earthShardAnimation = new Animation(shardFlyAnimationSpeed, shardFrames);
        shardFrames.clear();
        //Collision Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardCollisionEarth[i]);
        }
        earthCollisionAnimation = new Animation(shardCollisionAnimationSpeed, shardFrames);
        shardFrames.clear();
    }

    private void createAirAnimation() {
        //fly Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardAir[i]);
        }
        airShardAnimation = new Animation(shardFlyAnimationSpeed, shardFrames);
        shardFrames.clear();
        //Collision Animation
        for (int i = 0; i < numbersOfIceFrames; i++) {
            shardFrames.add(shardCollisionAir[i]);
        }
        airCollisionAnimation = new Animation(shardCollisionAnimationSpeed, shardFrames);
        shardFrames.clear();
    }

    public Animation getWaterShardAnimation() {
        return waterShardAnimation;
    }

    public Animation getFireShardAnimation() {
        return fireShardAnimation;
    }

    public Animation getEarthShardAnimation() {
        return earthShardAnimation;
    }

    public Animation getAirShardAnimation() {
        return airShardAnimation;
    }

    public Animation getWaterCollisionAnimation() {
        return waterCollisionAnimation;
    }

    public Animation getFireCollisionAnimation() {
        return fireCollisionAnimation;
    }

    public Animation getEarthCollisionAnimation() {
        return earthCollisionAnimation;
    }

    public Animation getAirCollisionAnimation() {
        return airCollisionAnimation;
    }

}
