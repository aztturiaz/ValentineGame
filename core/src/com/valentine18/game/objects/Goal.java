package com.valentine18.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.valentine18.game.core.assets.Assets;
import com.valentine18.game.core.Constants;

/**
 * Created by Aztturiaz on 10/02/2018.
 */

public class Goal extends AbstractGameObject
{
    private TextureRegion goalRegion;

    public Goal()
    {
        init();
    }

    private void init()
    {
        dimension.set(96.0f, 40.0f);
        goalRegion = Assets.instance.levelDecorations.goal;
        // Set bounding box for collision detection
        bounds.set(0, 0, 96, Constants.CELL_SIZE * 40);
        origin.set(dimension.x / 2.0f, 0.0f);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;
        reg = goalRegion;
        batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                false, false);
    }
}
