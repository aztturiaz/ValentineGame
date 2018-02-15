package com.valentine18.game.core.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Aztturiaz on 15/02/2018.
 */

public class Player2Asset
{
    public final TextureAtlas.AtlasRegion body;

    public Player2Asset(TextureAtlas atlas)
    {
        body = atlas.findRegion("Adrian");
    }
}