package com.valentine18.game.core;

/**
 * Created by Aztturiaz on 05/02/2018.
 */

public final class Constants
{
    // Visible game world is 5 meters wide
    public static final float VIEWPORT_WIDTH = 800.0f;

    // Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = 500.0f;

    // GUI Width
    public static final float VIEWPORT_GUI_WIDTH = 800.0f;

    // GUI Height
    public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "images/valentine18.pack.atlas";

    // Location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS_02 = "levels/tileset_valentine_02.atlas";

    // Game UI Font
    public static final String GAME_UI_FONT = "images/arial-15.fnt";

    // Location of description file for texture atlas
    public static final String TILE_MAP_LEVEL_01 = "levels/level01.tmx";

    public static final String TEXTURE_ATLAS_UI = "images/valentine18-ui.pack.atlas";
    public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

    // Location of description file for skins
    public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
    public static final String SKIN_VALENTINE_UI = "images/valentine18-ui.json";

    // Preferences Filename
    public static final String PREFERENCES = "preferences.pref";

    // Delay after game over
    public static final float TIME_DELAY_GAME_OVER = 3;

    // Amount of lives at game start
    public static final int LIVES_START = 3;

    // Cell Size for each cell in Tiled Map
    public static final float CELL_SIZE = 48.0f;

    // Delay Time after game finished
    public static final float TIME_DELAY_GAME_FINISHED = 6;

}
