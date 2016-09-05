/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author mike
 */
public class Crystal extends Sprite {


    private float posX;
    private float posY;
    private int width;
    private int height;
    private float radius;
    private Body b2Body;
    private World world;
    private Animation rotating;
    private float stateTimer;
    Array<TextureRegion> frames;
    
    //provisory until we get an animation
    private TextureRegion crystal;

    public Crystal(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        posX = x;
        posY = y;
        width = 32;
        height = 80;
        radius = 250 / Main.PPM;
        defineCrystal();
        
        //###########################################################
        //provisory until we get an animation
        crystal = new TextureRegion(new Texture("Graphics/Crystal/Source.png"), 0, 0, width, height);
        //############################################################
        setBounds(0, 0, width / Main.PPM , height / Main.PPM);
        setRegion(crystal);
        setPosition(b2Body.getPosition().x - (width / 2 / Main.PPM), b2Body.getPosition().y - (height / 2 / Main.PPM));
        /* Prepared for animation
        stateTimer = 0;
        frames = new Array<TextureRegion>();
        //Animation
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Crystal/rotating.png"), i * 32, 0, 32, 80));
        }
        rotating = new Animation(1, frames);*/
    }

    public void defineCrystal() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(posX / Main.PPM, posY / Main.PPM);
        bDef.type = BodyDef.BodyType.StaticBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(width / 2 / Main.PPM, height / 2 / Main.PPM);

        fDef.filter.categoryBits = Main.OBJECT_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef);
        
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        fDef.filter.categoryBits = Main.CRYSTAL_BIT;
        fDef.filter.maskBits = Main.PLAYER_BIT;
        fDef.shape = circleShape;
        fDef.isSensor = true;
        b2Body.createFixture(fDef);
    }

    public void update(float dt) {
        //prepared for animation
        //setRegion(rotating.getKeyFrame(dt));
    }

}


