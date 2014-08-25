package com.wupipi.battlecity.model;

import com.wupipi.battlecity.Const;

import java.io.Serializable;

/**
 * Created by xudong on 8/7/13.
 */
public class BattleField implements Serializable {
  private Obstacle[][] tiles = new Obstacle[Const.TILE_COUNT][Const.TILE_COUNT];

  public void setWall(int i, int j) {
    Wall wall = new Wall();
    wall.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = wall;
  }

  public void setWallTopChip(int i, int j) {
    WallTopChip topChip = new WallTopChip();
    topChip.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = topChip;
  }

  public void setWallLeftChip(int i, int j) {
    WallLeftChip leftChip = new WallLeftChip();
    leftChip.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = leftChip;
  }

  public void setWallRightChip(int i, int j) {
    WallRightChip rightChip = new WallRightChip();
    rightChip.position = new MyPoint(j * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE / 2, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = rightChip;
  }

  public void setWallBottomChip(int i, int j) {
    WallBottomChip bottomChip = new WallBottomChip();
    bottomChip.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE + Const.OFFSET_PER_TILE / 2);
    tiles[i][j] = bottomChip;
  }

  public void setWater(int i, int j) {
    Water water = new Water();
    water.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = water;
  }

  public void setGrid(int i, int j) {
    Grid grid = new Grid();
    grid.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = grid;
  }

  public void setGrass(int i, int j) {
    Grass grass = new Grass();
    grass.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = grass;
  }

  public void setIce(int i, int j) {
    Ice ice = new Ice();
    ice.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = ice;
  }

  public void setHome(int i, int j) {
    Home home = new Home();
    home.position = new MyPoint(j * Const.OFFSET_PER_TILE, i * Const.OFFSET_PER_TILE);
    tiles[i][j] = home;
    tiles[i][j + 1] = home;
    tiles[i + 1][j] = home;
    tiles[i + 1][j + 1] = home;
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
    tiles[i][j] = null;
  }

  public Obstacle get(int i, int j) {
    return tiles[i][j];
  }
}
