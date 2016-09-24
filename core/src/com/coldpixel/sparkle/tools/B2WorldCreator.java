package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.sprites.BonFire;
import com.coldpixel.sparkle.screens.PlayScreen;
import com.coldpixel.sparkle.sprites.Dirt;
import com.coldpixel.sparkle.sprites.EnvironmentObject;
import com.coldpixel.sparkle.sprites.Grass;
import com.coldpixel.sparkle.sprites.Soldier;
import com.coldpixel.sparkle.sprites.Crystal;
import com.coldpixel.sparkle.sprites.Player;
import java.util.ArrayList;

/**
 *
 * @author Coldpixel
 */
public class B2WorldCreator {

//==============================================================================
//Initialization
//============================================================================== 
    private BodyDef bDef;
    private PolygonShape polygonShape;
    private FixtureDef fDef;
    private Body body;

    private Grass grass;
    private Dirt dirt;
    private EnvironmentObject environmentObject;
    
    private ArrayList<BonFire> bonefires;
    private BonFire boneFire;
    
    private ArrayList<Soldier> soldiers;
    private Soldier soldier;
    
    private ArrayList<Crystal> crystals;
    private Crystal crystal;
    
    private AssetHelper assetHelper;
//==============================================================================
//Methods
//==============================================================================  
    public B2WorldCreator(PlayScreen playScreen) {
        World world = playScreen.getWorld();
        TiledMap map = playScreen.getMap();
//        Main main = playScreen.getMain();
        assetHelper = new AssetHelper();
        bDef = new BodyDef();
        polygonShape = new PolygonShape();
        fDef = new FixtureDef();

        //Create Grass
        for (MapObject object : map.getLayers().get(0).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            grass = new Grass(playScreen, rect);
        }

        //Create Dirt
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            dirt = new Dirt(playScreen, rect);
        }
        
        //Create Object
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            environmentObject = new EnvironmentObject(playScreen, rect);
        }

        //Create hitbox
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / Main.PPM, (rect.getY() + rect.getHeight() / 2) / Main.PPM);

            body = world.createBody(bDef);

            polygonShape.setAsBox((rect.getWidth() / 2) / Main.PPM, (rect.getHeight() / 2) / Main.PPM);
            fDef.shape = polygonShape;
            fDef.filter.categoryBits = Main.OBJECT_BIT;
            body.createFixture(fDef).setUserData(rect);
        }
        
        //Create light
        bonefires = new ArrayList<BonFire>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(EllipseMapObject.class)) { 
            boneFire = new BonFire(((EllipseMapObject) object), playScreen);
            bonefires.add(boneFire);
        }
        
        //Create Soldiers
        soldiers = new ArrayList<Soldier>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) { 
            soldier = new Soldier(playScreen, ((RectangleMapObject)object).getRectangle().getX() / Main.PPM,
                    ((RectangleMapObject)object).getRectangle().getY() / Main.PPM, playScreen.getPlayer(), Soldier.elementType.WATER);
            soldiers.add(soldier);
        }
        
        //Create Soldiers
        crystals = new ArrayList<Crystal>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) { 
            crystal = new Crystal(playScreen, ((RectangleMapObject)object).getRectangle().getX(),
                    ((RectangleMapObject)object).getRectangle().getY());
            crystals.add(crystal);
        }
    }
    
    public ArrayList<BonFire> getBonFires(){
        return bonefires;
    }
    
        public ArrayList<Soldier> getSoldiers(){
        return soldiers;
    }
        
    public ArrayList<Crystal> getCrystals(){
        return crystals;
    }
}
