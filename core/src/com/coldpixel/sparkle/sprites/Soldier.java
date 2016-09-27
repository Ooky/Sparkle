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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.scenes.Hud;
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
    private Animation deathAnimationEarth;
    private Animation deathAnimationFire;
    private Animation deathAnimationWater;
    private Animation deathAnimationAir;
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
    private Main.elementType element;

    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT, ATTACK, DESTROYED
    };
    public Soldier.State currentState;
    public Soldier.State previousState;
	
	private static int deathCounter=0;

    public Soldier(PlayScreen screen, float x, float y, Player player, Main.elementType element) {
        super(screen, x, y);
        this.screen = screen;
        this.element = element;
        frames = new Array<TextureRegion>();
        generateFrames();
        health = 100;
        previousHealth = 100;
        destroyed = false;
        setToDestroy = false;
        avoidObject = false;
        movementSpeed = .8f;
        maxSpeed = 1.0f;
        isFlipped = false;
        stateTime = 0;
        setBounds(0, 0, soldierWidth / Main.PPM, soldierHeight / Main.PPM);
        currentState = Soldier.State.STANDING;
        victim = player;
    }

    public void update(float dt, Hud hud) {
        if (previousHealth != health) {
            hud.addScore(previousHealth - health);
            previousHealth = health;
        }
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2Body);
            destroyed = true;
            //change texture region
        } else if (!destroyed) {
            if (avoidObject) {
                avoidObject();
            } else if (!isAttacking) {
                moveAi();
            }
            setPosition((b2Body.getPosition().x) - ((getWidth() + ((currentState == Soldier.State.ATTACK) ? (isFlipped ? -16 : +16) / Main.PPM : 0)) / 2), b2Body.getPosition().y - (getHeight() / 2));
        }
        setRegion(getFrame(dt));

    }

    public void draw(Batch batch) {
       // if (!destroyed || stateTime < 15) {
            super.draw(batch);
       // }
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
        fDef.filter.maskBits
                = /*Main.GROUND_BIT |*/ //Do we need this?
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
        if (!destroyed) {
            if (victim.getX() < this.getX() && region.isFlipX()) {
                isFlipped = false;
                region.flip(true, false);
            } else if (victim.getX() > this.getX() && !region.isFlipX()) {
                isFlipped = true;
                region.flip(true, false);
            }
        } else if (isFlipped && !region.isFlipX()) {
            region.flip(true, false);
        }
        return region;
    }

    public Soldier.State getState() {
        if (setToDestroy) {
            return Soldier.State.DESTROYED;
        } else if (isAttacking) {
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

    private void moveAi() {
        if (Math.abs(this.getX() - victim.getX()) > .5) {
            if (this.getX() > victim.getX() && this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()) {
                this.b2Body.applyLinearImpulse(new Vector2(-this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);

            } else if (this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()) {
                this.b2Body.applyLinearImpulse(new Vector2(this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
            }
        }
        if (Math.abs(this.getY() - victim.getY()) > 1) {
            if (this.getY() > victim.getY() && this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()) {
                this.b2Body.applyLinearImpulse(new Vector2(0, -this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);

            } else if (this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()) {
                this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
            }
        }
    }

    public void avoidObject() {
        if (this.b2Body.getLinearVelocity().x == 0
                && this.b2Body.getLinearVelocity().y == 0) {
            this.avoidDirection = getRandomState();
        }
        switch (avoidDirection) {
            case UP:
                if (this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()) {
                    this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
                }
                break;
            case DOWN:
                if (this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()) {
                    this.b2Body.applyLinearImpulse(new Vector2(0, -this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
                }
                break;
            case RIGHT:
                if (this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()) {
                    this.b2Body.applyLinearImpulse(new Vector2(this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            case LEFT:
                if (this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()) {
                    this.b2Body.applyLinearImpulse(new Vector2(-this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            default:
                if (this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()) {
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

    public void setAvoidObject(Boolean set) {
        this.avoidObject = set;
    }

    public void setAvoidObject(Rectangle rect) {
        this.avoidObject = true;
        //Rectangle
        float widthR = rect.width / Main.PPM;
        float heightR = rect.height / Main.PPM;
        float xR = rect.x / Main.PPM;
        float yR = rect.y / Main.PPM;
        //Soldier
        float widthS = getWidth();
        float heightS = getHeight();
        float xS = b2Body.getPosition().x - widthS / 2;
        float yS = b2Body.getPosition().y - heightS / 2;
        this.avoidDirection = getAvoidingDirection(xR, widthR, yR, heightR, xS, widthS, yS, heightS);
        /*  System.out.println("y: "+(String.format(java.util.Locale.US,"%.2f", yS-0.01)));
        System.out.println("x: "+(String.format(java.util.Locale.US,"%.2f", xS-0.01)));
        System.out.println("left/bott: "+xR+" ::::::: "+yR);
        System.out.println("left/top: "+xR+" ::::::: "+String.format(java.util.Locale.US,"%.2f", (yR+heightR)));
        System.out.println("right/bott: "+String.format(java.util.Locale.US,"%.2f", xR+widthR)+" ::::::: "+yR);
        System.out.println("right/top: "+String.format(java.util.Locale.US,"%.2f", xR+widthR)+" ::::::: "+String.format(java.util.Locale.US,"%.2f", yR+heightR));
         */
    }

    private State getAvoidingDirection(float xR, float widthR, float yR, float heightR, float xS, float widthS, float yS, float heightS) {
        if ((xS >= xR + widthR)) {//soldier is on the right side of an object
            if (xR + widthR <= victim.b2Body.getPosition().x - victim.getWidth() / 2) {//player is on the right side of the object
                return State.RIGHT;
            } else /*if(xR >= victim.b2Body.getPosition().x+victim.getWidth()/2)*///player isn't on the right side of the object
             if (yS >= victim.b2Body.getPosition().y) {//the player is standing lower than the soldier
                    return State.DOWN;
                } else {//the players position is higher than the soldiers
                    return State.UP;
                }
        } else if (xS + widthS <= xR) {//soldier is on the left side of an object
            if (xR >= victim.b2Body.getPosition().x + victim.getWidth() / 2) {//player is on the left side of the object
                return State.LEFT;
            } else /*if(xR <= victim.b2Body.getPosition().x-victim.getWidth()/2)*///player isn't on the left side of the object
             if (yS >= victim.b2Body.getPosition().y) {//the player is standing lower than the soldier
                    return State.DOWN;
                } else {//the players position is higher than the soldiers
                    return State.UP;
                }
        } else if (yS >= yR + heightR) {//soldier is on top of the object
            if (yR + heightR <= victim.b2Body.getPosition().y - victim.getHeight() / 2) {//player higher than object
                return State.UP;
            } else//the player isn't higher than the object
             if (xS >= victim.b2Body.getPosition().x) {//player is on the left
                    return State.LEFT;
                } else {//player is on the right
                    return State.RIGHT;
                }
        } else if (yS + heightS <= yR) {//soldier is under the object
            if (yR >= victim.b2Body.getPosition().y + victim.getHeight() / 2) {//player lower than object
                return State.DOWN;
            } else//the player isn't lower than the object
             if (xS >= victim.b2Body.getPosition().x) {//player is on the left
                    return State.LEFT;
                } else {
                    return State.RIGHT;
                }
        } else {
            return getRandomState();
        }
    }

    private State getRandomState() {
        State randomState = State.UP;
        switch (MathUtils.random(0, 3)) {
            case 0:
                randomState = State.DOWN;
                break;
            case 1:
                randomState = State.LEFT;
                break;
            case 2:
                randomState = State.RIGHT;
                break;
            case 3:
                randomState = State.UP;
                break;
        }
        return randomState;
    }
    
    public void setDeathAnimation(Main.elementType element){
        switch(element){
            case WATER:
                deathAnimation = deathAnimationWater;
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
            case EARTH:
                deathAnimation = deathAnimationEarth;
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
            case FIRE:
                deathAnimation = deathAnimationFire;
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
                break;
            case AIR:
                deathAnimation = deathAnimationAir;
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
                break;
            default:
                deathAnimation = deathAnimationWater;
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
        }
    }
    
    private void generateFrames() {

        if (null != element) {
            switch (element) {
                case EARTH:
                    //Earth
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWalk.png"), i * soldierWidth, 0, soldierWidth, soldierHeight));
                    }   //In case that we want to work with atlas later  frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));            
                    walkAnimation = new Animation(0.1f, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAttack.png"), i * (soldierWidth + 16), 0, soldierWidth + 16, soldierHeight));
                    }
                    attackAnimation = new Animation(0.15f, frames);
                    frames.clear();
                    //DeathAnimation Water
                    for (int i = 0; i < 12; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierDeathFinal.png"), i * (soldierWidth + 32), 0, soldierWidth + 32, soldierHeight));
                    }
                    deathAnimationWater = new Animation(0.3f, frames);
                    frames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierEarthDeathFire.png"), i * 80, 0, 80, 80));
                    }
                    deathAnimationFire = new Animation(0.3f, frames);
                    frames.clear();
                    break;
                case WATER:
                    //Water
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWalkBlue.png"), i * soldierWidth, 0, soldierWidth, soldierHeight));
                    }   //In case that we want to work with atlas later  frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));            
                    walkAnimation = new Animation(0.1f, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAttackBlue.png"), i * (soldierWidth + 16), 0, soldierWidth + 16, soldierHeight));
                    }
                    attackAnimation = new Animation(0.15f, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierDeathFinalBlue.png"), i * (soldierWidth + 32), 0, soldierWidth + 32, soldierHeight));
                    }
                    deathAnimationWater = new Animation(0.2f, frames);
                    frames.clear();
                     //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWaterDeathFire.png"), i * 80, 0, 80, 80));
                    }
                    deathAnimationFire = new Animation(0.3f, frames);
                    frames.clear();
					 //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWaterDeathAir.png"), i * 80, 0, 80, 80));
                    }
                    deathAnimationAir = new Animation(0.3f, frames);
                    frames.clear();
                    break;
                case FIRE:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWalkRed.png"), i * soldierWidth, 0, soldierWidth, soldierHeight));
                    }   //In case that we want to work with atlas later  frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));            
                    walkAnimation = new Animation(0.1f, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAttackRed.png"), i * (soldierWidth + 16), 0, soldierWidth + 16, soldierHeight));
                    }
                    attackAnimation = new Animation(0.15f, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierDeathFinalRed.png"), i * (soldierWidth + 32), 0, soldierWidth + 32, soldierHeight));
                    }
                    deathAnimationWater = new Animation(0.3f, frames);
                    frames.clear();
                     //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierFireDeathFire.png"), i * 80, 0, 80, 80));
                    }
                    deathAnimationFire = new Animation(0.3f, frames);
                    frames.clear();
                    break;
                case AIR:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierWalkYelow.png"), i * soldierWidth, 0, soldierWidth, soldierHeight));
                    }   //In case that we want to work with atlas later  frames.add(new TextureRegion(screen.getAtlas().findRegion("soldier"), i*soldierWidth, 0, soldierWidth, soldierHeight));            
                    walkAnimation = new Animation(0.1f, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAttackYelow.png"), i * (soldierWidth + 16), 0, soldierWidth + 16, soldierHeight));
                    }
                    attackAnimation = new Animation(0.15f, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierDeathFinalYelow.png"), i * (soldierWidth + 32), 0, soldierWidth + 32, soldierHeight));
                    }
                    deathAnimationWater = new Animation(0.3f, frames);
                    frames.clear();
                     //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(new TextureRegion(new Texture("Graphics/Enemy/Soldier/soldierAirDeathFire.png"), i * 80, 0,80, 80));
                    }
                    deathAnimationFire = new Animation(0.3f, frames);
                    frames.clear();
                    break;
                default:
                    break;
            }
        }
    }

    public void death() {
        setCategoryFilter(Main.DESTROYED_BIT);
        setToDestroy = true;
		deathCounter++;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }
    
    public Main.elementType getElement(){
        return element;
    }

	public static int getDeathCounter(){
		return deathCounter;
	}
}
