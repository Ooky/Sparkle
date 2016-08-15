package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author Coldpixel
 */
public class EnvironmentObject extends InteractiveTileObject {
    
    public EnvironmentObject(PlayScreen playScreen, Rectangle bounds) {
        super(playScreen, bounds);
    }

}
