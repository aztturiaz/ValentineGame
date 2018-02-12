package com.valentine18.game.core.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * Created by Aztturiaz on 10/02/2018.
 */

public final class LevelDecorationAssets
{
    public final AtlasRegion goal;

    public LevelDecorationAssets(TextureAtlas atlas)
    {
        goal = atlas.findRegion("store_exitSign");
    }
}
