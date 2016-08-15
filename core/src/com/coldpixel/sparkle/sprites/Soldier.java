/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author mike
 */
public class Soldier extends Enemy{
    //animation
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    
    private float x;
    private float y;
    private int soldierWidth = 48;
    private int soldierHeight = 64;
        
    public Soldier(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i<2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), soldierWidth / Main.PPM, soldierHeight / Main.PPM);
    }    
    
    public void update(float dt){
        stateTime += dt;
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }
    
    protected void defineEnemy(){
        BodyDef bDef = new BodyDef();
        bDef.position.set(x / Main.PPM, y / Main.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(soldierWidth / 2 / Main.PPM, soldierHeight / 2 / Main.PPM); //Starts from the center

        fDef.shape = rectangleShape;
        fDef.filter.categoryBits = Main.ENEMY_BIT;
        fDef.filter.maskBits = Main.GROUND_BIT |
                Main.BONFIRE_BIT |
                Main.PLAYER_BIT |
                Main.ENEMY_BIT |
                Main.OBJECT_BIT;
        b2Body.createFixture(fDef);
    }
    
}
