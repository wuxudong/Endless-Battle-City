package com.wupipi.tankwar.pojo;

import android.graphics.Rect;

import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 8/7/13.
 */
public class GameMap {
  private Obstacle[][] obstacles = new Obstacle[Const.TILE_COUNT][Const.TILE_COUNT];

  public void setWall(int i, int j) {
    Wall wall = new Wall();
    wall.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 1)
            * Const.OFFSET_PER_TILE, (i + 1) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = wall;
  }

  public void setWallTopChip(int i, int j) {
    WallTopChip topChip = new WallTopChip();
    topChip.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, j
            * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE
            + Const.OFFSET_PER_TILE / 2);
    obstacles[i][j] = topChip;
  }

  public void setWallLeftChip(int i, int j) {
    WallLeftChip leftChip = new WallLeftChip();
    leftChip.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, j
            * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE / 2, i * Const.OFFSET_PER_TILE
            + Const.OFFSET_PER_TILE);
    obstacles[i][j] = leftChip;
  }

  public void setWallRightChip(int i, int j) {
    WallRightChip rightChip = new WallRightChip();
    rightChip.rect =
        new Rect(j * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE / 2, i * Const.OFFSET_PER_TILE,
            j
                * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE
                + Const.OFFSET_PER_TILE);
    obstacles[i][j] = rightChip;
  }

  public void setWallBottomChip(int i, int j) {
    WallBottomChip bottomChip = new WallBottomChip();
    bottomChip.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE / 2,
            j
                * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE
                + Const.OFFSET_PER_TILE);
    obstacles[i][j] = bottomChip;
  }

  public void setWater(int i, int j) {
    Water water = new Water();
    water.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 1)
            * Const.OFFSET_PER_TILE, (i + 1) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = water;
  }

  public void setGrid(int i, int j) {
    Grid grid = new Grid();
    grid.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 1)
            * Const.OFFSET_PER_TILE, (i + 1) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = grid;
  }

  public void setGrass(int i, int j) {
    Grass grass = new Grass();
    grass.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 1)
            * Const.OFFSET_PER_TILE, (i + 1) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = grass;
  }

  public void setIce(int i, int j) {
    Ice ice = new Ice();
    ice.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 1)
            * Const.OFFSET_PER_TILE, (i + 1) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = ice;
  }


  public void setHome(int i, int j) {
    Home home = new Home();
    home.rect =
        new Rect(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE, (j + 2)
            * Const.OFFSET_PER_TILE, (i + 2) * Const.OFFSET_PER_TILE);
    obstacles[i][j] = home;
    obstacles[i][j + 1] = home;
    obstacles[i + 1][j] = home;
    obstacles[i + 1][j + 1] = home;
  }

  public void protectHome(boolean god) {
    if (god) {
      setGrid(23,11);
      setGrid(23,12);
      setGrid(23,13);
      setGrid(23,14);
      setGrid(24,11);
      setGrid(24,14);
      setGrid(25,11);
      setGrid(25,14);
    } else {
      setWall(23,11);
      setWall(23,12);
      setWall(23,13);
      setWall(23,14);
      setWall(24,11);
      setWall(24,14);
      setWall(25,11);
      setWall(25,14);
    }
   }


  public void clear(int i, int j) {
    obstacles[i][j] = null;
  }

  public Obstacle get(int i, int j) {
    return obstacles[i][j];
  }


}
