/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private Array<TextureRegion> frames;
    private boolean isAttacking = false;

    private float x;
    private float y;
    protected int soldierWidth = 48;
    protected int soldierHeight = 64;

    private Player victim;
    private Boolean isFlipped;

    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT, ATTACK
    };
    public Soldier.State currentState;
    public Soldier.State previousState;

    public Soldier(PlayScreen screen, float x, float y, Player player) {
        super(screen, x, y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
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

        isFlipped = false;
        stateTime = 0;
        setBounds(0, 0, soldierWidth / Main.PPM, soldierHeight / Main.PPM);
        currentState = Soldier.State.STANDING;
        victim = player;
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2Body.getPosition().x - ((getWidth() + ((currentState == Soldier.State.ATTACK) ? (isFlipped ? -16 : +16) / Main.PPM : 0)) / 2), b2Body.getPosition().y - (getHeight() / 2));
        setRegion(getFrame(dt));
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
                | Main.OBJECT_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef);
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
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }
        if (previousState == Soldier.State.ATTACK && currentState != Soldier.State.ATTACK && attackAnimation.isAnimationFinished(stateTime)) {
            this.setBounds(getX(), getY(), soldierWidth / Main.PPM, getHeight());
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        if (victim.getX() < this.getX() && region.isFlipX()) {
            isFlipped = false;
            region.flip(true, false);
        } else if (victim.getX() > this.getX() && !region.isFlipX()) {
            isFlipped = true;
            region.flip(true, false);
        }
        return region;
    }

    public Soldier.State getState() {
        if (isAttacking) {
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

    public void setAttack(boolean attack) {
        isAttacking = attack;
        if (isAttacking) {
            this.setBounds(getX() - 13 / Main.PPM, getY(), (soldierWidth + 16) / Main.PPM, getHeight());
        }
    }

    public void setAttack(boolean attack, Player player) {
        isAttacking = attack;
        //  victim = player;
        if (isAttacking) {
            this.setBounds(getX() - 13 / Main.PPM, getY(), (soldierWidth + 16) / Main.PPM, getHeight());
        }
    }

}
