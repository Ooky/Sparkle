package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author Creat-if
 */
public abstract class InteractiveTileObject {

//==============================================================================
//Initialization
//==============================================================================   
    protected World world;
    protected TiledMapTile tile;
    protected Body body;

    //Grass & Dirt
    private BodyDef bDef;
    private FixtureDef fDef;
    private PolygonShape polygonShape;

//==============================================================================
//Methods
//==============================================================================
    public InteractiveTileObject(PlayScreen playScreen, Rectangle bounds) {
        this.world = playScreen.getWorld();

        //Used for Grass & Dirt
        bDef = new BodyDef();
        fDef = new FixtureDef();
        polygonShape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / Main.PPM, (bounds.getY() + bounds.getHeight() / 2) / Main.PPM);

        body = playScreen.getWorld().createBody(bDef);

        polygonShape.setAsBox((bounds.getWidth() / 2) / Main.PPM, (bounds.getHeight() / 2) / Main.PPM);
        fDef.shape = polygonShape;
        body.createFixture(fDef);

    }
}
