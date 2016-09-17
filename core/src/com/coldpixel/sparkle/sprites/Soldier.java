/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;

/**
 *
 * @author mike
 */
public class Soldier extends Enemy {

    //animation

    private float stateTime;
    private Animation walkAnimation;
    private Animation attackAnimation;
    private Animation deathAnimation;
    private Array<TextureRegion> frames;
    private boolean isAttacking = false;

    private float x;
    private float y;
    protected int soldierWidth = 48;
    protected int soldierHeight = 64;
    private float movementSpeed;
    private float maxSpeed;
    
    private Player victim;
    private Boolean isFlipped;
    private Boolean setToDestroy;
    private Boolean destroyed;
    private Boolean avoidObject;
    private State avoidDirection;
    
    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT, ATTACK, DESTROYED
    };
    public Soldier.State currentState;
    public Soldier.State previousState;

    public Soldier(PlayScreen screen, float x, float y, Player player) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        health = 100;
        destroyed = false;
        setToDestroy = false;
        avoidObject = false;
        movementSpeed = .8f;
        maxSpeed = 1.0f;
        
        //walkAnimation
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWalk.png"), i * soldierWidth, 0, soldierWidth, soldierHeight));
        }
        //In case that we want to work with atlas later  frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));            
        walkAnimation = new Animation(0.1f, frames);
        frames.clear();
        //AttackAnimation
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAttack.png"), i * (soldierWidth + 16), 0, soldierWidth + 16, soldierHeight));
        }
        attackAnimation = new Animation(0.15f, frames);
        frames.clear();
        //DeathAnimation
        for (int i = 0; i < 12; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierDeathFinal.png"), i * (soldierWidth + 32), 0, soldierWidth + 32, soldierHeight));
        }
        deathAnimation = new Animation(0.3f, frames);
        frames.clear();
        isFlipped = false;
        stateTime = 0;
        setBounds(0, 0, soldierWidth / Main.PPM, soldierHeight / Main.PPM);
        currentState = Soldier.State.STANDING;
        victim = player;
    }

    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2Body);
            destroyed = true;  
            
            //change texture region
        } else if (!destroyed){
            if(avoidObject){
                avoidObject();
            }
            else if(!isAttacking){
                moveAi();
            }
            setPosition((b2Body.getPosition().x ) - ((getWidth() + ((currentState == Soldier.State.ATTACK) ? (isFlipped ? -16 : +16) / Main.PPM : 0)) / 2), b2Body.getPosition().y - (getHeight() / 2));    
        }
            setRegion(getFrame(dt));
            
    }
    
    public void draw(Batch batch){
        if(!destroyed || stateTime < 15){
            super.draw(batch);
        }
    }

    protected void defineEnemy() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(48 / 2 / Main.PPM, 64 / 2 / Main.PPM); //Starts from the center

        fDef.filter.categoryBits = Main.ENEMY_BIT;
        fDef.filter.maskBits = /*Main.GROUND_BIT |*/ //Do we need this?
                Main.BONFIRE_BIT
                | Main.PLAYER_BIT
                | Main.ENEMY_BIT
                | Main.OBJECT_BIT
                | Main.PLAYERATTACK_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef).setUserData(this);;
        rectangleShape.setAsBox(16 / 2 / Main.PPM, 64 / 2 / Main.PPM, new Vector2(-32 / Main.PPM, 0), 0);
        fDef.shape = rectangleShape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = Main.ENEMYMELEEATTACK_BIT;
        fDef.filter.maskBits = Main.PLAYER_BIT;
        b2Body.createFixture(fDef).setUserData(this);
        rectangleShape.setAsBox(16 / 2 / Main.PPM, 64 / 2 / Main.PPM, new Vector2(+32 / Main.PPM, 0), 0);
        fDef.shape = rectangleShape;
        fDef.isSensor = true;
        fDef.filter.categoryBits = Main.ENEMYMELEEATTACK_BIT;
        fDef.filter.maskBits = Main.PLAYER_BIT;
        b2Body.createFixture(fDef).setUserData(this);
    }

    public TextureRegion getFrame(float dt) {
        if (currentState == Soldier.State.ATTACK
                && attackAnimation.isAnimationFinished(stateTime)) {
            currentState = getState();
        } else if (currentState != Soldier.State.ATTACK) {
            currentState = getState();
        }
        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case UP:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case DOWN:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACK:
                region = attackAnimation.getKeyFrame(stateTime, true);
                if (attackAnimation.isAnimationFinished(stateTime)) {
                    stateTime = 0;
                    victim.decreaseHealth(10);
                    victim.b2Body.applyLinearImpulse(new Vector2((isFlipped ? +10 : -10), 0), victim.b2Body.getWorldCenter(), true);
                }
                break;
            case DESTROYED:
                region = deathAnimation.getKeyFrame(stateTime);
                this.setBounds(getX(), getY(), (soldierWidth+32) / Main.PPM, getHeight());
                break;
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (previousState == Soldier.State.ATTACK && currentState != Soldier.State.ATTACK && attackAnimation.isAnimationFinished(stateTime)) {
            this.setBounds(getX(), getY(), soldierWidth / Main.PPM, getHeight());
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        if(!destroyed){
            if (victim.getX() < this.getX() && region.isFlipX()) {
                isFlipped = false;
                region.flip(true, false);
            } else if (victim.getX() > this.getX() && !region.isFlipX()) {
                isFlipped = true;
                region.flip(true, false);
            }
        } else{
            if(isFlipped && !region.isFlipX())
                region.flip(true, false);
        }
        return region;
    }

    public Soldier.State getState() {
        if(setToDestroy){
            return Soldier.State.DESTROYED;
        }
        else if (isAttacking) {
            return Soldier.State.ATTACK;
        } else if (b2Body.getLinearVelocity().y > 0.001) {
            return Soldier.State.UP;
        } else if (b2Body.getLinearVelocity().y < -0.001) {
            return Soldier.State.DOWN;
        } else if (b2Body.getLinearVelocity().x > 0.001) {
            return Soldier.State.RIGHT;
        } else if (b2Body.getLinearVelocity().x < -0.001) {
            return Soldier.State.LEFT;
        } else {
            return Soldier.State.STANDING;
        }
    }
    
    private void moveAi(){
        if(Math.abs(this.getX() - victim.getX()) > .5 ){
            if(this.getX() > victim.getX()&& this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()){
                this.b2Body.applyLinearImpulse(new Vector2(- this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);

            }  else if(this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()){
                this.b2Body.applyLinearImpulse(new Vector2( this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
            }
        } 
        if(Math.abs(this.getY() - victim.getY()) > 1 ){
            if(this.getY() > victim.getY()&& this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()){
                this.b2Body.applyLinearImpulse(new Vector2(0, - this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);

            }  else if(this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()){
                this.b2Body.applyLinearImpulse(new Vector2(0,  this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
            }
        } 
    }
    
    public void avoidObject(){
        switch(avoidDirection){
            case UP:
                if(this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()){
                    this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
                }
                break;
            case DOWN:
                if(this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()){
                    this.b2Body.applyLinearImpulse(new Vector2(0, -this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
                }
                break;
            case RIGHT:
                if(this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()){
                    this.b2Body.applyLinearImpulse(new Vector2(this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            case LEFT:
                if(this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()){
                    this.b2Body.applyLinearImpulse(new Vector2(-this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            default:
                if(this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()){
                    this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
                }
                break;
        }        
    }

    public void setAttack(boolean attack) {
        isAttacking = attack;
        if (isAttacking) {
            this.setBounds(getX() - 13 / Main.PPM, getY(), (soldierWidth + 16) / Main.PPM, getHeight());
        }
    }
    
    public void setAvoidObject(Boolean set){
        this.avoidObject = set;
    }
    
    public void setAvoidObject(State direction){
        this.avoidObject = true;
        this.avoidDirection = direction;
    }
    
    public void death(){
        setCategoryFilter(Main.DESTROYED_BIT);
        setToDestroy = true;
    }
    
    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

}
