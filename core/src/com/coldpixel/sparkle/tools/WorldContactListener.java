package com.coldpixel.sparkle.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * 
 * @author Coldpixel
 */
public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact cntct) {
        Gdx.app.log("Begin Contact","");
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
