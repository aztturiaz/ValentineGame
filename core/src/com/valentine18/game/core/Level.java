package com.valentine18.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.valentine18.game.core.assets.Assets;
import com.valentine18.game.objects.Goal;
import com.valentine18.game.objects.Player;
import com.valentine18.game.objects.Player2;

/**
 * Created by Aztturiaz on 08/02/2018.
 */

public class Level
{
    public static final String TAG = Level.class.getName();

    public Player player;
    public Player2 player2;

    // objects
    public Goal goal;

    // Tiled Map
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    // Level value
    private int level = 1;

    public Level(int level)
    {
        this.level = level;
        init();
    }

    private void init()
    {
        // Player character
        player = new Player();

        // Player position according to Level
        switch (level)
        {
            case 2:
                player.position.set(0,680);
                break;
            case 3:
                break;
            default:
                player.position.set(0,250);
                break;
        }

        // Objects
        goal = new Goal();
        goal.position.set(Constants.CELL_SIZE * 142, Constants.CELL_SIZE * 10);

        if(level == 3)
        {
            // Player2
            player2 = new Player2();
            player2.position.set(goal.position.x + (Constants.CELL_SIZE * 2), goal.position.y);
        }

        Gdx.app.debug(TAG, "level loaded");
    }

    public void render(SpriteBatch batch)
    {
        // Draw Goal
        goal.render(batch);

        // Draw Player Character
        player.render(batch);

        if(level == 3)
        {
            // Draw Player 2 Character
            player2.render(batch);
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {
        tiledMap = Assets.instance.getTiledMap(level);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1, batch);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
    }

    public void update (float deltaTime)
    {
        player.update(deltaTime);
    }
}
