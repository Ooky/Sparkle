/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.utils.TimeUtils;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.sprites.Player;
import com.coldpixel.sparkle.sprites.Soldier;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mike
 */
public class Wave {

	private long startTime = 0;
	private int timeValue = 0;
	private int increaseSpawnTime = 0;
	private PlayScreen screen;
	private	Random rand = new Random();
	private Player player;
	
	public Wave(PlayScreen pl, Player player) {
		screen = pl;
		this.player = player;
		startTime = TimeUtils.nanoTime();
	}

	public void update() {
		if (TimeUtils.timeSinceNanos(startTime) > 1000000000) {//Every second
			if (timeValue < (10 + increaseSpawnTime)) {
				timeValue++;
			} else if (timeValue >= (10 + increaseSpawnTime)) {
				spawnEnemys();
				increaseSpawnTime += 2;
				timeValue = 0;
			}
			startTime = TimeUtils.nanoTime();
		}
	}
	
	private void spawnEnemys(){
		ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
		for(int i = 0; i < (3 + increaseSpawnTime); i++){
			Main.elementType element;
			switch(getRandomNumber(1, 4)){
				case 1:
					element = Main.elementType.AIR;
					break;
				case 2:
					element = Main.elementType.EARTH;
					break;
				case 3:
					element = Main.elementType.FIRE;
					break;
				case 4:
					element = Main.elementType.WATER;
					break;
				default:
					element = Main.elementType.AIR;
					break;
			}
			switch(getRandomNumber(1, 4)){
				case 1: 
					soldiers.add(new Soldier(screen, 50/Main.PPM, 50/Main.PPM, player, element));
					break;
				case 2: 
					soldiers.add(new Soldier(screen, 50/Main.PPM, 680/Main.PPM, player, element));
					break;
				case 3: 
					soldiers.add(new Soldier(screen, 1100/Main.PPM, 50/Main.PPM, player, element));
					break;
				case 4: 
					soldiers.add(new Soldier(screen, 1100/Main.PPM, 680/Main.PPM, player, element));
					break;
			}
		}
		screen.addSoldierArray(soldiers);
	}
	
	private int getRandomNumber(int min, int max){
		return rand.nextInt((max - min) + 1) + min;
	}
}
