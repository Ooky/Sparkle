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
