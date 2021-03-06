package com.coldpixel.sparkle.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Creat-if
 */
public class StartGame implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private TextButton startGameButton;
    private TextButton howToPlayCredits;
    private TextButton quitGame;
    private TextButtonStyle buttonStyleBlue;
    private TextButtonStyle buttonStyleRed;
    private TextButtonStyle buttonStyleGreen;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private ShapeRenderer shaperenderer = new ShapeRenderer();
    private int positionTop = 480;

    private Texture deathYellow;
    private Texture deathRed;
    private Texture backgroundTexture;
    private TextureRegion regionYellow;
    private TextureRegion regionRed;
    private TextureRegion backgroundRegion;
    private SpriteBatch batch = new SpriteBatch();
    private int backgroundX = 132;
    private int backgroundY = 0;
    private int backgroundWidth;
    private int backgroundHeight;

    private enum buttonState {

        BUTTON1, BUTTON2, BUTTON3
    }
    private buttonState currentButtonState = buttonState.BUTTON1;

    public StartGame(final Game game) {
        this.game = game;
        deathYellow = new Texture("Graphics/Enemy/Soldier/soldierAirDeathEarth.png");
        deathRed = new Texture("Graphics/Enemy/Soldier/soldierFireDeathEarth.png");
        backgroundTexture = new Texture("Graphics/TiledSpreetTest.png");
        regionYellow = new TextureRegion(deathYellow, 480, 0, 84, 80);
        regionRed = new TextureRegion(deathRed, 480, 0, 84, 80);
        backgroundWidth += 64;
        backgroundHeight += 64;
        backgroundRegion = new TextureRegion(backgroundTexture, backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/pack.atlas"));
        skin.addRegions(buttonAtlas);
        //Blue
        buttonStyleBlue = new TextButtonStyle();
        buttonStyleBlue.font = font;
        buttonStyleBlue.up = skin.getDrawable("Blue");
        buttonStyleBlue.down = skin.getDrawable("Grey");
        //Red
        buttonStyleRed = new TextButtonStyle();
        buttonStyleRed.font = font;
        buttonStyleRed.up = skin.getDrawable("Red");
        buttonStyleRed.down = skin.getDrawable("Grey");
        //Green
        buttonStyleGreen = new TextButtonStyle();
        buttonStyleGreen.font = font;
        buttonStyleGreen.up = skin.getDrawable("Green");
        buttonStyleGreen.down = skin.getDrawable("Grey");

        startGameButton = new TextButton("Start Game", buttonStyleGreen);
        howToPlayCredits = new TextButton("How to Play\nCredits", buttonStyleBlue);
        quitGame = new TextButton("Quit Game", buttonStyleRed);

        Table table = new Table();
        table.center();
        table.setFillParent(true);//take up entire stage
        table.add(startGameButton);
        table.row();
        table.add(howToPlayCredits).padTop(10);
        table.row();
        table.add(quitGame).padTop(10);
        addListener();

        stage.addActor(table);
        Gdx.gl.glLineWidth(10);

    }

    @Override
    public void render(float f) {
        handleInput();
        Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (int i = 0; i < 1280; i += 64) {
            for (int j = 0; j < 768; j += 64) {
                batch.draw(backgroundRegion, i, j, 64, 64);
            }
        }

        batch.draw(regionYellow, 560, 0, 820, 800);
        batch.draw(regionRed, 710, 0, -800, 800);
        batch.end();
        shaperenderer.setColor(new Color(Color.GRAY));
        shaperenderer.setProjectionMatrix(stage.getCamera().combined);
        shaperenderer.begin(ShapeRenderer.ShapeType.Line);
        shaperenderer.rectLine(640, positionTop - 58, 640, positionTop, 167);
        shaperenderer.end();
        stage.draw();
    }

    private void addListener() {
        //StartGameButton
        startGameButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                game.setScreen(new PlayScreen((Main) game));
                dispose();
            }
        });
        //HowToPlay CREDITS
        howToPlayCredits.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                game.setScreen(new HTPCredits((Main) game));
                dispose();
            }
        });
        //QuitGame
        quitGame.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) || (Gdx.input.isKeyJustPressed(Keys.S))) {
            moveBorderUP();
        }
        if (Gdx.input.isKeyJustPressed(Keys.UP) || (Gdx.input.isKeyJustPressed(Keys.W))) {
            moveBorderDOWN();
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || (Gdx.input.isKeyJustPressed(Keys.E))
                || (Gdx.input.isKeyJustPressed(Keys.SPACE))) {
            switch (currentButtonState) {
                case BUTTON1:
                    game.setScreen(new PlayScreen((Main) game));
                    dispose();
                    break;
                case BUTTON2:
                    game.setScreen(new HTPCredits((Main) game));
                    dispose();
                    break;
                case BUTTON3:
                    Gdx.app.exit();
                    break;

            }
        }
    }

    private void moveBorderUP() {
        switch (this.positionTop) {
            case 480:
                this.positionTop = positionTop - 66;
                currentButtonState = buttonState.BUTTON2;
                break;
            case 414:
                this.positionTop = positionTop - 66;
                currentButtonState = buttonState.BUTTON3;
                break;
            case 348:
                this.positionTop = 480;
                currentButtonState = buttonState.BUTTON1;
                break;
            default:
                this.positionTop = 480;
                currentButtonState = buttonState.BUTTON1;
                break;
        }

    }

    private void moveBorderDOWN() {
        switch (this.positionTop) {
            case 348:
                this.positionTop = positionTop + 66;
                currentButtonState = buttonState.BUTTON2;
                break;
            case 414:
                this.positionTop = positionTop + 66;
                currentButtonState = buttonState.BUTTON1;
                break;
            case 480:
                this.positionTop = 348;
                currentButtonState = buttonState.BUTTON3;
                break;
            default:
                this.positionTop = 348;
                currentButtonState = buttonState.BUTTON3;
                break;
        }

    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void show() {
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
        stage.dispose();
    }

}
