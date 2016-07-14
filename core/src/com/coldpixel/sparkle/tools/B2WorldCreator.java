package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.sprites.Dirt;
import com.coldpixel.sparkle.sprites.Grass;

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

        //Create hitbox
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bDef);

            polygonShape.setAsBox((rect.getWidth() / 2) / Main.PPM, (rect.getHeight() / 2) / Main.PPM);
            fDef.shape = polygonShape;
            body.createFixture(fDef);
        }
    }
}
