package com.valentine18.game.core.assets;

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
import com.badlogic.gdx.utils.I18NBundle;
import com.valentine18.game.core.Constants;

/**
 * Created by Aztturiaz on 05/02/2018.
 */

public class Assets implements Disposable, AssetErrorListener
{

    public PlayerAsset player;
    public Player2Asset player2;
    public LevelDecorationAssets levelDecorations;

    public AssetFonts fonts;

    public I18NBundle strings;

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
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }

        TextureAtlas levelDecorationAtlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS_02);

        // enable texture filtering for pixel smoothing
        for(Texture t : atlas.getTextures())
        {
            t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }

        // create game resource objects
        fonts = new AssetFonts();

        player = new PlayerAsset(atlas);
        player2 = new Player2Asset(atlas);

        // Level Decorations
        levelDecorations = new LevelDecorationAssets(levelDecorationAtlas);

        // Get the strings
        strings = assetManager.get(Constants.I18N_BUNDLE, I18NBundle.class);

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

        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS_02, TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        // Load strings resource file
        assetManager.load(Constants.I18N_BUNDLE, I18NBundle.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
    }

    private void initTiledMap()
    {
        TmxMapLoader tmxMapLoader = new TmxMapLoader(new InternalFileHandleResolver());
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.textureMinFilter = Texture.TextureFilter.Nearest;

        assetManager.setLoader(TiledMap.class, tmxMapLoader);
        assetManager.load(Constants.TILE_MAP_LEVEL_01, TiledMap.class, params);
        assetManager.finishLoading();

        assetManager.setLoader(TiledMap.class, tmxMapLoader);
        assetManager.load(Constants.TILE_MAP_LEVEL_02, TiledMap.class, params);
        assetManager.finishLoading();

        assetManager.setLoader(TiledMap.class, tmxMapLoader);
        assetManager.load(Constants.TILE_MAP_LEVEL_03, TiledMap.class, params);
        assetManager.finishLoading();
    }

    public TiledMap getTiledMap(int lvl)
    {
        TiledMap tm;

        switch(lvl)
        {
            case 2:
                tm = assetManager.get(Constants.TILE_MAP_LEVEL_02);
                break;
            case 3:
                tm = assetManager.get(Constants.TILE_MAP_LEVEL_03);
                break;
            default:
                tm = assetManager.get(Constants.TILE_MAP_LEVEL_01);
                break;
        }

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
