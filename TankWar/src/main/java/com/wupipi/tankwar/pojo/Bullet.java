package com.wupipi.tankwar.pojo;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 7/27/13.
 */
public class Bullet extends Movable {
  private int speed;

  private Direction direction;

  private Battle battle;

  private int power = 0;

  private Tank owner;

  public Bullet(Battle battle, int power, int speed, Point position, Direction direction, Tank owner) {
    this.battle = battle;
    this.power = power;
    this.speed = speed;
    this.position = position;
    this.direction = direction;
    this.owner = owner;
  }

  @Override
  public int getWidth() {
    return 6;
  }

  @Override
  public int getHeight() {
    return 6;
  }

  @Override
  public void move() {
    Point next = null;

    // Log.d("Tank_War", "bullet direction " + direction);
    switch (direction) {
      case EAST: {
        next = new Point(position.x + speed, position.y);
        break;
      }
      case WEST: {
        next = new Point(position.x - speed, position.y);
        break;
      }
      case NORTH: {
        next = new Point(position.x, position.y - speed);
        break;
      }
      default: {
        next = new Point(position.x, position.y + speed);
        break;
      }
    }


    position = next;

    // check hit
    int x = 0;
    int y = 0;
    switch (direction) {
      case NORTH:
        x = position.x + getWidth() / 2;
        y = position.y;
        break;
      case SOUTH:
        x = position.x + getWidth() / 2;
        y = position.y + getHeight();
        break;
      case WEST:
        x = position.x;
        y = position.y + getHeight() / 2;
        break;
      default:
        x = position.x + getWidth();
        y = position.y + getHeight() / 2;
        break;
    }


    if (x < 0) {
      battle.getBullets(getAlly()).remove(this);
      return;
    }
    else if (x >= Const.TILE_COUNT * Const.OFFSET_PER_TILE) {
      battle.getBullets(getAlly()).remove(this);
      return;
    }
    else if (y < 0) {
      battle.getBullets(getAlly()).remove(this);
      return;
    }
    else if (y >= Const.TILE_COUNT * Const.OFFSET_PER_TILE) {
      battle.getBullets(getAlly()).remove(this);
      return;
    }


    boolean hit = false;

    List<Tile> crossTiles = Tile.getCrossedTiles(getRect());

    for (Tile tile : crossTiles) {
      for (Obstacle object : battle.getObstacles(tile)) {
        if (!Rect.intersects(getRect(), object.rect)) {
          continue;
        }

        if (object instanceof Wall) {
          hit = true;
          switch (direction) {
            case NORTH: {
              battle.gameMap.setWallTopChip(tile.row, tile.col);
              break;
            }
            case SOUTH: {
              battle.gameMap.setWallBottomChip(tile.row, tile.col);
              break;
            }
            case WEST: {
              battle.gameMap.setWallLeftChip(tile.row, tile.col);
              break;
            }
            case EAST: {
              battle.gameMap.setWallRightChip(tile.row, tile.col);
              break;
            }
          }


          battle.hits.add(new Hit(battle, new Point(position.x + 3, position.y + 3)));

          Log.d("Tank_War", "hit " + object);
        } else if (object instanceof Grid) {
          hit = true;
          // TODO: CHECK POWER

          battle.hits.add(new Hit(battle, new Point(position.x + 3, position.y + 3)));
        } else if (object instanceof Home) {
          hit = true;

          battle.hits.add(new Hit(battle, new Point(position.x + 3, position.y + 3)));
          ((Home) object).destroyed = true;
          battle.mMode = WorkThread.LOSE;
        } else if (object instanceof WallBottomChip || object instanceof WallLeftChip
            || object instanceof WallTopChip || object instanceof WallRightChip) {
          hit = true;
          battle.gameMap.clear(tile.row, tile.col);
          battle.hits.add(new Hit(battle, new Point(position.x + 3, position.y + 3)));
        }
      }


      for (Ally ally : Ally.values()) {
        if (ally == getAlly())
          continue;

        for (Tank object : battle.getTanks(tile, ally)) {
          if (!Rect.intersects(getRect(), object.getRect())) {
            continue;
          }

          hit = true;

          if (!object.isGod()) {
            battle.getTanks(ally).remove(object);
            if(object.isCarryFood()) {
              battle.moreFood();
            }

            if(object.getPlayer().getAlly() == Ally.PLAYER) {
              battle.born(object.getPlayer());
            }

            battle.bombs.add(new Bomb(battle, object.position, object.scoreNumber()));
          }
          Log.d("Tank_War", "hit " + object);
        }

        for (Bullet object : battle.getBullets(tile, ally)) {
          if (!Rect.intersects(getRect(), object.getRect())) {
            continue;
          }

          hit = true;
          battle.getBullets(ally).remove(object);
          Log.d("Tank_War", "hit " + object);

        }
      }
    }
    if (hit) {
      battle.getBullets(getAlly()).remove(this);
      owner.fastCool();
    }

  }


  private Ally getAlly() {
    return owner.getAlly();
  }


  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.bullet[direction.ordinal()], position.x, position.y,
            paint);
  }
}
