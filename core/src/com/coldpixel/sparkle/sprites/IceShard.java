package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author mike
 */
public class IceShard extends Sprite{
    
    private int width;
    private int height;
    private float x;
    private float y;    
    private World world;
    private Body b2Body;
    private Boolean facingRight;
    private int damage;
    private Boolean setToDestroy;
    private Boolean destroyed;
    //animation
    private float stateTime;
    private Animation shardAnimation;
    private Animation collisionAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion iceShard;
    
    public IceShard(float x, float y, PlayScreen screen, Boolean directionRight) {
        this.x = x;
        this.y = y;
        width = 32;
        height = 32;
        world = screen.getWorld();
        facingRight = directionRight;
        damage = 25;
        destroyed = false;
        setToDestroy = false;
        stateTime = 0;
        frames = new Array<TextureRegion>();
        
        defineIceShard();
        //fly Animation
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Attacks/Ice/shard.png"), i * width, 0, width, height));
        }
        shardAnimation = new Animation(0.1f, frames);
        frames.clear();
        //Collision Animation
        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Attacks/Ice/shardCollision.png"), i * width, 0, width, height));
        }
        collisionAnimation = new Animation(.015f, frames);
        frames.clear(); 
        setBounds(0, 0, width / Main.PPM, height / Main.PPM);
    }
    
    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy)
            iceShard = collisionAnimation.getKeyFrame(stateTime, true);
        else
            iceShard = shardAnimation.getKeyFrame(stateTime, true);
        if(!facingRight && !iceShard.isFlipX())
            iceShard.flip(true, false);
        if(setToDestroy && !destroyed){
            world.destroyBody(b2Body);
            destroyed = true;
        } else if (!destroyed){
           setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        }
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(iceShard);
    }
    
    public void draw(Batch batch){
        if(!destroyed || stateTime < .1){
            super.draw(batch);
        }
    }

      protected void defineIceShard() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x + (facingRight?48:-48) / Main.PPM, y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(30 / 2 / Main.PPM, 10 / 2 / Main.PPM); //Starts from the center

        fDef.filter.categoryBits = Main.PLAYERATTACK_BIT;
        fDef.filter.maskBits = /*Main.GROUND_BIT |*/ //Do we need this?
                Main.BONFIRE_BIT
                | Main.ENEMY_BIT
                | Main.OBJECT_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef).setUserData(this);
        b2Body.applyLinearImpulse(new Vector2(facingRight?+5:-5, 0), this.b2Body.getWorldCenter(), true);
    }
      
    public int getDamage(){
        return damage;
    }
    
    public void destroy(){
        setToDestroy = true;
        stateTime = 0;
    }
}
