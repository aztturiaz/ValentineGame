package com.valentine18.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.valentine18.game.core.Assets;
import com.valentine18.game.screens.MenuScreen;


public class Valentine18 extends Game
{
	@Override
	public void create()
	{
		// Set LibGdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load Assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
