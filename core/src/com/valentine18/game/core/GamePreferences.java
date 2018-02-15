package com.valentine18.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Aztturiaz on 05/02/2018.
 */

public class GamePreferences
{
    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public boolean showFpsCounter;
    public int currentLevel;
    public int playerLives;
    private Preferences prefs;

    // singleton: prevent instantiation from other classes
    private GamePreferences ()
    {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load ()
    {
        currentLevel = prefs.getInteger("currentLevel", 1);
        playerLives = prefs.getInteger("playerLives", 3);
    }

    public void save ()
    {
        prefs.putInteger("currentLevel", currentLevel);
        prefs.putInteger("playerLives", playerLives);
        prefs.flush();
    }
}
