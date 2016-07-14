package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Coldpixel
 */
public class Dirt extends InteractiveTileObject {

    public Dirt(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }

}
