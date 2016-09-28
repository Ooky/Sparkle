package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.screens.PlayScreen;
import java.util.ArrayList;

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
    private boolean directionRight;

    public enum shootDirection {

        RIGHT, LEFT, UP, DOWN
    };
    public shootDirection currentShootDirection;
    private float health;
    public World world;
    private PlayScreen screen;
    public Body b2Body;

    //Player Stats
    public enum State {

        STANDING, UP, DOWN, RIGHT, LEFT, ATTACK,
    };
    public Main.elementType currentElement;
    private Main.elementType startingElement = Main.elementType.WATER;

    public State currentState;
    public State previousState;
    //Animation 
    private Animation playerWaterStanding;
    private Animation playerWaterRunning;
    private Animation playerWaterAttack;
    private Animation playerFireStanding;
    private Animation playerFireRunning;
    private Animation playerFireAttack;
    private Animation playerEarthStanding;
    private Animation playerEarthRunning;
    private Animation playerEarthAttack;
    private Animation playerAirStanding;
    private Animation playerAirRunning;
    private Animation playerAirAttack;
    private Animation playerCurrentElementStanding;
    private Animation playerCurrentElementRunning;
    private Animation playerCurrentElementAttack;

    private float stateTimer;
    Array<TextureRegion> frames;
    private TextureRegion playerStand;

    private ShapeRenderer shapeRenderer;

    //healing
    private float healingFactor;
    private Boolean isHealing;
    //Attack
    private Boolean isAttacking;
    private Shard iceShard;
    private ArrayList<Shard> shards;
  //  public boolean cooldownReady = true;

    //Player Stats
    private float movementSpeed;
    private float maxSpeed;
    private int maxHealth = 100;
    private float attackSpeed;
    private Boolean gameOver = false;
//==============================================================================
//Methods
//==============================================================================

    public Player(PlayScreen screen) {
        super(screen.getAtlas().findRegion("Player48x64"));
        startPosX = 180;
        startPosY = 60;
        playerWidth = 48;
        playerHeight = 64;
        directionRight = true;
        this.world = screen.getWorld();
        this.screen = screen;

        //Player Stats
        movementSpeed = 3.0f;
        maxSpeed = 4.0f;
        health = 100;
        attackSpeed = 1f;

        //healing
        isHealing = false;
        healingFactor = 0.05f;

        //Attack
        isAttacking = false;
        shards = new ArrayList<Shard>();
        shapeRenderer = new ShapeRenderer();

        definePlayer();
        //Animation
        playerStand = new TextureRegion(getTexture(), 0, 0, playerWidth, playerHeight);
        setBounds(0, 0, playerWidth / Main.PPM, playerHeight / Main.PPM);
        setRegion(playerStand);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        currentElement = startingElement;

        //currentShootDirection = Player.shootDirection.RIGHT;
        frames = new Array<TextureRegion>();
        generateFrames();
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
        fDef.filter.categoryBits = Main.PLAYER_BIT;
        fDef.filter.maskBits = /*Main.GROUND_BIT | */ //Needed?
                Main.BONFIRE_BIT
                | Main.ENEMY_BIT
                | Main.ENEMYMELEEATTACK_BIT
                | Main.CRYSTAL_BIT
                | Main.OBJECT_BIT;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef).setUserData(this);
    }

    public void update(float dt) {
        setPosition(b2Body.getPosition().x - ((getWidth() + ((currentState == Player.State.ATTACK) ? (directionRight ? +16 : -16) / Main.PPM : 0)) / 2), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if (isHealing && health < maxHealth) {
            healing();
        }
        if(health <= 0) {
            gameOver = true;
        } 
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
//        if (currentState == Player.State.ATTACK && !isAttacking) {
//            currentState = getState();
//        } else if (currentState != Player.State.ATTACK) {
//            currentState = getState();
//        }
        getCurrentElement();
        TextureRegion region;
        switch (currentState) {
            case STANDING:
                region = playerCurrentElementStanding.getKeyFrame(stateTimer);
                break;
            case UP:
                region = playerCurrentElementRunning.getKeyFrame(stateTimer, true);
                break;
            case DOWN:
                region = playerCurrentElementRunning.getKeyFrame(stateTimer, true);
                break;
            case RIGHT:
                region = playerCurrentElementRunning.getKeyFrame(stateTimer, true);
                break;
            case LEFT:
                region = playerCurrentElementRunning.getKeyFrame(stateTimer, true);
                break;
            case ATTACK:
                region = playerCurrentElementAttack.getKeyFrame(stateTimer, true);
                if (!region.isFlipX() && currentShootDirection == Player.shootDirection.RIGHT) {
                    region.flip(true, false);
                } else if (region.isFlipX() && currentShootDirection == Player.shootDirection.LEFT) {
                    region.flip(true, false);
                }
                if (playerCurrentElementAttack.isAnimationFinished(stateTimer) && previousState == Player.State.ATTACK) {
                    stateTimer = 0;
                    isAttacking = false;
                    switch (currentShootDirection) {
                        case RIGHT:
                            shards.add(new Shard(b2Body.getPosition().x, b2Body.getPosition().y, screen, shootDirection.RIGHT, currentElement));
                            directionRight = true;
                            break;
                        case LEFT:
                            shards.add(new Shard(b2Body.getPosition().x, b2Body.getPosition().y, screen, shootDirection.LEFT, currentElement));
                            directionRight = false;
                            break;
                        case UP:
                            shards.add(new Shard(b2Body.getPosition().x, b2Body.getPosition().y, screen, shootDirection.UP, currentElement));
                            break;
                        case DOWN:
                            shards.add(new Shard(b2Body.getPosition().x, b2Body.getPosition().y, screen, shootDirection.DOWN, currentElement));
                            break;
                    }
                    if (b2Body.getLinearVelocity().x < 0.00001 && b2Body.getLinearVelocity().x > -0.00001) {
                        b2Body.setLinearVelocity(0, b2Body.getLinearVelocity().y);
                    }
                }
                break;
            default:
                region = playerStand;
                break;
        }
        if (previousState == Player.State.ATTACK && currentState != Player.State.ATTACK && !isAttacking) {
            this.setBounds(getX() + (directionRight ? 16 : 0) / Main.PPM, getY(), playerWidth / Main.PPM, getHeight());
        }
        if (!isAttacking) {
            if (b2Body.getLinearVelocity().x != 0) {
                //RIGHT
                if ((b2Body.getLinearVelocity().x > 0) && !region.isFlipX()) {
                    region.flip(true, false);
                    directionRight = true;
                } else if ((b2Body.getLinearVelocity().x < 0) && region.isFlipX()) {
                    region.flip(true, false);
                    directionRight = false;
                }
            } else if (directionRight) {
                if (!region.isFlipX()) {
                    region.flip(true, false);
                }
            } else if (region.isFlipX()) {
                region.flip(true, false);
            }
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        if (isAttacking || (!isAttacking && currentState != Player.State.ATTACK)) {
            stateTimer = currentState == previousState ? stateTimer + dt : 0;
        }
        //update previous state
        previousState = currentState;
        return region;

    }

    public State getState() {
        if (isAttacking) {
            return Player.State.ATTACK;
        } else if (b2Body.getLinearVelocity().y > 0.001) {
            return State.UP;
        } else if (b2Body.getLinearVelocity().y < -0.001) {
            return State.DOWN;
        } else if (b2Body.getLinearVelocity().x > 0.001) {
            return State.RIGHT;
        } else if (b2Body.getLinearVelocity().x < -0.001) {
            return State.LEFT;
        } else {
            return State.STANDING;
        }
    }

    public void handleInput(int cooldownValue) {
        //LinearImpulse:first:x/y,second: where to impulse from the body?->center!, third: will impulse awake obj?
        if ((Gdx.input.isKeyPressed(Input.Keys.W) /*|| (Gdx.input.isKeyPressed(Input.Keys.UP))*/) && this.b2Body.getLinearVelocity().y <= this.getMaxSpeed()) {
            //Check if Player isnt moving faster than he is allowed to 
            this.b2Body.applyLinearImpulse(new Vector2(0, this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.A) /*|| (Gdx.input.isKeyPressed(Input.Keys.LEFT))*/) && this.b2Body.getLinearVelocity().x >= -this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(-this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.S) /*|| (Gdx.input.isKeyPressed(Input.Keys.DOWN))*/) && this.b2Body.getLinearVelocity().y >= -this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(0, -this.getMovementSpeed()), this.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.D) /*|| (Gdx.input.isKeyPressed(Input.Keys.RIGHT))*/) && this.b2Body.getLinearVelocity().x <= this.getMaxSpeed()) {
            this.b2Body.applyLinearImpulse(new Vector2(this.getMovementSpeed(), 0), this.b2Body.getWorldCenter(), true);
        }

        this.b2Body.setLinearDamping(60.0f);//Slow down

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            isAttacking = true;
            this.setBounds(getX() / Main.PPM, getY(), (playerWidth + 16) / Main.PPM, getHeight());
            currentShootDirection = shootDirection.RIGHT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            isAttacking = true;
            this.setBounds(getX() / Main.PPM, getY(), (playerWidth + 16) / Main.PPM, getHeight());
            currentShootDirection = shootDirection.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            isAttacking = true;
            this.setBounds(getX() / Main.PPM, getY(), (playerWidth + 16) / Main.PPM, getHeight());
            currentShootDirection = shootDirection.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            isAttacking = true;
            this.setBounds(getX() / Main.PPM, getY(), (playerWidth + 16) / Main.PPM, getHeight());
            currentShootDirection = shootDirection.DOWN;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && cooldownValue <= 0) {
            currentElement = Main.elementType.WATER;
            Main.cooldownReady = false;
         //   setCooldownReady(false);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && cooldownValue <= 0) {
            currentElement = Main.elementType.FIRE;
            Main.cooldownReady = false;
        //    setCooldownReady(false);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && cooldownValue <= 0) {
            currentElement = Main.elementType.EARTH;
            Main.cooldownReady = false;
        //    setCooldownReady(false);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && cooldownValue <= 0) {
            currentElement = Main.elementType.AIR;
            Main.cooldownReady = false;
        //    setCooldownReady(false);
        }
    }

    public int getHealth() {
        return (int) health;
    }

    public void setIsHealing(boolean h) {
        isHealing = h;
    }

    public void setHealingFactor(int factor) {
        healingFactor = factor;
    }

    public void increaseHealth(int increase) {
        health += increase;
    }

    public void decreaseHealth(int decrease) {
        health -= decrease;
    }

    public ArrayList<Shard> getIceShards() {
        return shards;
    }

    public void healing() {
        health += healingFactor;
    }

    public void generateFrames() {
        //----------------------------------------------------------------------
        //WATER
        //----------------------------------------------------------------------
        //STANDING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageWater.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerWaterStanding = new Animation(0.1f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageWaterWalk.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerWaterRunning = new Animation(.1f, frames, LOOP);
        frames.clear();
        //ATTACK
        for (int i = 0; i < 14; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageWaterAttack.png"), i * 64, 0, 64, playerHeight));
        }
        playerWaterAttack = new Animation(.05f / attackSpeed, frames);
        frames.clear();

        //----------------------------------------------------------------------
        //Fire
        //----------------------------------------------------------------------
        //STANDING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageFire.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerFireStanding = new Animation(0.1f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageFireWalk.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerFireRunning = new Animation(.1f, frames, LOOP);
        frames.clear();
        //ATTACK
        for (int i = 0; i < 14; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageFireAttack.png"), i * 64, 0, 64, playerHeight));
        }
        playerFireAttack = new Animation(.05f / attackSpeed, frames);
        frames.clear();

        //----------------------------------------------------------------------
        //Earth
        //----------------------------------------------------------------------
        //STANDING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageEarth.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerEarthStanding = new Animation(0.1f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageEarthWalk.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerEarthRunning = new Animation(.1f, frames, LOOP);
        frames.clear();
        //ATTACK
        for (int i = 0; i < 14; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageEarthAttack.png"), i * 64, 0, 64, playerHeight));
        }
        playerEarthAttack = new Animation(.05f / attackSpeed, frames);
        frames.clear();

        //----------------------------------------------------------------------
        //Air
        //----------------------------------------------------------------------
        //STANDING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageAir.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerAirStanding = new Animation(0.1f, frames, LOOP);
        frames.clear();
        //RUNNING
        for (int i = 0; i < 9; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageAirWalk.png"), i * playerWidth, 0, playerWidth, playerHeight));
        }
        playerAirRunning = new Animation(.1f, frames, LOOP);
        frames.clear();
        //ATTACK
        for (int i = 0; i < 14; i++) {
            //change to getTexture() later
            frames.add(new TextureRegion(new Texture("Graphics/Character/mageAirAttack.png"), i * 64, 0, 64, playerHeight));
        }
        playerAirAttack = new Animation(.05f / attackSpeed, frames);
        frames.clear();
    }

    public void getCurrentElement() {
        if (currentElement == Main.elementType.WATER) {
            playerCurrentElementStanding = playerWaterStanding;
            playerCurrentElementRunning = playerWaterRunning;
            playerCurrentElementAttack = playerWaterAttack;
        } else if (currentElement == Main.elementType.FIRE) {
            playerCurrentElementStanding = playerFireStanding;
            playerCurrentElementRunning = playerFireRunning;
            playerCurrentElementAttack = playerFireAttack;
        } else if (currentElement == Main.elementType.EARTH) {
            playerCurrentElementStanding = playerEarthStanding;
            playerCurrentElementRunning = playerEarthRunning;
            playerCurrentElementAttack = playerEarthAttack;
        } else if (currentElement == Main.elementType.AIR) {
            playerCurrentElementStanding = playerAirStanding;
            playerCurrentElementRunning = playerAirRunning;
            playerCurrentElementAttack = playerAirAttack;
        }

    }
    
    public Boolean getGameOver(){
        return gameOver;
    }

}
