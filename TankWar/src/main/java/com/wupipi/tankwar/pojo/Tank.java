package com.wupipi.tankwar.pojo;

import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 7/25/13.
 */
public abstract class Tank extends Movable {

  private int speed = 4;

  private int health;

  private Battle battle;

  private int fireCoolDown = 300;

  private long lastFire = 0;

  private int defaultBulletSpeed = 6;

  private boolean move = false;

  private boolean fire = false;

  /**
   * Current direction the tank is headed.
   */
  protected Direction direction = Direction.NORTH;
  private Direction nextDirection = Direction.NORTH;

  public Tank(Battle battle, Point position) {
    this.battle = battle;
    this.position = position;
  }

  @Override
  public int getWidth() {
    return 32;
  }

  @Override
  public int getHeight() {
    return 32;
  }


  public void head(Direction direction) {
    nextDirection = direction;
  }

  @Override
  public void move() {
    boolean isCurrentDirectionHorizontal =
        direction == Direction.WEST || direction == Direction.EAST;
    boolean isNextDirectionHorizontal =
        nextDirection == Direction.WEST || nextDirection == Direction.EAST;

    if (isCurrentDirectionHorizontal ^ isNextDirectionHorizontal) {
      if (isNextDirectionHorizontal) {
        position.y = ((position.y + 8) / 16) * 16;
      } else {
        position.x = ((position.x + 8) / 16) * 16;
      }
    }

    direction = nextDirection;

    if (move) {
      Log.d("Tank_War", position.toString());
      switch (direction) {
        case NORTH: {
          int leftUpX = position.x;
          int leftUpY = Math.max(position.y - speed, 0);

          Point nextReachablePosition =
              reachablePosition(
                  new Rect(leftUpX, leftUpY, leftUpX + getWidth(), position.y),
                  direction);
          position = nextReachablePosition;
          break;
        }
        case SOUTH: {
          int leftUpX = position.x;
          int leftUpY = position.y + getHeight();


          Point nextReachablePosition =
              reachablePosition(
                  new Rect(leftUpX, leftUpY, leftUpX + getWidth(), Math.min(leftUpY + speed,
                      Const.TILE_COUNT * Const.OFFSET_PER_TILE)),
                  direction);
          position = new Point(nextReachablePosition.x, nextReachablePosition.y - getHeight());
          break;
        }
        case WEST: {
          int leftUpX = Math.max(position.x - speed, 0);
          int leftUpY = position.y;

          Point nextReachablePosition =
              reachablePosition(
                  new Rect(leftUpX, leftUpY, position.x, position.y + getHeight()),
                  direction);
          position = nextReachablePosition;
          break;
        }
        default: {
          int leftUpX = position.x + getWidth();
          int leftUpY = position.y;

          Point nextReachablePosition =
              reachablePosition(
                  new Rect(leftUpX, leftUpY, Math.min(leftUpX + speed,
                      Const.TILE_COUNT
                          * Const.OFFSET_PER_TILE), position.y + getHeight()),
                  direction);
          position = new Point(nextReachablePosition.x - getWidth(), nextReachablePosition.y);
          break;
        }

      }
    }

    Log.d("TANK_WAR", direction.toString() + " " + position.toString());


    fire();
  }

  public void fire(boolean b) {
    this.fire = b;
  }

  private void fire() {
    if (!fire)
      return;

    long now = System.currentTimeMillis();

    int x = position.x;
    int y = position.y;
    if (now - lastFire > fireCoolDown) {
      switch (this.direction)
      {
        case NORTH:
          x += 13;
          y += 3;
          break;

        case SOUTH:
          x += 13;
          y += (32 - 6);
          break;

        case EAST:
          y += 13;
          x += (32 - 6);
          break;

        case WEST:
          y += 13;
          x += 3;
          break;

      }



      Bullet bullet = new Bullet(battle, defaultBulletSpeed, new Point(x, y), direction, this);
      battle.bullets.add(bullet);
      // Log.d("Tank_War", "fire to " + direction);
      lastFire = now;
    }
  }

  public void setMove(boolean move) {
    this.move = move;
  }

  private Point reachablePosition(Rect rect, Direction direction) {

    List<Tile> crossedTiles = Tile.getCrossedTiles(rect);

    int reachableNorthY = rect.top;
    int reachableSouthY = rect.bottom;
    int reachableWestX = rect.left;
    int reachableEastX = rect.right;


    Log.d("TANK_WAR", rect.toString());
    for (Tile tile : crossedTiles) {

      for (Obstacle object : battle.
          getObstacles(tile)) {
        if (object == null || object instanceof Ice || object instanceof Grass) {
          // do nothing
        } else {
          if (Rect.intersects(rect, object.rect)) {
            reachableNorthY = Math.max(reachableNorthY, object.rect.bottom);
            reachableSouthY = Math.min(reachableSouthY, object.rect.top);
            reachableWestX = Math.max(reachableWestX, object.rect.right);
            reachableEastX = Math.min(reachableEastX, object.rect.left);
          }
        }
      }



      for (Movable object : battle.
          getMovable(tile)) {
        if (object != this
            || object instanceof Tank) {
          if (Rect.intersects(rect, object.getRect())) {
            reachableNorthY = Math.max(reachableNorthY, object.getRect().bottom);
            reachableSouthY = Math.min(reachableSouthY, object.getRect().top);
            reachableWestX = Math.max(reachableWestX, object.getRect().right);
            reachableEastX = Math.min(reachableEastX, object.getRect().left);
          }
        }
      }

    }

    switch (direction) {
      case NORTH:
        return new Point(rect.left, reachableNorthY);
      case SOUTH:
        return new Point(rect.left, reachableSouthY);
      case WEST:
        return new Point(reachableWestX, rect.top);
      default:
        return new Point(reachableEastX, rect.top);
    }
  }

  public boolean inTile(int x, int y) {
    if (getRect().intersect(
        new Rect(y * Const.OFFSET_PER_TILE, x * Const.OFFSET_PER_TILE,
            (y + 1) * Const.OFFSET_PER_TILE, (x + 1) * Const.OFFSET_PER_TILE))) {
      return true;
    }
    else {
      return false;
    }
  }

  abstract Ally getAlly();
}
