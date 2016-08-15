package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 * 
 * @author Coldpixel
 */
public class Grass extends InteractiveTileObject{
    
    public Grass(PlayScreen playScreen, Rectangle bounds) {
        super(playScreen, bounds);
    }

}
