package com.coldpixel.sparkle.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.coldpixel.sparkle.sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.scenes.Hud;
import com.coldpixel.sparkle.sprites.BoneFire;
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
    
    //light
    private RayHandler rayHandler;
    private PointLight pointLight;
            
   // private PointLight myLight;

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

        player = new Player(world, this);

        world.setContactListener(new WorldContactListener());
        
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(.8f);

        for (BoneFire object: b2WorldCreator.getBoneFires()) {
            pointLight = new PointLight(rayHandler, 200, Color.WHITE, object.getWidth()/Main.PPM*6, (object.getPosX()+(object.getWidth()/2))/Main.PPM, (object.getPosY()+(object.getWidth()/2))/Main.PPM);
            pointLight.setSoftnessLength(0f);
            pointLight.attachToBody(object.getBody());
        }
    }

    public void update(float dt) {
        player.handleInput(dt);
        world.step(1 / 60f, 6, 2);//60 times a second
        rayHandler.update();
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
//        render Box2DDebugLines
        b2DebugRenderer.render(world, cam.combined);

        main.batch.setProjectionMatrix(cam.combined);
        rayHandler.setCombinedMatrix(cam);
        main.batch.begin();
        player.draw(main.batch);
        main.batch.end();
        rayHandler.render();
        player.targetLine();

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
        rayHandler.dispose();
    }

//==============================================================================
//Getter
//==============================================================================
    public TextureAtlas getAtlas() {
        return atlas;

    }

}
