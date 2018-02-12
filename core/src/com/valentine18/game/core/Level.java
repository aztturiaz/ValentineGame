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

    /*
    // objects
    public Array<Rock> rocks;
    public Array<GoldenCoin> goldcoins;
    public Array<Feather> feathers;
    // decoration
    public Clouds clouds;
    public Mountain mountains;
    public WaterOverlay waterOverlay;
    */

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

        /*
        // Objects
        rocks = new Array<Rock>();
        goldcoins = new Array<GoldenCoin>();
        feathers = new Array<Feather>();

        // golden coin
        else if (BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel))
        {
            obj = new GoldenCoin();
            offsetHeight = -1.5f;
            obj.position.set(pixelX * (CELL_SIZE * 2), baseHeight * obj.dimension.y + offsetHeight);
            goldcoins.add((GoldenCoin)obj);
        }
        // unknown object/pixel color
        else {
            int r = 0xff & (currentPixel >>> 24); //red color channel
            int g = 0xff & (currentPixel >>> 16); //green color channel
            int b = 0xff & (currentPixel >>> 8); //blue color channel
            int a = 0xff & currentPixel; //alpha channel
            Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
        }
        lastPixel = currentPixel;
    }*/

        /*
        // decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountain(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);
        */

        Gdx.app.debug(TAG, "level loaded");
    }

    public void render(SpriteBatch batch)
    {
        // Draw Goal
        goal.render(batch);

        // Draw Player Character
        player.render(batch);

        /*
        // Draw Mountains
        mountains.render(batch);

        // Draw Rocks
        for (Rock rock : rocks)
            rock.render(batch);

        // Draw Golden Coins
        for (GoldenCoin goldCoin : goldcoins)
            goldCoin.render(batch);

        // Draw Feathers
        for (Feather feather : feathers)
            feather.render(batch);

        // Draw Water Overlay
        waterOverlay.render(batch);

        // Draw Clouds
        clouds.render(batch);

        */
    }
/*
    public void renderGoldenCoins(SpriteBatch batch)
    {
        // Draw Golden Coins
        for (GoldenCoin goldCoin : goldcoins)
            goldCoin.render(batch);
    }
    */

    public void render(SpriteBatch batch, OrthographicCamera camera)
    {
        tiledMap = Assets.instance.getTiledMap();
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1, batch);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
    }

    public void update (float deltaTime)
    {
        /*
        for (Rock rock : rocks)
            rock.update(deltaTime);
        for (GoldenCoin goldCoin : goldcoins)
            goldCoin.update(deltaTime);
        for (Feather feather : feathers)
            feather.update(deltaTime);
        clouds.update(deltaTime);
        */
        player.update(deltaTime);
    }
}
