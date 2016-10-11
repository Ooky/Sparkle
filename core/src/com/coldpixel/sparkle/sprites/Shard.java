package com.coldpixel.sparkle.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
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

/**
 *
 * @author Creat-if
 */
public final class Shard extends Sprite {

	private int width;
	private int height;
	private float x;
	private float y;
	private World world;
	private Body b2Body;
	private Player.shootDirection shootDirection;
	private int damage;
	private Boolean setToDestroy;
	private Boolean destroyed;
	private short velocity;
	private Main.elementType element;
	//animation
	private float stateTime;
	private Animation waterShardAnimation;
	private Animation fireShardAnimation;
	private Animation earthShardAnimation;
	private Animation airShardAnimation;
	private Animation waterCollisionAnimation;
	private Animation fireCollisionAnimation;
	private Animation earthCollisionAnimation;
	private Animation airCollisionAnimation;
	private Array<TextureRegion> frames;
	private TextureRegion shard;
	private float flyAnimationSpeed = 0.1f;
	private float collisionAnimationSpeed = .015f;
	private short numbersOfFrames = 8;
	private short shardWidth = 32;
	private short shardHeight = 32;
	//Texture
	private Texture allShards = new Texture("Graphics/Attacks/ShardsALL.png");
	//TextureRegion
	private TextureRegion[] shardAir;
	private TextureRegion[] shardCollisionAir;
	private TextureRegion[] shardFire;
	private TextureRegion[] shardCollisionFire;
	private TextureRegion[] shardWater;
	private TextureRegion[] shardCollisionWater;
	private TextureRegion[] shardEarth;
	private TextureRegion[] shardCollisionEarth;

	public Shard(float x, float y, PlayScreen screen, Player.shootDirection direction, Main.elementType element) {
		this.x = x;
		this.y = y;
		width = 32;
		height = 32;
		world = screen.getWorld();
		shootDirection = direction;
		damage = 30;
		destroyed = false;
		setToDestroy = false;
		stateTime = 0;
		frames = new Array<TextureRegion>();
		velocity = 5;
		this.element = element;
		defineShard();

		initializeAllAnimations();

		generateFrames();

		setBounds(0, 0, width / Main.PPM, height / Main.PPM);
	}

	public void update(float dt) {
		stateTime += dt;

		getElementFrame();

		if (shootDirection == Player.shootDirection.LEFT && !shard.isFlipX()) {
			shard.flip(true, false);
		}
		if (setToDestroy && !destroyed) {
			world.destroyBody(b2Body);
			destroyed = true;
		} else if (!destroyed) {
			setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
		}
		setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
		setRegion(shard);

	}

	@Override
	public void draw(Batch batch) {
		if (!destroyed || stateTime < .1) {
			if (shootDirection == Player.shootDirection.UP) {
				this.rotate90(false);
			} else if (shootDirection == Player.shootDirection.DOWN) {
				this.rotate90(true);
			}
			super.draw(batch);
		}
	}

	protected void defineShard() {
		BodyDef bDef = new BodyDef();
		switch (shootDirection) {
			case RIGHT:
				bDef.position.set(x + 48 / Main.PPM, y);
				break;
			case LEFT:
				bDef.position.set(x - 48 / Main.PPM, y);
				break;
			case UP:
				bDef.position.set(x, y + 64 / Main.PPM);
				break;
			case DOWN:
				bDef.position.set(x, y - 64 / Main.PPM);
				break;
		}
		bDef.type = BodyDef.BodyType.DynamicBody;
		b2Body = world.createBody(bDef);

		FixtureDef fDef = new FixtureDef();
		PolygonShape rectangleShape = new PolygonShape();
		if (shootDirection == Player.shootDirection.RIGHT || shootDirection == Player.shootDirection.LEFT) {
			rectangleShape.setAsBox(30 / 2 / Main.PPM, 10 / 2 / Main.PPM); //Starts from the center
		} else {
			rectangleShape.setAsBox(10 / 2 / Main.PPM, 30 / 2 / Main.PPM); //Starts from the center
		}

		fDef.filter.categoryBits = Main.PLAYERATTACK_BIT;
		fDef.filter.maskBits = Main.GROUND_BIT
				| Main.BONFIRE_BIT
				| Main.ENEMY_BIT
				| Main.OBJECT_BIT;
		fDef.shape = rectangleShape;
		b2Body.createFixture(fDef).setUserData(this);

		switch (shootDirection) {
			case RIGHT:
				b2Body.applyLinearImpulse(new Vector2(velocity, 0), this.b2Body.getWorldCenter(), true);
				break;
			case LEFT:
				b2Body.applyLinearImpulse(new Vector2(-velocity, 0), this.b2Body.getWorldCenter(), true);
				break;
			case UP:
				b2Body.applyLinearImpulse(new Vector2(0, velocity), this.b2Body.getWorldCenter(), true);
				break;
			case DOWN:
				b2Body.applyLinearImpulse(new Vector2(0, -velocity), this.b2Body.getWorldCenter(), true);
				break;
		}
	}

	public int getDamage() {
		return damage;
	}

	public void destroy() {
		setToDestroy = true;
		stateTime = 0;
	}

	private void createWaterAnimation() {
		//fly Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardWater[i]);
		}
		waterShardAnimation = new Animation(flyAnimationSpeed, frames);
		frames.clear();
		//Collision Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardCollisionWater[i]);
		}
		waterCollisionAnimation = new Animation(collisionAnimationSpeed, frames);
		frames.clear();
	}

	private void createFireAnimation() {
		//fly Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardFire[i]);
		}
		fireShardAnimation = new Animation(flyAnimationSpeed, frames);
		frames.clear();
		//Collision Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardCollisionFire[i]);
		}
		fireCollisionAnimation = new Animation(collisionAnimationSpeed, frames);
		frames.clear();
	}

	private void createEarthAnimation() {
		//fly Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardEarth[i]);
		}
		earthShardAnimation = new Animation(flyAnimationSpeed, frames);
		frames.clear();
		//Collision Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardCollisionEarth[i]);
		}
		earthCollisionAnimation = new Animation(collisionAnimationSpeed, frames);
		frames.clear();
	}

	private void createAirAnimation() {
		//fly Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardAir[i]);
		}
		airShardAnimation = new Animation(flyAnimationSpeed, frames);
		frames.clear();
		//Collision Animation
		for (int i = 0; i < numbersOfFrames; i++) {
			frames.add(shardCollisionAir[i]);
		}
		airCollisionAnimation = new Animation(collisionAnimationSpeed, frames);
		frames.clear();
	}

	private void getElementFrame() {
		if (element == Main.elementType.WATER) {
			if (setToDestroy) {
				shard = waterCollisionAnimation.getKeyFrame(stateTime, true);
			} else {
				shard = waterShardAnimation.getKeyFrame(stateTime, true);
			}
		} else if (element == Main.elementType.FIRE) {
			if (setToDestroy) {
				shard = fireCollisionAnimation.getKeyFrame(stateTime, true);
			} else {
				shard = fireShardAnimation.getKeyFrame(stateTime, true);
			}
		} else if (element == Main.elementType.EARTH) {
			if (setToDestroy) {
				shard = earthCollisionAnimation.getKeyFrame(stateTime, true);
			} else {
				shard = earthShardAnimation.getKeyFrame(stateTime, true);
			}
		} else if (element == Main.elementType.AIR) {
			if (setToDestroy) {
				shard = airCollisionAnimation.getKeyFrame(stateTime, true);
			} else {
				shard = airShardAnimation.getKeyFrame(stateTime, true);
			}
		}

	}

	public Main.elementType getElement() {
		return this.element;
	}

	private TextureRegion[] shardAir() {
		shardAir = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardAir[j] = new TextureRegion(allShards, i, 0, shardWidth, shardHeight);
			j++;
		}
		return shardAir;
	}

	private TextureRegion[] shardCollisionAir() {
		shardCollisionAir = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardCollisionAir[j] = new TextureRegion(allShards, i, 128, shardWidth, shardHeight);
			j++;
		}
		return shardCollisionAir;
	}

	private TextureRegion[] shardFire() {
		shardFire = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardFire[j] = new TextureRegion(allShards, i, 64, shardWidth, shardHeight);
			j++;
		}
		return shardFire;
	}

	private TextureRegion[] shardCollisionFire() {
		shardCollisionFire = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardCollisionFire[j] = new TextureRegion(allShards, i, 192, shardWidth, shardHeight);
			j++;
		}
		return shardCollisionFire;
	}

	private TextureRegion[] shardWater() {
		shardWater = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardWater[j] = new TextureRegion(allShards, i, 32, shardWidth, shardHeight);
			j++;
		}
		return shardWater;
	}

	private TextureRegion[] shardCollisionWater() {
		shardCollisionWater = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardCollisionWater[j] = new TextureRegion(allShards, i, 160, shardWidth, shardHeight);
			j++;
		}
		return shardCollisionWater;
	}

	private TextureRegion[] shardEarth() {
		shardEarth = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardEarth[j] = new TextureRegion(allShards, i, 98, shardWidth, shardHeight);
			j++;
		}
		return shardEarth;
	}

	private TextureRegion[] shardCollisionEarth() {
		shardCollisionEarth = new TextureRegion[numbersOfFrames];
		int j = 0;
		for (int i = 0; i <= 224; i += shardWidth) {
			shardCollisionEarth[j] = new TextureRegion(allShards, i, 224, shardWidth, shardHeight);
			j++;
		}
		return shardCollisionEarth;
	}

	private void generateFrames() {
		createWaterAnimation();
		createAirAnimation();
		createFireAnimation();
		createEarthAnimation();

	}

	private void initializeAllAnimations() {
		shardAir();
		shardCollisionAir();
		shardFire();
		shardCollisionFire();
		shardWater();
		shardCollisionWater();
		shardEarth();
		shardCollisionEarth();
	}

}
