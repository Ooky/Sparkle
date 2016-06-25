package com.coldpixel.sparkle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Constants;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public class PlayScreen implements Screen {
    
//==============================================================================
//Initialization
//==============================================================================   
    private Main main;
    Texture texture;
    private OrthographicCamera cam;
    private Viewport gamePort;
    
//==============================================================================
//Methods
//==============================================================================
    public PlayScreen(Main main) {
        this.main = main;
        texture = new Texture("Graphics/Character/Character.png");
        cam = new OrthographicCamera();
        gamePort = new StretchViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGTH,cam);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
        cam.update();
        Gdx.gl.glClearColor(42/255f, 47/255f, 48/255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.setProjectionMatrix(cam.combined);
        main.batch.begin();
        main.batch.draw(texture, 0, 0);
        main.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); //Adjust the Viewport
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
    }

}
