/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author MikeSSD
 */
public class BonFire extends Sprite {
    private float posX;
    private float posY;
    private float width;
    private Color color;
    private Ellipse e;
    private Body b2Body;
    private World world;
    private Animation burning;
    private float stateTimer;
    Array<TextureRegion> frames;
    private TextureRegion fireBurning;
    private PointLight pointLight;
    private float currentDistance;
    private float previousDistance;

    
    public BonFire(EllipseMapObject ellipse, World world) {
        this.world = world;
        e = ellipse.getEllipse();
        posX = e.x;
        posY = e.y;
        width = e.width;
        currentDistance = width/Main.PPM*6;
        previousDistance = currentDistance;
        defineBonFire();
        
        fireBurning = new TextureRegion(new Texture("Graphics/Terrain/BonFire.png"), 0, 0, 32, 64);
        setBounds(0, 0, 64/Main.PPM, 128/Main.PPM);
        setRegion(fireBurning);
        setPosition(b2Body.getPosition().x - (64/2/Main.PPM), b2Body.getPosition().y - (64/2/Main.PPM));
        stateTimer = 0;
        frames = new Array<TextureRegion>();
        //STANDING
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Terrain/BonFire.png"), i * 32, 0, 32, 64));
        }
        burning = new Animation(0.16f, frames, LOOP);
    }

    public void defineBonFire() {
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
    
    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = burning.getKeyFrame(stateTimer);
        stateTimer = stateTimer + dt;
        return region;
    }
    
    public void update(float dt) {
        setRegion(getFrame(dt));
        //light rays extend and shorten
        if(previousDistance >= width/Main.PPM*6){
            currentDistance -= 1/Main.PPM;
            if(currentDistance < width/Main.PPM*5)
                previousDistance = currentDistance;
        }else if(previousDistance <= width/Main.PPM*5){
            currentDistance += 1/Main.PPM;
            if(currentDistance > width/Main.PPM*6)
                previousDistance = currentDistance;
        }
        pointLight.setDistance(currentDistance);
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
    
   public void setPointLight(PointLight pLight){
       pointLight = pLight;
   }
}
