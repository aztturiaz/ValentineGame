package com.valentine18.game.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.valentine18.game.objects.Player;
import com.valentine18.game.objects.Player.JUMP_STATE;
import com.valentine18.game.screens.MenuScreen;

import static com.valentine18.game.core.Constants.CELL_SIZE;

/**
 * Created by Aztturiaz on 07/02/2018.
 */

public class GameController extends InputAdapter
{
    private static final String TAG = GameController.class.getName();

    private Game game;

    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    private Vector3 worldPosition = null;
    private float timeLeftGameOverDelay;

    public CameraHelper cameraHelper;
    public Level level;
    public int lives;
    public int score;
    public float livesVisual;
    public float scoreVisual;

    Player player = null;

    public GameController(Game game)
    {
        this.game = game;
        init();
    }

    private void init()
    {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        //TODO: Create parameter, implement saving and reading
        lives = Constants.LIVES_START;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel ()
    {
        score = 0;
        scoreVisual = score;
        level = new Level();
        player = level.player;
        cameraHelper.setTarget(player);
    }

    public void update(float deltaTime)
    {
        handleDebugInput(deltaTime);
        if (isGameOver())
        {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0)
            {
                backToMenu();
            }
        }
        else
        {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);

        testCollisions();

        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater())
        {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }

        // Lives Visual Effect
        if (livesVisual> lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);

        // Score Visual Effect
        if (scoreVisual< score)
            scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
    }

    private void handleDebugInput(float deltaTime)
    {
        if(Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;

        if(Gdx.input.isKeyPressed(Input.Keys.COMMA))
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if(Gdx.input.isKeyPressed(Input.Keys.PERIOD))
            cameraHelper.addZoom(-camZoomSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.MINUS))
            cameraHelper.addZoom(camZoomSpeed);
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
            cameraHelper.setZoom(1);
    }

    private void handleInputGame (float deltaTime)
    {
        if (cameraHelper.hasTarget(player))
        {
            // Player Movement
            // TODO: Remove Auto-movement
            player.velocity.x = player.terminalVelocity.x;
            /*if (Gdx.input.isKeyPressed(Input.Keys.A))
            {
                player.velocity.x = -player.terminalVelocity.x;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.D))
            {
                player.velocity.x = player.terminalVelocity.x;
            }
            else
            {
                // Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != Application.ApplicationType.Desktop)
                {
                    player.velocity.x = player.terminalVelocity.x;
                }
            }*/

            // Player Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE))
            {
                player.setJumping(true);
            }
            else
            {
                player.setJumping(false);
            }
        }
    }

    private void backToMenu()
    {
        // switch to Menu Screen
        game.setScreen(new MenuScreen(game));
    }

    public boolean isGameOver ()
    {
        return lives < 0;
    }

    public boolean isPlayerInWater ()
    {
        return player.position.y < -5;
    }

    /* // TODO: Implement similar logic for enemies
    private void onCollisionPlayerWithGoldCoin(GoldenCoin goldcoin)
    {
        goldcoin.collected = true;
        score += goldcoin.getScore();
    }*/

    private void testCollisions()
    {
        float xPos, yPos;
        float cellX, cellY;
        int absCellX, absCellY;
        boolean validFloorCollision = false, validTopCollision = false, validSideCollision = false;
        boolean isFloor = false;

        TiledMapTileLayer.Cell tiledMapCell;

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) Assets.instance.getTiledMap().getLayers().get("Tiledmap");

        // LEFT BOTTOM CELL -------------------------
        xPos = player.position.x + player.bounds.x;
        yPos = player.position.y; // Bound Y is always 0 relative to Player's position

        cellX = xPos / CELL_SIZE;
        cellY = yPos / CELL_SIZE;

        absCellX = MathUtils.floor(cellX);
        absCellY = MathUtils.floor(cellY);

        tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);
        isFloor = tiledMapCell !=null ? tiledMapCell.getTile().getProperties().containsKey("floor") : false;

        if(isFloor)
        {

            handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.FLOOR);
            validFloorCollision = true;
        }

        // RIGHT BOTTOM CELL -------------------------
        if(!validFloorCollision)
        {
            xPos = player.position.x + player.bounds.x + player.bounds.width;

            cellX = xPos / CELL_SIZE;

            absCellX = MathUtils.floor(cellX);
            absCellY = MathUtils.floor(cellY);

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);
            isFloor = tiledMapCell !=null ? tiledMapCell.getTile().getProperties().containsKey("floor") : false;

            if(isFloor)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.FLOOR);
            }
        }

        // RIGHT SIDE BOTTOM CELL -------------------------
        xPos = (player.position.x + player.bounds.x + player.bounds.width);
        yPos = player.position.y;

        cellX = xPos / CELL_SIZE;
        cellY = yPos / CELL_SIZE;

        absCellX = MathUtils.floor(cellX);
        absCellY = MathUtils.floor(cellY);

        tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

        if(tiledMapCell != null)
        {
            handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.RIGHT);
            validSideCollision = true;
        }

        // RIGHT SIDE TOP CELL -------------------------
        if(!validSideCollision)
        {
            yPos = player.position.y + player.bounds.height;

            cellY = yPos / CELL_SIZE;

            absCellY = MathUtils.floor(cellY);

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.RIGHT);
            }
        }

        // LEFT SIDE BOTTOM CELL -------------------------
        if(!validSideCollision)
        {
            xPos = (player.position.x + player.bounds.x);
            yPos = player.position.y;

            //Gdx.app.error(TAG, "Player position = (" + (player.position.x + player.bounds.x) + "," + (player.position.y + player.bounds.y) + ")");

            cellX = xPos / CELL_SIZE;
            cellY = yPos / CELL_SIZE;

            absCellX = MathUtils.floor(cellX);
            absCellY = MathUtils.floor(cellY);

            //Gdx.app.debug(TAG, "Bottom Left Cell position = (" + (absCellX) + "," + (absCellY) + ")");

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.LEFT);
                validSideCollision = true;
            }
        }

        if(!validSideCollision)
        {
            // LEFT SIDE TOP CELL -------------------------
            yPos = player.position.y + player.bounds.height;

            cellY = yPos / CELL_SIZE;

            absCellY = MathUtils.floor(cellY);

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.LEFT);
            }
        }

        /* // TODO: Implement similar collision for enemies
        // Test collision: Player <-> Gold Coins
        for (GoldenCoin goldenCoin : level.goldcoins)
        {
            if (goldenCoin.collected) continue;

            Rectangle rectanglePlayer, rectangleCoin;

            rectanglePlayer = new Rectangle(player.position.x + player.bounds.x, player.position.y, player.bounds.width, player.bounds.height);
            rectangleCoin = new Rectangle(goldenCoin.position.x, goldenCoin.position.y, goldenCoin.bounds.width, goldenCoin.bounds.height);

            if (!rectanglePlayer.overlaps(rectangleCoin)) continue;

            onCollisionPlayerWithGoldCoin(goldenCoin);
            break;
        }
        */
    }

    private void handlePlayerCollision(int cellXPos, int cellYPos, CollisionCell.CellType cellType)
    {
        float cellLevelX = cellXPos * CELL_SIZE;
        float cellLevelY = cellYPos * CELL_SIZE;

        Rectangle intersection = new Rectangle();

        boolean collisionDetected =
                Intersector.intersectRectangles(
                        new Rectangle((player.position.x + player.bounds.x), (player.position.y + player.bounds.y), player.bounds.width, player.bounds.height),
                        new Rectangle(cellLevelX, cellLevelY, CELL_SIZE, CELL_SIZE),
                        intersection);

        if(!collisionDetected) return;

        switch(cellType)
        {
            case FLOOR:
                if (intersection.getHeight() > 0)
                {
                    switch (player.jumpState)
                    {
                        case FALLING:
                        case JUMP_FALLING:
                            player.position.y = intersection.getY() + intersection.getHeight();
                            player.jumpState = JUMP_STATE.GROUNDED;
                            break;
                        case JUMP_RISING:
                            player.position.y = intersection.getY() + intersection.getHeight();
                            break;
                    }
                }
                break;
            case TOP:
                break;
            case LEFT:
                if (intersection.getWidth() > 0)
                {
                    player.position.x = (intersection.getX() + intersection.getWidth()) - player.bounds.x;
                }
                break;
            case RIGHT:
                if (intersection.getWidth() > 0)
                {
                    player.position.x = intersection.getX() - player.bounds.x - player.bounds.width;
                }
                break;
        }
    }

    @Override
    public boolean keyUp(int keycode)
    {
        // Reset game world
        if(keycode == Input.Keys.HOME)
        {
            init();
            Gdx.app.debug(TAG, "Game world reset");
        }
        // Toggle camera follow
        else if (keycode == Input.Keys.ENTER)
        {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.player);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // Back to Menu
        else if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)
        {
            backToMenu();
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        worldPosition = cameraHelper.camera.unproject(new Vector3(screenX, screenY, 0));
        return true;
        //return super.touchDown(screenX, screenY, pointer, button);
    }
}
