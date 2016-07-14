package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public abstract class InteractiveTileObject {

//==============================================================================
//Initialization
//==============================================================================   
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    //Grass & Dirt
    BodyDef bDef;
    FixtureDef fDef;
    PolygonShape polygonShape;

//==============================================================================
//Methods
//==============================================================================
    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        //Used for Grass & Dirt
        bDef = new BodyDef();
        fDef = new FixtureDef();
        polygonShape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Main.PPM, (bounds.getY() + bounds.getHeight() / 2) / Main.PPM);

        body = world.createBody(bDef);

        polygonShape.setAsBox((bounds.getWidth() / 2) / Main.PPM, (bounds.getHeight() / 2) / Main.PPM);
        fDef.shape = polygonShape;
        body.createFixture(fDef);

    }
}
