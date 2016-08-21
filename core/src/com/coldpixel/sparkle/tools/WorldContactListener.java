package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.coldpixel.sparkle.Main;
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
                    //((Soldier)fixA.getUserData()).pushBack((Player)fixB);(wenn die animation beendet ist wird ein pushback durchgeführt)
                   // ((Player)fixB.getUserData()).b2Body.applyLinearImpulse(new Vector2((((Soldier)fixA.getUserData()).getX()<((Player)fixB.getUserData()).getX()?25f:-25f), 0), ((Player)fixB.getUserData()).b2Body.getWorldCenter(), true);
                }
                else if(fixB.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixB.getUserData()).setAttack(true);
                   // ((Player)fixA.getUserData()).b2Body.applyLinearImpulse(new Vector2((((Soldier)fixB.getUserData()).getX()<((Player)fixA.getUserData()).getX()?25f:-25f), 0), ((Player)fixA.getUserData()).b2Body.getWorldCenter(), true);
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
                    ((Player)fixB.getUserData()).decreaseHealth(10);
                }
                else if(fixB.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixB.getUserData()).setAttack(false);
                    ((Player)fixA.getUserData()).decreaseHealth(10);
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
