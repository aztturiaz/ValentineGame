package com.valentine18.game.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Aztturiaz on 07/02/2018.
 */

public class GameRenderer implements Disposable
{
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private GameController gameController;

    public GameRenderer(GameController gameController)
    {
        this.gameController = gameController;
        init();
    }

    public void init()
    {
        batch = new SpriteBatch();
        // Main Camera
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        //camera.position.set(0, 0, 0);
        camera.position.set(Constants.VIEWPORT_GUI_WIDTH/2, Constants.VIEWPORT_HEIGHT/2, 0);
        camera.update();
        // GUI Camera
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void render()
    {
        renderWorld(batch);
        renderGui(batch);
    }

    private void renderWorld (SpriteBatch batch) {
        gameController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        gameController.level.player.render(batch);
        batch.end();
        // Render TileMap - Done after batch ends, because TiledMap implements it's own batch begin-end segment
        // Just use the same SpriteBatch
        gameController.level.render(batch, camera);
    }

    public void resize (int width, int height)
    {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    private void renderGuiExtraLive (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;

        for (int i = 0; i < Constants.LIVES_START; i++)
        {
            if (gameController.lives <= i)
            {
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            }
            batch.draw(Assets.instance.player.body, x + i * 32, y,
                    50, 50, 30, 48, 0.5f, -0.5f, 0);
            batch.setColor(1, 1, 1, 1);
        }

        if (gameController.lives >= 0
                && gameController.livesVisual> gameController.lives)
        {
            int i = gameController.lives;
            float alphaColor = Math.max(0, gameController.livesVisual - gameController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + gameController.lives - gameController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(Assets.instance.player.body, x + i * 32, y,
                    50, 50, 30, 48,
                    alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private void renderGui (SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        // draw extra lives icon + text (anchored to top right edge)
        renderGuiExtraLive(batch);
        // draw game over text
        renderGuiGameOverMessage(batch);

        batch.end();
    }

    private void renderGuiGameOverMessage (SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (gameController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            //fontGameOver.drawMultiLine(batch, "GAME OVER", x, y, 0, BitmapFont.HAlignment.CENTER);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }

    @Override
    public void dispose()
    {
        batch.dispose();
    }
}
