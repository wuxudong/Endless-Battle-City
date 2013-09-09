package com.wupipi.tankwar.pojo;

import java.util.List;

import android.graphics.*;
import android.util.Log;

import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 7/25/13.
 */
public class Tank extends Movable {

  private int speed = 4;

  private int health;

  private Battle battle;

  private int fireCoolDown = 20;

  private int power = 1;

  private long coolingFrame = 0;

  private int bulletSpeed = 6;

  private boolean move = false;

  private boolean fire = false;

  private boolean god = false;

  protected int godTime = 0;

  private Ally ally;
  private TankType tankType;

  private boolean carryFood = false;

  private Player player;

  /**
   * Current direction the tank is headed.
   */
  protected Direction direction = Direction.NORTH;
  private Direction nextDirection = Direction.NORTH;

  public Tank(Battle battle, Point position, Ally ally, TankType tankType, boolean carryFood, Player player) {
    this.battle = battle;
    this.position = position;
    this.ally = ally;
    this.tankType = tankType;
    this.carryFood = carryFood;
    this.player = player;
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

    if (godTime > 0) {
      godTime--;
      if (godTime == 0) {
        god = false;
      }
    }

    if (coolingFrame > 0) {
      coolingFrame--;
    }
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
    if (coolingFrame == 0) {
      coolingFrame = fireCoolDown;
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



      Bullet bullet = new Bullet(battle, power, bulletSpeed, new Point(x, y), direction, this);
      battle.getBullets(getAlly()).add(bullet);
      // Log.d("Tank_War", "fire to " + direction);
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
          reachableNorthY = Math.max(reachableNorthY, tile.getRect().bottom);
          reachableSouthY = Math.min(reachableSouthY, tile.getRect().top);
          reachableWestX = Math.max(reachableWestX, tile.getRect().right);
          reachableEastX = Math.min(reachableEastX, tile.getRect().left);
        }
      }

      for (Tank object : battle.
          getTanks(tile)) {
        if (object != this) {
          reachableNorthY = Math.max(reachableNorthY, tile.getRect().bottom);
          reachableSouthY = Math.min(reachableSouthY, tile.getRect().top);
          reachableWestX = Math.max(reachableWestX, tile.getRect().right);
          reachableEastX = Math.min(reachableEastX, tile.getRect().left);
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

  public void beGod(int time) {
    god = true;
    godTime = time;
  }

  public boolean isGod() {
    return god;
  }

  public void star() {
    fireCoolDown /= 2;
    power++;

    bulletSpeed = Math.min(2 * bulletSpeed, 7);
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {

    if (carryFood) {
      if ((battle.frame / 5) % 2 == 0)
        paint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x00FF0000));
    }

    switch (tankType) {
      case PLAY1: {
        canvas
            .drawBitmap(tankWarImage.play1[direction.ordinal()], position.x, position.y,
                paint);

        break;
      }
      case PLAY2: {
        // TODO
        break;
      }

      case NPC1: {
        canvas
            .drawBitmap(tankWarImage.tank1[direction.ordinal()], null, getRect(),
                paint);

        break;
      }

      case NPC2: {
        canvas
            .drawBitmap(tankWarImage.tank2[direction.ordinal()], null, getRect(),
                paint);

        break;
      }

      case NPC3: {
        canvas
            .drawBitmap(tankWarImage.tank3[direction.ordinal()], null, getRect(),
                paint);

        break;
      }

    }

    if (isGod()) {
      int frame = (godTime / 6) % 2;

      canvas
          .drawBitmap(tankWarImage.shield[frame], position.x, position.y,
              paint);
    }

    paint.setColorFilter(null);

    Log.d("Tank_War", getRect().toString());

    Log.d("Tank_War", "CANVAS " + canvas.getWidth() + " " + canvas.getHeight());
  }

  public Ally getAlly() {
    return ally;
  }

  public Score.ScoreNumber scoreNumber() {
    switch (tankType) {
      case NPC1:
        return Score.ScoreNumber._100;
      case NPC2:
        return Score.ScoreNumber._200;
      case NPC3:
        return Score.ScoreNumber._400;
      default:
        return Score.ScoreNumber.NONE;
    }
  }

  public void fastCool() {
    coolingFrame = Math.min(coolingFrame, 4);
  }

  public boolean isCarryFood() {
    return carryFood;
  }

  public Player getPlayer() {
    return player;
  }
}
