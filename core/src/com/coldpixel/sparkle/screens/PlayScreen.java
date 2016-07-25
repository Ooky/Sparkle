package com.coldpixel.sparkle.screens;

import com.coldpixel.sparkle.sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.scenes.Hud;
import com.coldpixel.sparkle.tools.B2WorldCreator;
import com.coldpixel.sparkle.tools.WorldContactListener;

/**
 *
 * @author Coldpixel
 */
public class PlayScreen implements Screen {

//==============================================================================
//Initialization
//==============================================================================   
    private Main main;
    private OrthographicCamera cam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2DebugRenderer;
    private B2WorldCreator b2WorldCreator;

    //Character
    private Player player;
    private TextureAtlas atlas;

//==============================================================================
//Methods
//==============================================================================
    public PlayScreen(Main main) {
        atlas = new TextureAtlas("Player_and_Enemies.pack");

        this.main = main;
        cam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, cam);
        hud = new Hud(main.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiledmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);
        cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);//zero-gravity, sleep=true
        b2DebugRenderer = new Box2DDebugRenderer();

        b2WorldCreator = new B2WorldCreator(world, map);

        //Player
        player = new Player(world, this);
        
        world.setContactListener(new WorldContactListener());

    }

    public void handleInput(float dt) {
        //LinearImpulse:first:x/y,second: where to impulse from the body?->center!, third: will impulse awake obj?
        if ((Gdx.input.isKeyPressed(Input.Keys.W) || (Gdx.input.isKeyPressed(Input.Keys.UP))) && player.b2Body.getLinearVelocity().y <= player.getMaxSpeed()) {
            //Check if Player isnt moving faster than he is allowed to 
            player.b2Body.applyLinearImpulse(new Vector2(0, player.getMovementSpeed()), player.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.A) || (Gdx.input.isKeyPressed(Input.Keys.LEFT))) && player.b2Body.getLinearVelocity().x >= -player.getMaxSpeed()) {
            player.b2Body.applyLinearImpulse(new Vector2(-player.getMovementSpeed(), 0), player.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.S) || (Gdx.input.isKeyPressed(Input.Keys.DOWN))) && player.b2Body.getLinearVelocity().y >= -player.getMaxSpeed()) {
            player.b2Body.applyLinearImpulse(new Vector2(0, -player.getMovementSpeed()), player.b2Body.getWorldCenter(), true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.D) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT))) && player.b2Body.getLinearVelocity().x <= player.getMaxSpeed()) {
            player.b2Body.applyLinearImpulse(new Vector2(player.getMovementSpeed(), 0), player.b2Body.getWorldCenter(), true);
        }
//        player.b2Body.setLinearVelocity(new Vector2(0,0));//Stop immediatly.
        player.b2Body.setLinearDamping(60.0f);//Slow down
    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);//60 times a second

        player.update(dt);

        cam.update();
        renderer.setView(cam);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render gamemap
        renderer.render();
        //render Box2DDebugLines
        b2DebugRenderer.render(world, cam.combined);

        main.batch.setProjectionMatrix(cam.combined);
        main.batch.begin();
        player.draw(main.batch);
        main.batch.end();

        hud.drawHUD();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); //Adjust the Viewport
        hud.stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2DebugRenderer.dispose();
        hud.dispose();
    }

//==============================================================================
//Getter
//==============================================================================
    public TextureAtlas getAtlas() {
        return atlas;

    }

}
