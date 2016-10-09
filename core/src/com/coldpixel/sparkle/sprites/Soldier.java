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

    private static int counter = 0;
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

    protected final int soldierWidth = 48;
    protected final int soldierHeight = 64;
    private float movementSpeed;
    private float maxSpeed;

    private Player victim;
    private Boolean isFlipped;
    private Boolean setToDestroy;
    private Boolean destroyed;
    private Boolean avoidObject;
    private State avoidDirection;
    private Main.elementType element;

    private Boolean isSpawned = false;
    private int number;

    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT, ATTACK, DESTROYED
    };
    public Soldier.State currentState;
    public Soldier.State previousState;

    private static int deathCounter;

    private Texture textureSoldier;
    //Earth
    private TextureRegion[] soldierWalkGreen;
    private TextureRegion[] soldierAttackGreen;
    private TextureRegion[] soldierGreenDeathWater;
    private TextureRegion[] soldierGreenDeathFire;
    private TextureRegion[] soldierGreenDeathAir;
    private TextureRegion[] soldierGreenDeathEarth;
    //Water
    private TextureRegion[] soldierWalkBlue;
    private TextureRegion[] soldierAttackBlue;
    private TextureRegion[] soldierBlueDeathWater;
    private TextureRegion[] soldierBlueDeathFire;
    private TextureRegion[] soldierBlueDeathAir;
    private TextureRegion[] soldierBlueDeathEarth;
    //Fire
    private TextureRegion[] soldierWalkRed;
    private TextureRegion[] soldierAttackRed;
    private TextureRegion[] soldierRedDeathWater;
    private TextureRegion[] soldierRedDeathFire;
    private TextureRegion[] soldierRedDeathAir;
    private TextureRegion[] soldierRedDeathEarth;
    //Air
    private TextureRegion[] soldierWalkYellow;
    private TextureRegion[] soldierAttackYellow;
    private TextureRegion[] soldierYellowDeathWater;
    private TextureRegion[] soldierYellowDeathFire;
    private TextureRegion[] soldierYellowDeathAir;
    private TextureRegion[] soldierYellowDeathEarth;

    private float walkAnimationSpeed = 0.1f;
    private float attackAnimationSpeed = 0.15f;
    private float deathAnimationSpeed = 0.15f;

    public Soldier(PlayScreen screen, float x, float y, Player player, Main.elementType element) {
        super(screen, x, y);
        counter++;
        this.number = counter;
        this.screen = screen;
        this.element = element;
        frames = new Array<TextureRegion>();
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
        deathCounter = 0;
        //Textures
        textureSoldier = new Texture("Graphics/Enemy/All.png");
        initializeAnimationALL();

        generateFrames();
    }

    private void generateFrames() {

        if (null != element) {
            switch (element) {
                case EARTH:
                    //Earth
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(soldierWalkGreen[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(soldierAttackGreen[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Water
                    for (int i = 0; i < 12; i++) {
                        frames.add(soldierGreenDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, frames);
                    frames.clear();

                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(soldierGreenDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, frames);
                    frames.clear();

                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        frames.add(soldierGreenDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, frames);
                    frames.clear();

                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        frames.add(soldierGreenDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    break;

                case WATER:
                    //Water
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(soldierWalkBlue[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(soldierAttackBlue[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(soldierBlueDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(soldierBlueDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        frames.add(soldierBlueDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        frames.add(soldierBlueDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    break;

                case FIRE:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(soldierWalkRed[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(soldierAttackRed[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(soldierRedDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(soldierRedDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        frames.add(soldierRedDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        frames.add(soldierRedDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    break;
                case AIR:
                    //walkAnimation
                    for (int i = 0; i < 10; i++) {
                        frames.add(soldierWalkYellow[i]);
                    }
                    walkAnimation = new Animation(walkAnimationSpeed, frames);
                    frames.clear();
                    //AttackAnimation
                    for (int i = 0; i < 4; i++) {
                        frames.add(soldierAttackYellow[i]);
                    }
                    attackAnimation = new Animation(attackAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation
                    for (int i = 0; i < 12; i++) {
                        frames.add(soldierYellowDeathWater[i]);
                    }
                    deathAnimationWater = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Fire
                    for (int i = 0; i < 15; i++) {
                        frames.add(soldierYellowDeathFire[i]);
                    }
                    deathAnimationFire = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Air
                    for (int i = 0; i < 16; i++) {
                        frames.add(soldierYellowDeathAir[i]);
                    }
                    deathAnimationAir = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    //DeathAnimation Earth
                    for (int i = 0; i < 14; i++) {
                        frames.add(soldierYellowDeathEarth[i]);
                    }
                    deathAnimationEarth = new Animation(deathAnimationSpeed, frames);
                    frames.clear();
                    break;
            }
        }
    }

    @Override
    public void update(float dt, Hud hud) {
        if (previousHealth != health) {
            hud.addScore(previousHealth - health);
            previousHealth = health;
        }
        //stateTime += dt;
        if (setToDestroy && !destroyed) {
            // world.destroyBody(b2Body);
            b2Body.setActive(false);
            //b2Body.setActive(false);
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

    @Override
    public void draw(Batch batch) {
        //if (!destroyed || stateTime < 50) {
        super.draw(batch);
        // }
    }

    @Override
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
        if (!destroyed && !setToDestroy && previousState == Soldier.State.ATTACK && currentState != Soldier.State.ATTACK && attackAnimation.isAnimationFinished(stateTime)) {
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
        if (isAttacking && (!destroyed || setToDestroy)) {
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

    public void setDeathAnimation(Main.elementType element) {
        switch (element) {
            case WATER:
                deathAnimation = deathAnimationWater;
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
            case EARTH:
                deathAnimation = deathAnimationEarth;
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
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

    public void death() {
        // setCategoryFilter(Main.DESTROYED_BIT);
        setToDestroy = true;
        deathCounter++;
    }

    public void revive() {
        health = 100;
        previousHealth = 100;
        destroyed = false;
        setToDestroy = false;
        avoidObject = false;
        movementSpeed = .8f;
        maxSpeed = 1.0f;
        isFlipped = false;
        setBounds(0, 0, soldierWidth / Main.PPM, soldierHeight / Main.PPM);
        currentState = Soldier.State.RIGHT;
        previousState = Soldier.State.RIGHT;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Main.elementType getElement() {
        return element;
    }

    public static int getDeathCounter() {
        return deathCounter;
    }

    public Boolean getIsSpawned() {
        return isSpawned;
    }

    public void setIsSpawned(Boolean isSpawned) {
        this.isSpawned = isSpawned;

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private void initializeAnimationALL() {
        //Earth
        soldierWalkGreen();
        soldierAttackGreen();
        soldierGreenDeathWater();
        soldierGreenDeathFire();
        soldierGreenDeathAir();
        soldierGreenDeathEarth();
        //Water
        soldierWalkBlue();
        soldierAttackBlue();
        soldierBlueDeathWater();
        soldierBlueDeathFire();
        soldierBlueDeathAir();
        soldierBlueDeathEarth();
        //Fire
        soldierWalkRed();
        soldierAttackRed();
        soldierRedDeathWater();
        soldierRedDeathFire();
        soldierRedDeathAir();
        soldierRedDeathEarth();
        //Air
        soldierWalkYellow();
        soldierAttackYellow();
        soldierYellowDeathWater();
        soldierYellowDeathFire();
        soldierYellowDeathAir();
        soldierYellowDeathEarth();

    }

    //Soldier Air Yellow
//------------------------------------------------------------------------------
    private TextureRegion[] soldierYellowDeathEarth() {
        soldierYellowDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierYellowDeathEarth[j] = new TextureRegion(textureSoldier, i, 640, 80, 80);
            j++;
        }

        return soldierYellowDeathEarth;
    }

    private TextureRegion[] soldierYellowDeathAir() {
        soldierYellowDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierYellowDeathAir[j] = new TextureRegion(textureSoldier, i, 0, 80, 80);
            j++;
        }
        return soldierYellowDeathAir;
    }

    private TextureRegion[] soldierYellowDeathFire() {
        soldierYellowDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierYellowDeathFire[j] = new TextureRegion(textureSoldier, i, 320, 80, 80);
            j++;
        }
        return soldierYellowDeathFire;
    }

    private TextureRegion[] soldierYellowDeathWater() {
        soldierYellowDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierYellowDeathWater[j] = new TextureRegion(textureSoldier, i, 960, 80, 64);
            j++;
        }

        return soldierYellowDeathWater;
    }

    private TextureRegion[] soldierAttackYellow() {
        soldierAttackYellow = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackYellow[j] = new TextureRegion(textureSoldier, 1152, i, 64, 64);
            j++;
        }
        return soldierAttackYellow;
    }

    private TextureRegion[] soldierWalkYellow() {
        soldierWalkYellow = new TextureRegion[10];
        int j = 0;
        for (int i = 0; i <= 432; i += 48) {
            soldierWalkYellow[j] = new TextureRegion(textureSoldier, i, 1216, 48, 64);
            j++;
        }
        return soldierWalkYellow;
    }

//Soldier Water Blue
//------------------------------------------------------------------------------
    private TextureRegion[] soldierBlueDeathEarth() {
        soldierBlueDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierBlueDeathEarth[j] = new TextureRegion(textureSoldier, i, 800, 80, 80);
            j++;
        }

        return soldierBlueDeathEarth;
    }

    private TextureRegion[] soldierBlueDeathAir() {
        soldierBlueDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierBlueDeathAir[j] = new TextureRegion(textureSoldier, i, 80, 80, 80);
            j++;
        }
        return soldierBlueDeathAir;
    }

    private TextureRegion[] soldierBlueDeathFire() {
        soldierBlueDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierBlueDeathFire[j] = new TextureRegion(textureSoldier, i, 480, 80, 80);
            j++;
        }
        return soldierBlueDeathFire;
    }

    private TextureRegion[] soldierBlueDeathWater() {
        soldierBlueDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierBlueDeathWater[j] = new TextureRegion(textureSoldier, i, 1024, 80, 64);
            j++;
        }

        return soldierBlueDeathWater;
    }

    private TextureRegion[] soldierAttackBlue() {
        soldierAttackBlue = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackBlue[j] = new TextureRegion(textureSoldier, 1024, i, 64, 64);
            j++;
        }
        return soldierAttackBlue;
    }

    private TextureRegion[] soldierWalkBlue() {
        soldierWalkBlue = new TextureRegion[10];
        int j = 0;
        for (int i = 0; i <= 432; i += 48) {
            soldierWalkBlue[j] = new TextureRegion(textureSoldier, i, 1280, 48, 64);
            j++;
        }
        return soldierWalkBlue;
    }

//Soldier Fire Red
//------------------------------------------------------------------------------
    private TextureRegion[] soldierRedDeathEarth() {
        soldierRedDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierRedDeathEarth[j] = new TextureRegion(textureSoldier, i, 720, 80, 80);
            j++;
        }
        return soldierRedDeathEarth;
    }

    private TextureRegion[] soldierRedDeathAir() {
        soldierRedDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierRedDeathAir[j] = new TextureRegion(textureSoldier, i, 160, 80, 80);
            j++;
        }
        return soldierRedDeathAir;
    }

    private TextureRegion[] soldierRedDeathFire() {
        soldierRedDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierRedDeathFire[j] = new TextureRegion(textureSoldier, i, 560, 80, 80);
            j++;
        }
        return soldierRedDeathFire;
    }

    private TextureRegion[] soldierRedDeathWater() {
        soldierRedDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierRedDeathWater[j] = new TextureRegion(textureSoldier, i, 1152, 80, 64);
            j++;
        }
        return soldierRedDeathWater;
    }

    private TextureRegion[] soldierAttackRed() {
        soldierAttackRed = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackRed[j] = new TextureRegion(textureSoldier, 1088, i, 64, 64);
            j++;
        }
        return soldierAttackRed;
    }

    private TextureRegion[] soldierWalkRed() {
        soldierWalkRed = new TextureRegion[10];
        int j = 0;
        for (int i = 480; i <= 912; i += 48) {
            soldierWalkRed[j] = new TextureRegion(textureSoldier, i, 1216, 48, 64);
            j++;
        }
        return soldierWalkRed;
    }

//Soldier Earth Green
//------------------------------------------------------------------------------
    private TextureRegion[] soldierGreenDeathEarth() {
        soldierGreenDeathEarth = new TextureRegion[14];
        int j = 0;
        for (int i = 0; i <= 1040; i += 80) {
            soldierGreenDeathEarth[j] = new TextureRegion(textureSoldier, i, 880, 80, 80);
            j++;
        }
        return soldierGreenDeathEarth;
    }

    private TextureRegion[] soldierGreenDeathAir() {
        soldierGreenDeathAir = new TextureRegion[16];
        int j = 0;
        for (int i = 0; i <= 1200; i += 80) {
            soldierGreenDeathAir[j] = new TextureRegion(textureSoldier, i, 240, 80, 80);
            j++;
        }
        return soldierGreenDeathAir;
    }

    private TextureRegion[] soldierGreenDeathFire() {
        soldierGreenDeathFire = new TextureRegion[15];
        int j = 0;
        for (int i = 0; i <= 1120; i += 80) {
            soldierGreenDeathFire[j] = new TextureRegion(textureSoldier, i, 400, 80, 80);
            j++;
        }
        return soldierGreenDeathFire;
    }

    private TextureRegion[] soldierGreenDeathWater() {
        soldierGreenDeathWater = new TextureRegion[12];
        int j = 0;
        for (int i = 0; i <= 880; i += 80) {
            soldierGreenDeathWater[j] = new TextureRegion(textureSoldier, i, 1088, 80, 64);
            j++;
        }
        return soldierGreenDeathWater;
    }

    private TextureRegion[] soldierAttackGreen() {
        soldierAttackGreen = new TextureRegion[4];
        int j = 0;
        for (int i = 1024; i <= 1216; i += 64) {
            soldierAttackGreen[j] = new TextureRegion(textureSoldier, 960, i, 64, 64);
            j++;
        }
        return soldierAttackGreen;
    }

    private TextureRegion[] soldierWalkGreen() {
        soldierWalkGreen = new TextureRegion[10];
        int j = 0;
        for (int i = 640; i <= 1216; i += 64) {
            soldierWalkGreen[j] = new TextureRegion(textureSoldier, 1232, i, 48, 64);
            j++;
        }
        return soldierWalkGreen;
    }

}
