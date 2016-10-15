/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.scenes.Hud;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.tools.AssetHelper;

/**
 *
 * @author Creat-if
 */
public class Soldier extends Enemy {

    private static int counter = 0;
    //animation
    private float stateTime;
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
    private Soldier.State currentState;
    private Soldier.State previousState;

    private static int deathCounter;

    private AssetHelper assetHelper;
    private Animation deathAnimation;

    public Soldier(PlayScreen screen, float x, float y, Player player, Main.elementType element) {
        super(screen, x, y);
        counter++;
        this.number = counter;
        this.screen = screen;
        this.element = element;

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

        assetHelper = new AssetHelper(element);
        assetHelper.initializeAllAnimationsShard();
        assetHelper.generateFramesSoldier();
        deathAnimation = assetHelper.getDeathAnimation();
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

    private TextureRegion getFrame(float dt) {
        if (currentState == Soldier.State.ATTACK
                && assetHelper.getAttackAnimation().isAnimationFinished(stateTime)) {
            currentState = getState();
        } else if (currentState != Soldier.State.ATTACK) {
            currentState = getState();
        }
        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case UP:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case DOWN:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case LEFT:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
            case ATTACK:
                region = assetHelper.getAttackAnimation().getKeyFrame(stateTime, true);
                if (assetHelper.getAttackAnimation().isAnimationFinished(stateTime)) {
                    stateTime = 0;
                    victim.decreaseHealth(10);
                    victim.b2Body.applyLinearImpulse(new Vector2((isFlipped ? +10 : -10), 0), victim.b2Body.getWorldCenter(), true);
                }
                break;
            case DESTROYED:
                region = deathAnimation.getKeyFrame(stateTime);
                break;
            default:
                region = assetHelper.getWalkAnimation().getKeyFrame(stateTime, true);
                break;
        }
        if (!destroyed && !setToDestroy && previousState == Soldier.State.ATTACK && currentState != Soldier.State.ATTACK && assetHelper.getAttackAnimation().isAnimationFinished(stateTime)) {
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

    private Soldier.State getState() {
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
            if (this.getX() > victim.getX() && this.b2Body.getLinearVelocity().x >= -maxSpeed) {
                this.b2Body.applyLinearImpulse(new Vector2(-movementSpeed, 0), this.b2Body.getWorldCenter(), true);

            } else if (this.b2Body.getLinearVelocity().x <= maxSpeed) {
                this.b2Body.applyLinearImpulse(new Vector2(movementSpeed, 0), this.b2Body.getWorldCenter(), true);
            }
        }
        if (Math.abs(this.getY() - victim.getY()) > 1) {
            if (this.getY() > victim.getY() && this.b2Body.getLinearVelocity().y >= -maxSpeed) {
                this.b2Body.applyLinearImpulse(new Vector2(0, -movementSpeed), this.b2Body.getWorldCenter(), true);

            } else if (this.b2Body.getLinearVelocity().y <= maxSpeed) {
                this.b2Body.applyLinearImpulse(new Vector2(0, movementSpeed), this.b2Body.getWorldCenter(), true);
            }
        }
    }

    private void avoidObject() {
        if (this.b2Body.getLinearVelocity().x == 0
                && this.b2Body.getLinearVelocity().y == 0) {
            this.avoidDirection = getRandomState();
        }
        switch (avoidDirection) {
            case UP:
                if (this.b2Body.getLinearVelocity().y <= maxSpeed) {
                    this.b2Body.applyLinearImpulse(new Vector2(0, movementSpeed), this.b2Body.getWorldCenter(), true);
                }
                break;
            case DOWN:
                if (this.b2Body.getLinearVelocity().y >= -maxSpeed) {
                    this.b2Body.applyLinearImpulse(new Vector2(0, -movementSpeed), this.b2Body.getWorldCenter(), true);
                }
                break;
            case RIGHT:
                if (this.b2Body.getLinearVelocity().x <= maxSpeed) {
                    this.b2Body.applyLinearImpulse(new Vector2(movementSpeed, 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            case LEFT:
                if (this.b2Body.getLinearVelocity().x >= -maxSpeed) {
                    this.b2Body.applyLinearImpulse(new Vector2(-movementSpeed, 0), this.b2Body.getWorldCenter(), true);
                }
                break;
            default:
                if (this.b2Body.getLinearVelocity().y <= maxSpeed) {
                    this.b2Body.applyLinearImpulse(new Vector2(0, movementSpeed), this.b2Body.getWorldCenter(), true);
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
                deathAnimation = assetHelper.getDeathAnimationWater();
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
            case EARTH:
                deathAnimation = assetHelper.getDeathAnimationEarth();
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
                break;
            case FIRE:
                deathAnimation = assetHelper.getDeathAnimationFire();
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
                break;
            case AIR:
                deathAnimation = assetHelper.getDeathAnimationAir();
                this.setBounds(getX(), getY(), 80 / Main.PPM, 80 / Main.PPM);
                break;
            default:
                deathAnimation = assetHelper.getDeathAnimationWater();
                ;
                this.setBounds(getX(), getY(), (soldierWidth + 32) / Main.PPM, getHeight());
                break;
        }
    }

    public void death() {
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

}
