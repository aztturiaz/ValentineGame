package com.valentine18.game.core;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Aztturiaz on 07/02/2018.
 */

public final class PlayerAsset
{
    public final TextureAtlas.AtlasRegion body;

    public PlayerAsset(TextureAtlas atlas)
    {
        body = atlas.findRegion("Liz_walk",2);
    }
}
