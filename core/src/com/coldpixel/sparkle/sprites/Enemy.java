package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.scenes.Hud;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author Creat-if
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2Body;
    protected int health;
    protected int previousHealth;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }

    public void decreaseHealth(float decrease) {
        health -= decrease;
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt, Hud hud);

    public int getHealth() {
        return health;
    }
}
