package com.valentine18.game.core.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Aztturiaz on 07/02/2018.
 */

public final class PlayerAsset
{
    public final TextureAtlas.AtlasRegion body;
    public final TextureAtlas.AtlasRegion jump;
    public final Animation<TextureRegion> playerWalk;

    public PlayerAsset(TextureAtlas atlas)
    {
        jump = atlas.findRegion("Liz_jump");
        body = atlas.findRegion("Liz_walk",2);

        // Animation: Player Walking
        Array<TextureRegion> walkRegions = new Array<TextureRegion>(4);

        for(int i = 0; i < 4; i++)
        {
            TextureRegion reg = i == 3 ?
                    atlas.findRegion("Liz_walk",2) :
                    atlas.findRegion("Liz_walk",i + 1);
            walkRegions.add(reg);

        }
        playerWalk = new Animation(1.0f / 10.0f, walkRegions, Animation.PlayMode.LOOP);

    }
}
