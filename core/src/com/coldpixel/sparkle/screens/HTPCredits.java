package com.coldpixel.sparkle.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
public class HTPCredits implements Screen {//HowToPlayCredits Screen

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private BitmapFont bitmapFont = new BitmapFont();
    private TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("Buttons/pack.atlas"));
    private Skin skin = new Skin();
    private Texture howToPlayTexture = new Texture("Graphics/HowToPlay/HowToPlay.png");
    private SpriteBatch batch = new SpriteBatch();
    TextButtonStyle buttonStyleDonate = new TextButton.TextButtonStyle();
    TextButton buttonDonate;

    public HTPCredits(final Game game) {
        this.game = game;
        viewport = new FitViewport(Main.V_WIDTH, Main.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

        Label howToPlayTitle = new Label("HOW TO PLAY", font);
        Label howToPlay = new Label("You deal more Damage depending on your and the enemys current element."
                + "\nYour goal is to kill as many enemys as possible, you will die anyways."
                + "\nIf you are in the center, near the crystal, you'll regain some health over time.", font);
        Label elementsControls = new Label(""
                + "\nWater -> Fire                                                                   WSAD = Movement"
                + "\nFire -> Earth                                                ARROW-KEYS = Shootdirection"
                + "\nEarth -> Air                                                                          ESC = Mainmenu"
                + "\nAir -> Water", font);

        Label creditsTitle = new Label("CREDITS", font);
        Label credits = new Label("We did barely all on our own except the background music and sound."
                + "\nCredits for background music goes to artisticdude from opengameart.org"
                + "\nCredits for fire sound goes to AntumDeluge from opengameart.org"
                + "\nCredits for test-assets and inspiration goes to Kenny from kenney.nl"
                + "\n\nIf you want to contact us: creatiftrue [@] gmail.com", font);
        Label supportUSTitle = new Label("WANT TO SUPPORT US?", font);
        Label supportUS = new Label("This is our first released game. It would help us a lot if you donate something."
                + "\nWe'd appreciate every amount.", font);

        skin.addRegions(buttonAtlas);

        buttonStyleDonate.font = new BitmapFont();
        buttonStyleDonate.up = skin.getDrawable("Blue");
        buttonStyleDonate.down = skin.getDrawable("Grey");
        buttonDonate = new TextButton("Donate", buttonStyleDonate);
        buttonDonate.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeListener.ChangeEvent ce, Actor actor) {
                Gdx.net.openURI("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7RB6QGREK8Z2J");
            }
        });

        Label stars1 = new Label("************************************************************************************", font);
        Label stars2 = new Label("************************************************************************************", font);
        Label stars3 = new Label("************************************************************************************", font);

        Label escape = new Label("Press \"ESC\" or \"C\" to go back", font);

        table.add(howToPlayTitle).expandX().padTop(50);
        table.row();
        table.add(stars1);
        table.row();
        table.add(howToPlay).expandX();
        table.row();
        table.add(elementsControls).padTop(10f).padRight(4);
        table.row();
        table.add(creditsTitle).expandX().padTop(30f);
        table.row();
        table.add(stars2);
        table.row();
        table.add(credits).expandX().padRight(24);
        table.row();
        table.add(supportUSTitle).expandX().padTop(30f);
        table.row();
        table.add(stars3);
        table.row();
        table.add(supportUS).expandX().padLeft(2);
        table.row();
        table.add(buttonDonate).padTop(100f);
        table.row();
        table.row();
        table.add(escape).expandX().padTop(50);

        stage.addActor(table);
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(42 / 255f, 47 / 255f, 48 / 255f, 1);//0-1, Float.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || (Gdx.input.isKeyJustPressed(Input.Keys.C))) {
            game.setScreen(new StartGame((Main) game));
            dispose();
        }
        stage.draw();
        batch.begin();
        batch.draw(howToPlayTexture, 500, 500, 100, 100);
        batch.end();
    }

    @Override
    public void show() {
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
        stage.dispose();
        bitmapFont.dispose();
        buttonAtlas.dispose();
        game.dispose();
        skin.dispose();
    }

}
