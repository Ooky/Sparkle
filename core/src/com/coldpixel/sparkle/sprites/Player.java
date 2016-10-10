package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
 * @author Creat-if
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
	private shootDirection currentShootDirection;
	private float health;
	private World world;
	private PlayScreen screen;
	public Body b2Body;

	//Player Stats
	private enum State {

		STANDING, UP, DOWN, RIGHT, LEFT, ATTACK,
	};
	public Main.elementType currentElement;
	private Main.elementType startingElement = Main.elementType.WATER;

	private State currentState;
	private State previousState;

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

	//Texture
	private Texture characterTexture = new Texture("Graphics/Character/AnimationAll.png");
	//TextureRegion
	private TextureRegion[] mageWaterIdle;
	private TextureRegion[] mageWaterAttack;
	private TextureRegion[] mageWaterWalk;

	private TextureRegion[] mageFireIdle;
	private TextureRegion[] mageFireAttack;
	private TextureRegion[] mageFireWalk;

	private TextureRegion[] mageAirIdle;
	private TextureRegion[] mageAirAttack;
	private TextureRegion[] mageAirWalk;

	private TextureRegion[] mageEarthIdle;
	private TextureRegion[] mageEarthAttack;
	private TextureRegion[] mageEarthWalk;

	private float stateTimer;
	Array<TextureRegion> frames;
	private TextureRegion playerStand;

	//healing
	private float healingFactor;
	private Boolean isHealing;
	//Attack
	private Boolean isAttacking;
	private ArrayList<Shard> shards;

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
		startPosX = 580;
		startPosY = 280;
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
		healingFactor = 0.1f;

		//Attack
		isAttacking = false;
		shards = new ArrayList<Shard>();

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

		initializeAllAnimations();
		generateFrames();
	}

	private void definePlayer() {
		BodyDef bDef = new BodyDef();
		bDef.position.set(startPosX / Main.PPM, startPosY / Main.PPM);
		bDef.type = BodyDef.BodyType.DynamicBody;
		b2Body = world.createBody(bDef);

		FixtureDef fDef = new FixtureDef();
		PolygonShape rectangleShape = new PolygonShape();
		rectangleShape.setAsBox(playerWidth / 2 / Main.PPM, playerHeight / 2 / Main.PPM); //Starts from the center
		fDef.filter.categoryBits = Main.PLAYER_BIT;
		fDef.filter.maskBits
				= Main.BONFIRE_BIT
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
		if (health <= 0) {
			gameOver = true;
		}
	}

//==============================================================================
//Getter
//==============================================================================
	private TextureRegion getFrame(float dt) {
		currentState = getState();
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

	private State getState() {
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
		if ((Gdx.input.isKeyPressed(Input.Keys.W)) && this.b2Body.getLinearVelocity().y <= maxSpeed) {
			//Check if Player isnt moving faster than he is allowed to 
			this.b2Body.applyLinearImpulse(new Vector2(0, movementSpeed), this.b2Body.getWorldCenter(), true);
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.A)) && this.b2Body.getLinearVelocity().x >= -maxSpeed) {
			this.b2Body.applyLinearImpulse(new Vector2(-movementSpeed, 0), this.b2Body.getWorldCenter(), true);
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.S)) && this.b2Body.getLinearVelocity().y >= -maxSpeed) {
			this.b2Body.applyLinearImpulse(new Vector2(0, -movementSpeed), this.b2Body.getWorldCenter(), true);
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.D)) && this.b2Body.getLinearVelocity().x <= maxSpeed) {
			this.b2Body.applyLinearImpulse(new Vector2(movementSpeed, 0), this.b2Body.getWorldCenter(), true);
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

	public void decreaseHealth(int decrease) {
		health -= decrease;
	}

	public ArrayList<Shard> getIceShards() {
		return shards;
	}

	public void healing() {
		health += healingFactor;
	}

	private void generateFrames() {
		//----------------------------------------------------------------------
		//WATER
		//----------------------------------------------------------------------
		//STANDING
		for (int i = 0; i < 9; i++) {
			frames.add(mageWaterIdle[i]);
		}
		playerWaterStanding = new Animation(0.1f, frames, LOOP);
		frames.clear();
		//RUNNING
		for (int i = 0; i < 9; i++) {
			frames.add(mageWaterWalk[i]);
		}
		playerWaterRunning = new Animation(.1f, frames, LOOP);
		frames.clear();
		//ATTACK
		for (int i = 0; i < 14; i++) {
			frames.add(mageWaterAttack[i]);
		}
		playerWaterAttack = new Animation(.05f / attackSpeed, frames);
		frames.clear();

		//----------------------------------------------------------------------
		//Fire
		//----------------------------------------------------------------------
		//STANDING
		for (int i = 0; i < 9; i++) {
			frames.add(mageFireIdle[i]);
		}
		playerFireStanding = new Animation(0.1f, frames, LOOP);
		frames.clear();
		//RUNNING
		for (int i = 0; i < 9; i++) {
			frames.add(mageFireWalk[i]);
		}
		playerFireRunning = new Animation(.1f, frames, LOOP);
		frames.clear();
		//ATTACK
		for (int i = 0; i < 14; i++) {
			frames.add(mageFireAttack[i]);
		}
		playerFireAttack = new Animation(.05f / attackSpeed, frames);
		frames.clear();

		//----------------------------------------------------------------------
		//Earth
		//----------------------------------------------------------------------
		//STANDING
		for (int i = 0; i < 9; i++) {
			frames.add(mageEarthIdle[i]);
		}
		playerEarthStanding = new Animation(0.1f, frames, LOOP);
		frames.clear();
		//RUNNING
		for (int i = 0; i < 9; i++) {
			frames.add(mageEarthWalk[i]);
		}
		playerEarthRunning = new Animation(.1f, frames, LOOP);
		frames.clear();
		//ATTACK
		for (int i = 0; i < 14; i++) {
			frames.add(mageEarthAttack[i]);
		}
		playerEarthAttack = new Animation(.05f / attackSpeed, frames);
		frames.clear();

		//----------------------------------------------------------------------
		//Air
		//----------------------------------------------------------------------
		//STANDING
		for (int i = 0; i < 9; i++) {
			frames.add(mageAirIdle[i]);
		}
		playerAirStanding = new Animation(0.1f, frames, LOOP);
		frames.clear();
		//RUNNING
		for (int i = 0; i < 9; i++) {
			frames.add(mageAirWalk[i]);
		}
		playerAirRunning = new Animation(.1f, frames, LOOP);
		frames.clear();
		//ATTACK
		for (int i = 0; i < 14; i++) {
			frames.add(mageAirAttack[i]);
		}
		playerAirAttack = new Animation(.05f / attackSpeed, frames);
		frames.clear();
	}

	private void getCurrentElement() {
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

	public Boolean getGameOver() {
		return gameOver;
	}

	private void initializeAllAnimations() {
		mageWaterIdle();
		mageWaterAttack();
		mageWaterWalk();

		mageFireIdle();
		mageFireAttack();
		mageFireWalk();

		mageAirIdle();
		mageAirAttack();
		mageAirWalk();

		mageEarthIdle();
		mageEarthAttack();
		mageEarthWalk();
	}

//Mage Air Yellow
//------------------------------------------------------------------------------
	private TextureRegion[] mageAirIdle() {
		mageAirIdle = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageAirIdle[j] = new TextureRegion(characterTexture, i, 464, playerWidth, playerHeight);
			j++;
		}
		return mageAirIdle;
	}

	private TextureRegion[] mageAirAttack() {
		mageAirAttack = new TextureRegion[15];
		int j = 0;
		for (int i = 0; i <= 896; i += 64) {
			mageAirAttack[j] = new TextureRegion(characterTexture, i, 728, 64, playerHeight);
			j++;
		}
		return mageAirAttack;
	}

	private TextureRegion[] mageAirWalk() {
		mageAirWalk = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageAirWalk[j] = new TextureRegion(characterTexture, i, 398, playerWidth, playerHeight);
			j++;
		}
		return mageAirWalk;
	}

//Mage Fire Red
//------------------------------------------------------------------------------
	private TextureRegion[] mageFireIdle() {
		mageFireIdle = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageFireIdle[j] = new TextureRegion(characterTexture, i, 200, playerWidth, playerHeight);
			j++;
		}
		return mageFireIdle;
	}

	private TextureRegion[] mageFireAttack() {
		mageFireAttack = new TextureRegion[15];
		int j = 0;
		for (int i = 0; i <= 896; i += 64) {
			mageFireAttack[j] = new TextureRegion(characterTexture, i, 595, 64, playerHeight);
			j++;
		}
		return mageFireAttack;
	}

	private TextureRegion[] mageFireWalk() {
		mageFireWalk = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageFireWalk[j] = new TextureRegion(characterTexture, i, 134, playerWidth, playerHeight);
			j++;
		}
		return mageFireWalk;
	}

//Mage Earth Green
//------------------------------------------------------------------------------
	private TextureRegion[] mageEarthIdle() {
		mageEarthIdle = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageEarthIdle[j] = new TextureRegion(characterTexture, i, 332, playerWidth, playerHeight);
			j++;
		}
		return mageEarthIdle;
	}

	private TextureRegion[] mageEarthAttack() {
		mageEarthAttack = new TextureRegion[15];
		int j = 0;
		for (int i = 0; i <= 896; i += 64) {
			mageEarthAttack[j] = new TextureRegion(characterTexture, i, 662, 64, playerHeight);
			j++;
		}
		return mageEarthAttack;
	}

	private TextureRegion[] mageEarthWalk() {
		mageEarthWalk = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageEarthWalk[j] = new TextureRegion(characterTexture, i, 266, playerWidth, playerHeight);
			j++;
		}
		return mageEarthWalk;
	}

//Mage Water Blue
//------------------------------------------------------------------------------
	private TextureRegion[] mageWaterIdle() {
		mageWaterIdle = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageWaterIdle[j] = new TextureRegion(characterTexture, i, 68, playerWidth, playerHeight);
			j++;
		}
		return mageWaterIdle;
	}

	private TextureRegion[] mageWaterAttack() {
		mageWaterAttack = new TextureRegion[15];
		int j = 0;
		for (int i = 0; i <= 896; i += 64) {
			mageWaterAttack[j] = new TextureRegion(characterTexture, i, 530, 64, playerHeight);
			j++;
		}
		return mageWaterAttack;
	}

	private TextureRegion[] mageWaterWalk() {
		mageWaterWalk = new TextureRegion[10];
		int j = 0;
		for (int i = 0; i <= 432; i += 48) {
			mageWaterWalk[j] = new TextureRegion(characterTexture, i, 0, playerWidth, playerHeight);
			j++;
		}
		return mageWaterWalk;
	}
}
