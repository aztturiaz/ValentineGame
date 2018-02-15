package com.valentine18.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.valentine18.game.core.assets.Assets;

/**
 * Created by Aztturiaz on 15/02/2018.
 */

public class Player2 extends AbstractGameObject
{
    private TextureRegion player2region;

    public Player2()
    {
        init();
    }

    private void init()
    {
        dimension.set(30.0f, 48.0f);
        player2region = Assets.instance.player2.body;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2.0f, 0.0f);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg;
        reg = player2region;
        batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }
}
