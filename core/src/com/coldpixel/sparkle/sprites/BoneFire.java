/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author MikeSSD
 */
public class BoneFire extends Sprite {
    private float posX;
    private float posY;
    private float width;
    private Color color;
    private Ellipse e;
    private Body b2Body;
    private World world;
    
    public BoneFire(EllipseMapObject ellipse, World world) {
        this.world = world;
        e = ellipse.getEllipse();
        posX = e.x;
        posY = e.y;
        width = e.width;
        defineBoneFire();
    }

    public void defineBoneFire() {
        BodyDef bDef = new BodyDef();
        bDef.position.set((posX+width/2) / Main.PPM, (posY+width/2) / Main.PPM);
        bDef.type = BodyDef.BodyType.StaticBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(width / 2 / Main.PPM, width / 2 / Main.PPM); //Starts from the center
        
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef);
    }
    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getWidth() {
        return width;
    } 
    
    public Color getColor(){
        return color;
    }
    
    public Body getBody(){
        return b2Body;
    }
}
