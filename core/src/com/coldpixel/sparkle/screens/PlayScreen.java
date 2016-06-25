package com.coldpixel.sparkle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Coldpixel
 */
public class PlayScreen implements Screen {

    private Main main;
    Texture texture;

    public PlayScreen(Main main) {
        this.main = main;
        texture = new Texture("Graphics/Terrain/Terrain.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(42/255f, 47/255f, 48/255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.batch.begin();
        main.batch.draw(texture, 0, 0);
        main.batch.end();
    }

    @Override
    public void resize(int i, int i1) {
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
