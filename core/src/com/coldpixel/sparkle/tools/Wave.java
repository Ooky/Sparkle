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
 * @author Creat-if
 */
public class Wave {

    private long startTime = 0;
    private int timeValue = 0;
    private int increaseSpawnTime = 2;
    private Random rand = new Random();
    private ArrayList<Soldier> soldiers = new ArrayList<Soldier>();
    private Soldier randomSoldier;
    private int[] rndNumbers;
    private static int arrPos = 0;

    public Wave(PlayScreen screen, Player player) {
        startTime = TimeUtils.nanoTime();
        rndNumbers = new int[30];
        for (int i = 0; i < rndNumbers.length; i++) {
            rndNumbers[i] = getRandomNumber();
        }
        for (int i = 0; i < 30; i++) {
            addEnemy(screen, player);
        }
        screen.setSoldiers(soldiers);

    }

    public void update(PlayScreen screen) {
        if (TimeUtils.timeSinceNanos(startTime) > 1000000000) {//Every second
            timeValue++;
            if (timeValue >= (1 + increaseSpawnTime)) {
                screen.spawnSoldiers(increaseSpawnTime);
                increaseSpawnTime += 2;
                timeValue = 0;
            }
            startTime = TimeUtils.nanoTime();
        }
    }

    private void addEnemy(PlayScreen screen, Player player) {
        Main.elementType element;
        switch (rndNumbers[arrPos]) {
            case 0:
                element = Main.elementType.AIR;
                break;
            case 1:
                element = Main.elementType.EARTH;
                break;
            case 2:
                element = Main.elementType.FIRE;
                break;
            case 3:
                element = Main.elementType.WATER;
                break;
            default:
                element = Main.elementType.AIR;
                break;
        }
        if (arrPos < (rndNumbers.length - 1)) {
            arrPos += 1;
        } else {
            arrPos = 0;
        }
        switch (rndNumbers[arrPos]) {
            case 0:
                randomSoldier = new Soldier(screen, 50 / Main.PPM, 50 / Main.PPM, player, element);
                break;
            case 1:
                randomSoldier = new Soldier(screen, 50 / Main.PPM, 680 / Main.PPM, player, element);
                break;
            case 2:
                randomSoldier = new Soldier(screen, 1100 / Main.PPM, 50 / Main.PPM, player, element);
                break;
            case 3:
                randomSoldier = new Soldier(screen, 1100 / Main.PPM, 680 / Main.PPM, player, element);
                break;
        }
        randomSoldier.b2Body.setActive(false);
        soldiers.add(randomSoldier);
    }

    public void setSoldierRandomPosition(Soldier soldier) {
        if (arrPos < (rndNumbers.length - 1)) {
            arrPos += 1;
        } else {
            arrPos = 0;
        }
        switch (rndNumbers[arrPos]) {
            case 0:
                soldier.b2Body.setTransform(50 / Main.PPM, 50 / Main.PPM, soldier.b2Body.getAngle());
                break;
            case 1:
                soldier.b2Body.setTransform(50 / Main.PPM, 680 / Main.PPM, soldier.b2Body.getAngle());
                break;
            case 2:
                soldier.b2Body.setTransform(1100 / Main.PPM, 50 / Main.PPM, soldier.b2Body.getAngle());
                break;
            case 3:
                soldier.b2Body.setTransform(1100 / Main.PPM, 680 / Main.PPM, soldier.b2Body.getAngle());
                break;
        }
    }

    private int getRandomNumber() {
        return rand.nextInt(4);
    }

    public void clearArray() {
        soldiers.clear();
    }
}
