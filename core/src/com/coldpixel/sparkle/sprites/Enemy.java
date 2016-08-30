/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author mike
 */
public abstract class Enemy extends Sprite{
    protected World world;
    protected PlayScreen screen;
    public Body b2Body;
    protected int health;
    
    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
    }
    
    public void decreaseHealth(int decrease){
        health -= decrease;
        System.out.println(health);
    }
    
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        for (Fixture fixture : b2Body.getFixtureList()) {
            fixture.setFilterData(filter); 
        }    
    }
    
    protected abstract void defineEnemy();
    
    public int getHealth(){
        return health;
    }
    
}
