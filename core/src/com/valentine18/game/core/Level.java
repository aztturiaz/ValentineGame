package com.valentine18.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.valentine18.game.core.assets.Assets;
import com.valentine18.game.objects.Goal;
import com.valentine18.game.objects.Player;

/**
 * Created by Aztturiaz on 08/02/2018.
 */

public class Level
{
    public static final String TAG = Level.class.getName();

    public Player player;

    // objects
    public Goal goal;

    // Tiled Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    public Level()
    {
        init();
    }

    private void init()
    {
        // Player character
        player = new Player();
        player.position.set(0,240);

        // Objects
        goal = new Goal();
        goal.position.set(Constants.CELL_SIZE * 142,Constants.CELL_SIZE * 10);

        Gdx.app.debug(TAG, "level loaded");
    }

    public void render(SpriteBatch batch)
    {
        // Draw Goal
        goal.render(batch);

        // Draw Player Character
        player.render(batch);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {
        tiledMap = Assets.instance.getTiledMap();
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1, batch);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
    }

    public void update (float deltaTime)
    {
        player.update(deltaTime);
    }
}
