package Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;

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

//==============================================================================
//Methods
//==============================================================================
    public Player(World world) {
        startPosX = 180;
        startPosY = 60;
        playerWidth = 24;//Its 48, dunno why it is half
        playerHeight = 32;//Its 46, dunno why it is half
        this.world = world;
        movementSpeed = 3.0f;
        maxSpeed = 4.0f;
        definePlayer();
    }

    public void definePlayer() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(startPosX / Main.PPM, startPosY / Main.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleShape = new PolygonShape();
        rectangleShape.setAsBox(playerWidth / Main.PPM, playerHeight / Main.PPM);

//        CircleShape shape = new CircleShape();
//        shape.setRadius(50);
//        fDef.shape = shape;
        fDef.shape = rectangleShape;
        b2Body.createFixture(fDef);

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

}
