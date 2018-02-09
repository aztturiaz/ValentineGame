package com.valentine18.game.core;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Aztturiaz on 07/02/2018.
 */

public class CollisionCell
{
    private final TiledMapTileLayer.Cell cell;
    public final int cellX;
    public final int cellY;
    public CellType type;

    public CollisionCell(TiledMapTileLayer.Cell cell, int cellX, int cellY, CellType type)
    {
        this.cell = cell;
        this.cellX = cellX;
        this.cellY = cellY;
        this.type = type;
    }

    public boolean isEmpty()
    {
        return cell == null;
    }

    public enum CellType
    {
        FLOOR,
        LEFT,
        RIGHT,
        TOP
    }
}
