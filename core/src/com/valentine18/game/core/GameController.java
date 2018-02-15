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
import com.valentine18.game.core.assets.Assets;
import com.valentine18.game.objects.Goal;
import com.valentine18.game.objects.Player;
import com.valentine18.game.objects.Player.JUMP_STATE;
import com.valentine18.game.screens.GameScreen;
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
    public int currentLevel;

    Player player = null;
    Goal goal = null;
    private boolean goalReached = false;

    public GameController(Game game)
    {
        this.game = game;
        init();
    }

    private void init()
    {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        goalReached = false;
        lives = GamePreferences.instance.playerLives;
        currentLevel = GamePreferences.instance.currentLevel;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    private void initLevel ()
    {
        score = 0;
        scoreVisual = score;
        level = new Level(currentLevel);
        player = level.player;
        goal = level.goal;
        cameraHelper.setTarget(player);
    }

    private void restartLevel()
    {
        // Player position according to Level
        switch (currentLevel)
        {
            case 2:
                player.position.set(0,680);
                break;
            case 3:
            default:
                player.position.set(0,250);
                break;
        }
    }

    public void update(float deltaTime)
    {
        handleDebugInput(deltaTime);
        if (isGameOver() || goalReached)
        {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0)
            {
                if(isGameOver() || (goalReached && currentLevel == 3))
                {
                    backToMenu();
                }
                else
                {
                    nextLevel();
                }
                return;
            }
        }
        else
        {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);

        testCollisions();

        cameraHelper.update(deltaTime);

        if (!isGameOver() && isPlayerOffscreen())
        {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                restartLevel();
        }

        // Lives Visual Effect
        if (livesVisual> lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
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
            // Player Movement (Automatically)
            if(goalReached)
            {
                player.velocity.y = 0;
            }
            else
            {
                player.velocity.x = player.terminalVelocity.x;
            }

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
        saveGameState();
        // switch to Menu Screen
        game.setScreen(new MenuScreen(game));
    }

    private void nextLevel()
    {
        saveGameState();
        // switch to Menu Screen
        game.setScreen(new GameScreen(game));
    }

    public boolean isGameOver ()
    {
        return lives < 0;
    }

    public boolean isPlayerOffscreen()
    {
        return player.position.y < -5;
    }

    /* // TODO: Implement similar logic for enemies - ADD ENEMIES
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
        boolean validFloorCollision = false, validSideCollision = false;
        boolean isFloor, isSpike;

        TiledMapTileLayer.Cell tiledMapCell;

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) Assets.instance.getTiledMap(currentLevel).getLayers().get("Tiledmap");

        // LEFT BOTTOM CELL -------------------------
        xPos = player.position.x + player.bounds.x;
        yPos = player.position.y; // Bound Y is always 0 relative to Player's position

        cellX = xPos / CELL_SIZE;
        cellY = yPos / CELL_SIZE;

        absCellX = MathUtils.floor(cellX);
        absCellY = MathUtils.floor(cellY);

        tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);
        isFloor = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("floor") : false;
        isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

        if(isFloor)
        {
            handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.FLOOR, isSpike);
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
            isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

            if(isFloor)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.FLOOR, isSpike);
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

        isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

        if(tiledMapCell != null)
        {
            handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.RIGHT, isSpike);
            validSideCollision = true;
        }

        // RIGHT SIDE TOP CELL -------------------------
        if(!validSideCollision)
        {
            yPos = player.position.y + player.bounds.height;

            cellY = yPos / CELL_SIZE;

            absCellY = MathUtils.floor(cellY);

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

            isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.RIGHT, isSpike);
            }
        }

        // LEFT SIDE BOTTOM CELL -------------------------
        if(!validSideCollision)
        {
            xPos = (player.position.x + player.bounds.x);
            yPos = player.position.y;

            cellX = xPos / CELL_SIZE;
            cellY = yPos / CELL_SIZE;

            absCellX = MathUtils.floor(cellX);
            absCellY = MathUtils.floor(cellY);

            tiledMapCell = tiledMapTileLayer.getCell(absCellX, absCellY);

            isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.LEFT, isSpike);
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

            isSpike = tiledMapCell != null ? tiledMapCell.getTile().getProperties().containsKey("spikes") : false;

            if(tiledMapCell != null)
            {
                handlePlayerCollision(absCellX, absCellY, CollisionCell.CellType.LEFT, isSpike);
            }
        }

        if(!goalReached)
        {
            Rectangle intersection = new Rectangle();

            Intersector.intersectRectangles(
                    new Rectangle(player.position.x + player.bounds.x, player.position.y, player.bounds.width, player.bounds.height),
                    new Rectangle(goal.position.x + goal.bounds.x, goal.position.y + goal.bounds.y, goal.bounds.width, goal.bounds.height),
                    intersection);

            if(intersection.getWidth() > 0)
            {
                onCollisionPlayerWithGoal();
            }
        }

        // TODO: Implement similar collision as GOAL for enemies

    }

    private void onCollisionPlayerWithGoal()
    {
        goalReached = true;
        currentLevel = currentLevel == 3 ? 1 : currentLevel + 1;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
        player.velocity.x = 0;
    }

    private void handlePlayerCollision(int cellXPos, int cellYPos, CollisionCell.CellType cellType, boolean isSpike)
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

        if(isSpike)
        {
            if(isGameOver()) return;
            onSpikesCollision();
            return;
        }

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

    private void onSpikesCollision()
    {
        lives--;
        player.jumpState = JUMP_STATE.GROUNDED;

        if(isGameOver())
        {
            timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
        }
        else
        {
            restartLevel();
        }
    }

    @Override
    public boolean keyUp(int keycode)
    {
        // Reset game world
        if(keycode == Input.Keys.HOME)
        {
            currentLevel = currentLevel == 3 ? 1 : currentLevel + 1;
            saveGameState();
            nextLevel();
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

    public boolean isGoalReached()
    {
        return goalReached;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        worldPosition = cameraHelper.camera.unproject(new Vector3(screenX, screenY, 0));
        return true;
    }

    private void saveGameState()
    {
        GamePreferences.instance.playerLives = lives;
        GamePreferences.instance.currentLevel = currentLevel;
        GamePreferences.instance.save();
    }
}
