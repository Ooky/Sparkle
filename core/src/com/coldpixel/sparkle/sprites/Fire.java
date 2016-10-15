package com.coldpixel.sparkle.sprites;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
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
public final class Fire extends Sprite {

    private float posX;
    private float posY;
    private float radius;
    private int width;
    private int height;
    private int scale;
    private Color color;
    private Ellipse e;
    private Body b2Body;
    private World world;
    private Animation burning;
    private float stateTimer;
    private Array<TextureRegion> frames;
    private PointLight pointLight;
    private float currentDistance;
    private float previousDistance;
    private float frameDuration;

    public Fire(EllipseMapObject ellipse, PlayScreen screen) {
        this.world = screen.getWorld();
        e = ellipse.getEllipse();
        posX = e.x;
        posY = e.y;
        radius = e.width;
        width = 32;
        height = 64;
        scale = 1;
        frameDuration = 0.15f;
        currentDistance = radius / Main.PPM * 4;
        previousDistance = currentDistance;
        defineBonFire();

        setBounds(0, 0, width / Main.PPM * scale, height / Main.PPM * scale);
        setPosition(b2Body.getPosition().x - (32 / 2 / Main.PPM), b2Body.getPosition().y - (32 / 2 / Main.PPM));
        stateTimer = 0;
        frames = new Array<TextureRegion>();
        //Animation
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(new Texture("Graphics/Terrain/Fire.png"), i * 32, 0, 32, 64));
        }
        burning = new Animation(frameDuration, frames, LOOP);
    }

    private void defineBonFire() {
        BodyDef bDef = new BodyDef();
        bDef.position.set((posX + radius / 2) / Main.PPM, (posY + radius / 2) / Main.PPM);
        bDef.type = BodyDef.BodyType.StaticBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        PolygonShape rectangleeShape = new PolygonShape();
        rectangleeShape.setAsBox(radius / 400f, radius / 400f);

        fDef.filter.categoryBits = Main.BONFIRE_BIT;
        fDef.shape = rectangleeShape;
        b2Body.createFixture(fDef).setUserData(new Rectangle(posX - radius / 2, posY - radius / 2, radius / 400f, radius / 400f));
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = burning.getKeyFrame(stateTimer);
        stateTimer = stateTimer + dt;
        return region;
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
        //light rays extend and shorten
        if (previousDistance >= radius / Main.PPM * 4) {
            currentDistance -= 1 / Main.PPM;
            if (currentDistance < radius / Main.PPM * 3) {
                previousDistance = currentDistance;
            }
        } else if (previousDistance <= radius / Main.PPM * 4) {
            currentDistance += 1 / Main.PPM;
            if (currentDistance > radius / Main.PPM * 3) {
                previousDistance = currentDistance;
            }
        }
        pointLight.setDistance(currentDistance);
    }

    @Override
    public float getWidth() {
        return radius;
    }

    public Body getBody() {
        return b2Body;
    }

    public void setPointLight(PointLight pLight) {
        pointLight = pLight;
    }
}
