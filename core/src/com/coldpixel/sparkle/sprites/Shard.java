package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.tools.AssetHelper;

/**
 *
 * @author Creat-if
 */
public final class Shard extends Sprite {

    private int width;
    private int height;
    private float x;
    private float y;
    private World world;
    private Body b2Body;
    private Player.shootDirection shootDirection;
    private int damage;
    private Boolean setToDestroy;
    private Boolean destroyed;
    private short velocity;
    private Main.elementType element;
    //animation
    private float stateTime;
    private TextureRegion shard;

    private AssetHelper assetHelper = new AssetHelper();

    public Shard(float x, float y, PlayScreen screen, Player.shootDirection direction, Main.elementType element) {
        this.x = x;
        this.y = y;
        width = 32;
        height = 32;
        world = screen.getWorld();
        shootDirection = direction;
        damage = 30;
        destroyed = false;
        setToDestroy = false;
        stateTime = 0;
        velocity = 5;
        this.element = element;
        defineShard();

        assetHelper.initializeAllAnimationsShard();
        assetHelper.generateFramesShard();
        setBounds(0, 0, width / Main.PPM, height / Main.PPM);
    }

    public void update(float dt) {
        stateTime += dt;

        getElementFrame();

        if (shootDirection == Player.shootDirection.LEFT && !shard.isFlipX()) {
            shard.flip(true, false);
        }
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2Body);
            destroyed = true;
        } else if (!destroyed) {
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        }
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(shard);

    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < .1) {
            if (shootDirection == Player.shootDirection.UP) {
                this.rotate90(false);
            } else if (shootDirection == Player.shootDirection.DOWN) {
                this.rotate90(true);
            }
            super.draw(batch);
        }
    }

    protected void defineShard() {
        BodyDef bDef = new BodyDef();
        switch (shootDirection) {
            case RIGHT:
                bDef.position.set(x + 48 / Main.PPM, y);
                break;
            case LEFT:
                bDef.position.set(x - 48 / Main.PPM, y);
                break;
            case UP:
                bDef.position.set(x, y + 64 / Main.PPM);
                break;
            case DOWN:
                bDef.position.set(x, y - 64 / Main.PPM);
                break;
        }
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        if (shootDirection == Player.shootDirection.RIGHT || shootDirection == Player.shootDirection.LEFT) {
            rectangleShape.setAsBox(30 / 2 / Main.PPM, 10 / 2 / Main.PPM); //Starts from the center
        } else {
            rectangleShape.setAsBox(10 / 2 / Main.PPM, 30 / 2 / Main.PPM); //Starts from the center
        }

        fDef.filter.categoryBits = Main.PLAYERATTACK_BIT;
        fDef.filter.maskBits = Main.GROUND_BIT
                | Main.BONFIRE_BIT
                | Main.ENEMY_BIT
                | Main.OBJECT_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef).setUserData(this);

        switch (shootDirection) {
            case RIGHT:
                b2Body.applyLinearImpulse(new Vector2(velocity, 0), this.b2Body.getWorldCenter(), true);
                break;
            case LEFT:
                b2Body.applyLinearImpulse(new Vector2(-velocity, 0), this.b2Body.getWorldCenter(), true);
                break;
            case UP:
                b2Body.applyLinearImpulse(new Vector2(0, velocity), this.b2Body.getWorldCenter(), true);
                break;
            case DOWN:
                b2Body.applyLinearImpulse(new Vector2(0, -velocity), this.b2Body.getWorldCenter(), true);
                break;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void destroy() {
        setToDestroy = true;
        stateTime = 0;
    }

    private void getElementFrame() {
        if (element == Main.elementType.WATER) {
            if (setToDestroy) {
                shard = assetHelper.getWaterCollisionAnimation().getKeyFrame(stateTime, true);
            } else {
                shard = assetHelper.getWaterShardAnimation().getKeyFrame(stateTime, true);
            }
        } else if (element == Main.elementType.FIRE) {
            if (setToDestroy) {
                shard = assetHelper.getFireCollisionAnimation().getKeyFrame(stateTime, true);
            } else {
                shard = assetHelper.getFireShardAnimation().getKeyFrame(stateTime, true);
            }
        } else if (element == Main.elementType.EARTH) {
            if (setToDestroy) {
                shard = assetHelper.getEarthCollisionAnimation().getKeyFrame(stateTime, true);
            } else {
                shard = assetHelper.getEarthShardAnimation().getKeyFrame(stateTime, true);
            }
        } else if (element == Main.elementType.AIR) {
            if (setToDestroy) {
                shard = assetHelper.getAirCollisionAnimation().getKeyFrame(stateTime, true);
            } else {
                shard = assetHelper.getAirShardAnimation().getKeyFrame(stateTime, true);
            }
        }

    }

    public Main.elementType getElement() {
        return this.element;
    }
}
