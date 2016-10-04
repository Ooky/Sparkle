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
	private int increaseSpawnTime = 2;
	private PlayScreen screen;
	private	Random rand = new Random();
	private Player player;
	private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
	private ArrayList<Soldier> rubishSoldiers = new ArrayList<Soldier>();
	private Soldier randomSoldier;
	
	public Wave(PlayScreen pl, Player player) {
		screen = pl;
		this.player = player;
		startTime = TimeUtils.nanoTime();
		for(int i = 0; i < 80; i++){
			addEnemy();
		}
	}

	public void update() {
		if (TimeUtils.timeSinceNanos(startTime) > 1000000000) {//Every second
			if (timeValue < (8 + increaseSpawnTime)) {
				/*if(soldiers.size() < increaseSpawnTime){
					addEnemy();
				}*/
				if((soldiers.size() < 3) /*|| (increaseSpawnTime > 10 && soldiers.size() < 20)*/){
					for(int i = 0; i < 30; i++){
						addEnemy();
					}
					//soldiers.addAll(rubishSoldiers);
					//rubishSoldiers.clear();
				}
				timeValue++;
			} else if (timeValue >= (8 + increaseSpawnTime)) {
				spawnEnemys();
				increaseSpawnTime += 2;
				timeValue = 0;
			}
			startTime = TimeUtils.nanoTime();
		}
	}
	
	private void spawnEnemys(){
		/*for(Soldier soldier : soldiers){
			soldier.b2Body.setActive(true);
		}*/
		for(int i = 0; i < increaseSpawnTime; i++){
			soldiers.get(1).b2Body.setActive(true);
			soldiers.get(1).setDestroyed(false);
			screen.addSoldier(soldiers.get(1));
			rubishSoldiers.add(soldiers.get(1));
			soldiers.remove(1);
		}
		//soldiers.clear();
	}
	
	private void addEnemy(){
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
				randomSoldier = new Soldier(screen, 50/Main.PPM, 50/Main.PPM, player, element);
				break;
			case 2: 
				randomSoldier = new Soldier(screen, 50/Main.PPM, 680/Main.PPM , player, element);
				break;
			case 3: 
				randomSoldier = new Soldier(screen, 1100/Main.PPM, 50/Main.PPM, player, element);
				break;
			case 4: 
				randomSoldier = new Soldier(screen, 1100/Main.PPM, 680/Main.PPM, player, element);
				break;
		}
		randomSoldier.b2Body.setActive(false);
		soldiers.add(randomSoldier);
	}
	
	private int getRandomNumber(int min, int max){
		return rand.nextInt((max - min) + 1) + min;
	}
	public void clearArray(){
		soldiers.clear();
	}
}
