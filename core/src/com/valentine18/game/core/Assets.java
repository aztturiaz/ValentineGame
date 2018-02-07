package com.valentine18.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Aztturiaz on 05/02/2018.
 */

public class Assets implements Disposable, AssetErrorListener
{
    /*
    public PlayerAsset player;
    public RockAsset rock;
    public GoldenCoinAsset goldenCoin;
    public FeatherAsset feather;
    public LevelDecorationAsset levelDecoration;
    */
    public AssetFonts fonts;


    public static final String TAG = Assets.class.getName();

    /* This makes the class as singleton and holds the actual instance of the class
        which is Read-Only */
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    //singleton: prevent instantiation from other classes
    private Assets (){}

    public void init (AssetManager assetManager)
    {
        this.assetManager = assetManager;
        loadAssets();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

        // enable texture filtering for pixel smoothing
        for(Texture t : atlas.getTextures())
        {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // create game resource objects
        fonts = new AssetFonts();
        /*
        player = new PlayerAsset(atlas);
        rock = new RockAsset(atlas);
        levelDecoration = new LevelDecorationAsset(atlas);
        feather = new FeatherAsset(atlas);
        goldenCoin = new GoldenCoinAsset(atlas);
        */

        initTiledMap();
    }

    private void loadAssets()
    {
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
    }

    private void initTiledMap()
    {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load(Constants.TILE_MAP_LEVEL_01, TiledMap.class);
        assetManager.finishLoading();
    }

    public TiledMap getTiledMap()
    {
        TiledMap tm = assetManager.get(Constants.TILE_MAP_LEVEL_01);
        return tm;
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }

    @Override
    public void dispose()
    {
        fonts.dispose();
        assetManager.dispose();
    }
}
