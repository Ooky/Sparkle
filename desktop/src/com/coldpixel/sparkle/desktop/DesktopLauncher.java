package com.coldpixel.sparkle.desktop;

import com.coldpixel.sparkle.Constants;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.coldpixel.sparkle.Main;

/**
 *
 * @author Creat-if
 */
public class DesktopLauncher {

    public static void main(String[] arg) {

        LwjglApplicationConfiguration config;
        config = new LwjglApplicationConfiguration();

        if (Constants.fullscreen) {
            Constants.setWINDOW_WIDTH(LwjglApplicationConfiguration.getDesktopDisplayMode().width);
            Constants.setWINDOW_HEIGTH(LwjglApplicationConfiguration.getDesktopDisplayMode().height);
        }

        config.width = Constants.getWINDOW_WIDTH();
        config.height = Constants.getWINDOW_HEIGTH();
        config.title = Constants.GAMENAME;
        config.resizable = Constants.RESZIABLE;
//        config.addIcon(Constants.FAVICON, Files.FileType.Internal);

        if (Constants.borderlessWindow) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
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
