package com.coldpixel.sparkle.desktop;

import com.badlogic.gdx.Files;
import com.coldpixel.sparkle.Constants;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.coldpixel.sparkle.Main;

public class DesktopLauncher {

    public static void main(String[] arg) {

        LwjglApplicationConfiguration config;
        config = new LwjglApplicationConfiguration();

        config.width = Constants.WINDOW_WIDTH;
        config.height = Constants.WINDOW_HEIGTH;
        config.title = Constants.GAMENAME;
        config.resizable = Constants.RESZIABLE;
        config.addIcon(Constants.FAVICON, Files.FileType.Internal);

        if (Constants.borderlessWindow) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        }

        if (Constants.fullscreen) {
            config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
            config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        }

        if (Constants.MAX_FPS) {
            //Shows the "real" fps, 0 disables throttling 
            config.vSyncEnabled = false;
            config.foregroundFPS = 0;
            config.backgroundFPS = 0;
        }

        new LwjglApplication(new Main(), config);
    }
}
