package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.coldpixel.sparkle.Constants;
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

    private ShapeRenderer shapeRenderer;

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

        shapeRenderer = new ShapeRenderer();

        definePlayer();

        playerStand = new TextureRegion(getTexture(), 0, 0, playerWidth, playerHeight);
        setBounds(0, 0, playerWidth / Main.PPM, playerHeight / Main.PPM);
        setRegion(playerStand);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        frames = new Array<TextureRegion>();
        //STANDING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mage.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerStanding = new Animation(0.1f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 5; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageWalk.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerRunning = new Animation(.1f, frames, LOOP);
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
            if ((b2Body.getLinearVelocity().x > 0) && !region.isFlipX()) {
                region.flip(true, false);
            } else if ((b2Body.getLinearVelocity().x < 0) && region.isFlipX()) {
                region.flip(true, false);
            }/*
            if ((b2Body.getLinearVelocity().y < 0) && !region.isFlipY()) {
                region.flip(false, true);
            } else if ((b2Body.getLinearVelocity().y > 0) && region.isFlipY()) {
                region.flip(false, true);
            }*/
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

    public void handleInput(float dt) {
        //LinearImpulse:first:x/y,second: where to impulse from the body?->center!, third: will impulse awake obj?
        if ((Gdx.input.isKeyPressed(Input.Keys.W) || (Gdx.input.isKeyPressed(Input.Keys.UP))) && this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()) {
            //Check if Player isnt moving faster than he is allowed to 
            this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.A) || (Gdx.input.isKeyPressed(Input.Keys.LEFT))) && this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(-this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.S) || (Gdx.input.isKeyPressed(Input.Keys.DOWN))) && this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(0, -this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.D) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT))) && this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
        }
//        player.b2Body.setLinearVelocity(new Vector2(0,0));//Stop immediatly.
        this.b2Body.setLinearDamping(60.0f);//Slow down
    }

    public void targetLine() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(b2Body.getPosition().x * Main.PPM,
                b2Body.getPosition().y * Main.PPM,
                Gdx.input.getX(),
                Constants.WINDOW_HEIGTH - Gdx.input.getY());
        shapeRenderer.end();
    }

}
