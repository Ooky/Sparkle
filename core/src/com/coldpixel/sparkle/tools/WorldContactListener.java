package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.coldpixel.sparkle.Main;
import com.coldpixel.sparkle.sprites.IceShard;
import com.coldpixel.sparkle.sprites.Player;
import com.coldpixel.sparkle.sprites.Soldier;

/**
 * 
 * @author Coldpixel
 */
public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        
        switch(cDef){
            case Main.PLAYER_BIT | Main.ENEMYMELEEATTACK_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixA.getUserData()).setAttack(true);
                }
                else {
                    ((Soldier)fixB.getUserData()).setAttack(true);
                }
                break;
            //IceShard Enemy
            case Main.PLAYERATTACK_BIT | Main.ENEMY_BIT:
                    if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    ((Soldier)fixA.getUserData()).decreaseHealth(((IceShard)fixB.getUserData()).getDamage());
                    ((IceShard)fixB.getUserData()).destroy();
                    //Sets filter for soldier to destroyed_bit to avoid collisions
                    if(((Soldier)fixA.getUserData()).getHealth() <= 0){
                        ((Soldier)fixA.getUserData()).death();
                    }
                }
                else {
                    ((Soldier)fixB.getUserData()).decreaseHealth(((IceShard)fixA.getUserData()).getDamage());
                    ((IceShard)fixA.getUserData()).destroy();
                    if(((Soldier)fixB.getUserData()).getHealth() <= 0){
                        ((Soldier)fixB.getUserData()).death();
                    }
                }
                break;
            //IceShard Objects
            case Main.PLAYERATTACK_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.OBJECT_BIT){
                    ((IceShard)fixB.getUserData()).destroy();
                } else{
                    ((IceShard)fixA .getUserData()).destroy();
                }
                break;
            //IceShard Bonfire
            case Main.PLAYERATTACK_BIT | Main.BONFIRE_BIT:
                if(fixA.getFilterData().categoryBits == Main.BONFIRE_BIT){
                    ((IceShard)fixB.getUserData()).destroy();
                } else{
                    ((IceShard)fixA .getUserData()).destroy();
                }
                break;
            //Player Crystal
            case Main.PLAYER_BIT | Main.CRYSTAL_BIT:
                if(fixA.getFilterData().categoryBits == Main.PLAYER_BIT){
                    ((Player)fixA.getUserData()).setIsHealing(true);
                } else{
                    ((Player)fixB.getUserData()).setIsHealing(true);
                }
                break;
            case Main.ENEMY_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixA.getUserData()).setAvoidObject(state);
                } else{
                /*    float width = ((Rectangle)fixA.getUserData()).width;
                    float height = ((Rectangle)fixA.getUserData()).height;
                    float x = ((Rectangle)fixA.getUserData()).x;
                    float y = ((Rectangle)fixA.getUserData()).y;
                    System.out.println(((Soldier)fixB.getUserData()).getY()-((Soldier)fixB.getUserData()).getHeight()/2+" "+(y+height)/Main.PPM);
                   // if(((Soldier)fixB.getUserData()).b2Body.getPosition().x > (x + width / 2) / Main.PPM){
                   //     
                  //  }
                   */Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixB.getUserData()).setAvoidObject(state);
                }
                break;
            case Main.ENEMY_BIT | Main.BONFIRE_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixA.getUserData()).setAvoidObject(state);
                } else{
                    Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixB.getUserData()).setAvoidObject(state);
                }
                break;
            case Main.ENEMY_BIT | Main.CRYSTAL_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixA.getUserData()).setAvoidObject(state);
                } else{
                    Soldier.State state = Soldier.State.UP;
                    ((Soldier)fixB.getUserData()).setAvoidObject(state);
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        
        switch(cDef){
            case Main.PLAYER_BIT | Main.ENEMYMELEEATTACK_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixA.getUserData()).setAttack(false);
                }
                else if(fixB.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixB.getUserData()).setAttack(false);
                }
                break;
            //Player Crystal
            case Main.PLAYER_BIT | Main.CRYSTAL_BIT:
                if(fixA.getFilterData().categoryBits == Main.PLAYER_BIT){
                    ((Player)fixA.getUserData()).setIsHealing(false);
                } else{
                    ((Player)fixB.getUserData()).setIsHealing(false);
                }
            break;
            case Main.ENEMY_BIT | Main.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    ((Soldier)fixA.getUserData()).setAvoidObject(false);
                } else{
                    ((Soldier)fixB.getUserData()).setAvoidObject(false);
                }
                break;
            case Main.ENEMY_BIT | Main.BONFIRE_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    ((Soldier)fixA.getUserData()).setAvoidObject(false);
                } else{
                    ((Soldier)fixB.getUserData()).setAvoidObject(false);
                }
                break;
            case Main.ENEMY_BIT | Main.CRYSTAL_BIT:
                if(fixA.getFilterData().categoryBits == Main.ENEMY_BIT){
                    ((Soldier)fixA.getUserData()).setAvoidObject(false);
                } else{
                    ((Soldier)fixB.getUserData()).setAvoidObject(false);
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
    

}
