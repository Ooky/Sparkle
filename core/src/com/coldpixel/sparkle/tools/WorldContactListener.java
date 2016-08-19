package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.coldpixel.sparkle.Main;
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
                if(fixA.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT)
                    ((Soldier)fixA.getUserData()).setAttack(true);
                else if(fixB.getFilterData().categoryBits == Main.ENEMYMELEEATTACK_BIT){
                    ((Soldier)fixB.getUserData()).setAttack(true);}
                break;
        }
    }

    @Override
    public void endContact(Contact cntct) {
        Gdx.app.log("End Contact","");
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
    

}
