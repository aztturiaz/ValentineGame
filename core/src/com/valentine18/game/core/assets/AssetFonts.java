package com.valentine18.game.core.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.valentine18.game.core.Constants;

/**
 * Created by Aztturiaz on 05/02/2018.
 */

public class AssetFonts implements Disposable
{
    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public AssetFonts ()
    {
        // create three fonts using Libgdx's 15px bitmap font
        defaultSmall = new BitmapFont(Gdx.files.internal(Constants.GAME_UI_FONT), true);
        defaultNormal = new BitmapFont(Gdx.files.internal(Constants.GAME_UI_FONT), true);
        defaultBig = new BitmapFont(Gdx.files.internal(Constants.GAME_UI_FONT), true);

        // set font sizes
        defaultSmall.getData().setScale(0.75f);
        defaultNormal.getData().setScale(1.0f);
        defaultBig.getData().setScale(2.0f);

        // enable linear texture filtering for smooth fonts
        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void dispose()
    {
        defaultSmall.dispose();
        defaultNormal.dispose();
        defaultBig.dispose();
    }
}
