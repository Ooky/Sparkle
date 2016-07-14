package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 
 * @author Coldpixel
 */
public class Grass extends InteractiveTileObject{
    
    public Grass(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

}
