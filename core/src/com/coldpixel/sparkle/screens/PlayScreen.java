package com.coldpixel.sparkle.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.coldpixel.sparkle.sprites.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.scenes.Hud;
import com.coldpixel.sparkle.sprites.BonFire;
import com.coldpixel.sparkle.sprites.Enemy;
import com.coldpixel.sparkle.tools.AssetHelper;
import com.coldpixel.sparkle.tools.B2WorldCreator;
import com.coldpixel.sparkle.tools.WorldContactListener;
import com.coldpixel.sparkle.sprites.Shard;
import com.coldpixel.sparkle.sprites.Crystal;
import com.coldpixel.sparkle.sprites.Soldier;
import com.coldpixel.sparkle.tools.Wave;
import java.util.ArrayList;

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
    private float ambientLight;

    // private PointLight myLight;
    //Character
    private Player player;
    private TextureAtlas atlas;

    //DayNightCycle
    private long startTime;
    boolean isDay;
    private long cycleTime;

    //Music
    private AssetHelper assetHelper;

	//Wave
	private Wave waveHandler;
	private ArrayList<Soldier> soldiersArray;
//==============================================================================
//Methods
//==============================================================================
    public PlayScreen(Main main) {

        atlas = new TextureAtlas("Player_and_Enemies.pack");
        startTime = 0;
        ambientLight = 0.8f;
        isDay = true;
        cycleTime = TimeUtils.nanoTime();
		soldiersArray = new ArrayList<Soldier>();
		
        this.main = main;
        cam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, cam);
        hud = new Hud(main.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiledmap2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Main.PPM);
        cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);//zero-gravity, sleep=true
        b2DebugRenderer = new Box2DDebugRenderer();

        player = new Player(this);		
		waveHandler = new Wave(this,player);
        b2WorldCreator = new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.8f);

        for (BonFire boneFire : b2WorldCreator.getBonFires()) {
            pointLight = new PointLight(rayHandler, 100, Color.ORANGE, boneFire.getWidth() / Main.PPM * 4, 0, 0);
            pointLight.setSoftnessLength(0.5f);
            pointLight.attachToBody(boneFire.getBody(), 0, 0);
            pointLight.setIgnoreAttachedBody(true);
            boneFire.setPointLight(pointLight);
        }

        //Music and Sound
        assetHelper = new AssetHelper();
        assetHelper.musicBackground(true);
        assetHelper.soundBonfire(true);
    }

    public void update(float dt) {
        player.handleInput(hud.cooldownValue);
        //hud.setCooldownReady(player.getCooldownReady());
        world.step(1 / 60f, 6, 2);//60 times a second
//        rayHandler.update();
        player.update(dt);
		waveHandler.update();		
		for(Enemy enemy : soldiersArray){
			enemy.update(dt, hud);
		}
        for (Enemy enemy : b2WorldCreator.getSoldiers()) {
            enemy.update(dt, hud);
        }
        //bonfire animation
        for (BonFire boneFire : b2WorldCreator.getBonFires()) {
            boneFire.update(dt);
        }
        for (Shard shard : player.getIceShards()) {
            shard.update(dt);
        }
        for (Crystal crystal : b2WorldCreator.getCrystals()) {
            crystal.update(dt);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            assetHelper.stopMusic();
            assetHelper.stopSound();
            main.setScreen(new StartGame(main));
        }
		
        cam.update();
        renderer.setView(cam);
    }

    private void dayNightCycle() {
//        System.out.println(ambientLight);
//        System.out.println(TimeUtils.timeSinceNanos(cycleTime));
//        System.out.println(TimeUtils.timeSinceNanos(startTime));
//        System.out.println(toggleDayTime);
        if (TimeUtils.timeSinceNanos(cycleTime) > 900000000000L && !isDay) {//15Min  900000000000L
            if (TimeUtils.timeSinceNanos(startTime) > 50000000L) {//1Sec= 1000000000
                ambientLight += 0.005f;
                rayHandler.setAmbientLight(ambientLight);
                startTime = TimeUtils.nanoTime();
                if (ambientLight >= 0.8f) {
                    startTime = 0;
                    isDay = true;
                    cycleTime = TimeUtils.nanoTime();
                }
            }
        } else if (TimeUtils.timeSinceNanos(cycleTime) > 450000000000L && isDay) {//7.5Min  450000000000L
            if (TimeUtils.timeSinceNanos(startTime) > 50000000L) {
                ambientLight -= 0.005f;
                rayHandler.setAmbientLight(ambientLight);
                startTime = TimeUtils.nanoTime();
                if (ambientLight <= 0.08f) {
                    startTime = 0;
                    isDay = false;
                    cycleTime = 0;
                }
            }
        }

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
        rayHandler.setCombinedMatrix(cam);
        dayNightCycle();

        main.batch.begin();
		for(Enemy enemy : soldiersArray){
			enemy.draw(main.batch);
		}
        for (Enemy enemy : b2WorldCreator.getSoldiers()) {
            enemy.draw(main.batch);
        }
        for (Crystal crystal : b2WorldCreator.getCrystals()) {
            crystal.draw(main.batch);
        }
        player.draw(main.batch);
        main.batch.end();
        rayHandler.updateAndRender();
//        rayHandler.render();
        main.batch.begin();
        for (BonFire boneFire : b2WorldCreator.getBonFires()) {
            boneFire.draw(main.batch);
        }
        for (Shard ice : player.getIceShards()) {
            ice.draw(main.batch);
        }
        for (Crystal crystal : b2WorldCreator.getCrystals()) {
            crystal.draw(main.batch);
        }
        main.batch.end();

        hud.drawHUD(player.getHealth());
        hud.drawActionbar(main.batch, player.currentElement);
        hud.drawCooldown(player.currentElement);
        if (player.getGameOver()) {
            main.setScreen(new GameOver(main, hud.getScoreValue(), Soldier.getDeathCounter(), assetHelper));
            assetHelper.dispose();
            dispose();
        }
    }

    @Override
    public void show() {
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
        assetHelper.dispose();
    }

//==============================================================================
//Getter
//==============================================================================
    public TextureAtlas getAtlas() {
        return atlas;

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Main getMain() {
        return main;
    }

    public Player getPlayer() {
        return player;
    }

	public void addSoldierArray(ArrayList<Soldier> arr){
		this.soldiersArray.addAll(arr);
	}
}
