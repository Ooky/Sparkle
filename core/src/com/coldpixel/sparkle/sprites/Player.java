package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * @author Coldpixel
 */
public class Player extends Sprite {

//==============================================================================
//Initialization
//==============================================================================  
    private final int startPosX;
    private final int startPosY;
    private final int playerWidth;
    private final int playerHeight;
    private float movementSpeed;
    private float maxSpeed = 2.0f;
    public World world;
    public Body b2Body;

    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT,
    };
    public State currentState;
    public State previousState;
    private Animation playerStanding;
    private Animation playerRunning;
    private float stateTimer;
    Array<TextureRegion> frames;

    private TextureRegion playerStand;

//==============================================================================
//Methods
//==============================================================================
    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("Player48x64"));
        startPosX = 180;
        startPosY = 60;
        playerWidth = 48;
        playerHeight = 64;
        this.world = world;
        movementSpeed = 3.0f;
        maxSpeed = 4.0f;

        definePlayer();

        playerStand = new TextureRegion(getTexture(), 0, 0, playerWidth, playerHeight);
        setBounds(0, 0, playerWidth / Main.PPM, playerHeight / Main.PPM);
        setRegion(playerStand);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        frames = new Array<TextureRegion>();
        //STANDING
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerStanding = new Animation(1.0f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * playerWidth, 64, playerWidth, playerHeight));
        }
        playerRunning = new Animation(1.0f, frames, LOOP);
        frames.clear();
    }

    public void definePlayer() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(startPosX / Main.PPM, startPosY / Main.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(playerWidth / 2 / Main.PPM, playerHeight / 2 / Main.PPM); //Starts from the center

//        CircleShape shape = new CircleShape();
//        shape.setRadius(50);
//        fDef.shape = shape;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef);
    }

    public void update(float dt) {
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

//==============================================================================
//Getter
//==============================================================================
    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = playerStanding.getKeyFrame(stateTimer);
                break;
            case UP:
                region = playerRunning.getKeyFrame(stateTimer, true);
                break;
            case DOWN:
                region = playerRunning.getKeyFrame(stateTimer, true);
                break;
            case RIGHT:
                region = playerRunning.getKeyFrame(stateTimer, true);
                break;
            case LEFT:
                region = playerRunning.getKeyFrame(stateTimer, true);
                break;
            default:
                region = playerStand;
                break;
        }
        if (region != playerStand) {
            if ((b2Body.getLinearVelocity().x < 0) && !region.isFlipX()) {
                region.flip(true, false);
            } else if ((b2Body.getLinearVelocity().x > 0) && region.isFlipX()) {
                region.flip(true, false);
            }
            if ((b2Body.getLinearVelocity().y < 0) && !region.isFlipY()) {
                region.flip(false, true);
            } else if ((b2Body.getLinearVelocity().y > 0) && region.isFlipY()) {
                region.flip(false, true);
            }
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2Body.getLinearVelocity().y > 0) {
            return State.UP;
        } else if (b2Body.getLinearVelocity().y < 0) {
            return State.DOWN;
        } else if (b2Body.getLinearVelocity().x > 0) {
            return State.RIGHT;
        } else if (b2Body.getLinearVelocity().x < 0) {
            return State.LEFT;
        } else {
            return State.STANDING;
        }
    }

}
