package com.coldpixel.sparkle;

/**
 *
 * @author Coldpixel
 */
public class Constants {

//==============================================================================
//Desktoplauncher
//==============================================================================  
    public static int WINDOW_WIDTH = 1280;
    public static int WINDOW_HEIGTH = 720;
    public static final boolean fullscreen = false;
    public static final boolean borderlessWindow = false;
    public static final String GAMENAME = "Coldpixel - Sparkle";
    public static final boolean RESZIABLE = false;
    public static final String FAVICON = "Graphics/Icon/Icon.png";
    public static final boolean MAX_FPS = false;

//==============================================================================
//Getter
//==============================================================================
    public static int getWINDOW_WIDTH() {
        return WINDOW_WIDTH;
    }

    public static int getWINDOW_HEIGTH() {
        return WINDOW_HEIGTH;
    }

//==============================================================================
//Setter
//==============================================================================
    public static void setWINDOW_WIDTH(int WINDOW_WIDTH) {
        Constants.WINDOW_WIDTH = WINDOW_WIDTH;
    }

    public static void setWINDOW_HEIGTH(int WINDOW_HEIGTH) {
        Constants.WINDOW_HEIGTH = WINDOW_HEIGTH;
    }
}
