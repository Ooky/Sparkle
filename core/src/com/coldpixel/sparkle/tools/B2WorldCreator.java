package com.coldpixel.sparkle.tools;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.sprites.BoneFire;
import com.coldpixel.sparkle.sprites.Dirt;
import com.coldpixel.sparkle.sprites.EnvironmentObject;
import com.coldpixel.sparkle.sprites.Grass;
import java.util.ArrayList;

/**
 *
 * @author Coldpixel
 */
public class B2WorldCreator {

//==============================================================================
//Initialization
//============================================================================== 
    BodyDef bDef;
    PolygonShape polygonShape;
    FixtureDef fDef;
    Body body;

    Grass grass;
    Dirt dirt;
    EnvironmentObject environmentObject;
    ArrayList<BoneFire> bonefires;
    private BoneFire boneFire;
//==============================================================================
//Methods
//==============================================================================  
    public B2WorldCreator(World world, TiledMap map) {        
        bDef = new BodyDef();
        polygonShape = new PolygonShape();
        fDef = new FixtureDef();

        //Create Grass
        for (MapObject object : map.getLayers().get(0).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            grass = new Grass(world, map, rect);
        }

        //Create Dirt
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            dirt = new Dirt(world, map, rect);
        }
        
        //Create Object
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            environmentObject = new EnvironmentObject(world, map, rect);
        }

        //Create hitbox
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bDef);

            polygonShape.setAsBox((rect.getWidth() / 2) / Main.PPM, (rect.getHeight() / 2) / Main.PPM);
            fDef.shape = polygonShape;
            body.createFixture(fDef);
        }
        
        //Create light
        bonefires = new ArrayList<BoneFire>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(EllipseMapObject.class)) { 
            boneFire = new BoneFire(((EllipseMapObject) object), world);
            bonefires.add(boneFire);
        }
    }
    
    public ArrayList<BoneFire> getBoneFires(){
        return bonefires;
    }
}
