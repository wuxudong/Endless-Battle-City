package com.wupipi.tankwar.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Point;
import android.graphics.Rect;

import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 7/26/13.
 */
public class Battle {
  private Random random = new Random();

  /**
   * Current mode of application: READY to run, RUNNING, or you have already lost. static final
   * ints are used instead of an enum for performance reasons.
   */
  public int mMode = WorkThread.RUNNING;
  public Tank playerTank;

  private List<Tank> playerTanks = new CopyOnWriteArrayList<Tank>();

  private List<Tank> npcTanks = new CopyOnWriteArrayList<Tank>();

  private List<Bullet> playerBullets = new CopyOnWriteArrayList<Bullet>();

  private List<Bullet> npcBullets = new CopyOnWriteArrayList<Bullet>();

  private Food food = null;

  public List<Hit> hits = new CopyOnWriteArrayList<Hit>();

  public List<Bomb> bombs = new CopyOnWriteArrayList<Bomb>();

  public List<TankStart> tankStarts = new CopyOnWriteArrayList<TankStart>();

  private TankAI ai = new TankAI(this);

  public int frame = 0;
  public List<Score> scores = new CopyOnWriteArrayList<Score>();

  public GameMap gameMap;

  private int homeGodTime = 0;
  private int stopNpcTime = 0;


  public Battle() {

    playerTank = new Tank(this, new Point(9 * 16, 24 * 16), Ally.PLAYER, TankType.PLAY1, false);
    playerTanks.add(playerTank);


    Tank tank1 = new Tank(this, new Point(0, 0), Ally.NPC, TankType.NPC1, false);
    npcTanks.add(tank1);

    Tank tank2 = new Tank(this, new Point(182, 0), Ally.NPC, TankType.NPC2, false);
    npcTanks.add(tank2);

    Tank tank3 = new Tank(this, new Point(384, 0), Ally.NPC, TankType.NPC3, false);
    npcTanks.add(tank3);
  }

  public void update() {
    frame++;

    ai.ai();

    for (Tank t : playerTanks) {
      t.move();
    }

    if (stopNpcTime > 0) {
      stopNpcTime--;
    } else {
      for (Tank t : npcTanks) {
        t.move();
      }
    }

    for (Bullet b : playerBullets) {
      b.move();
    }


    for (Bullet b : npcBullets) {
      b.move();
    }

    for (Hit obj : hits) {
      obj.move();
    }

    for (Bomb obj : bombs) {
      obj.move();
    }

    for (Score obj : scores) {
      obj.move();
    }

    for (TankStart obj : tankStarts) {
      obj.move();
    }

    if (food != null) {
      food.move();
    }


    moreTankStart();

    if (homeGodTime > 0) {
      homeGodTime--;
      if (homeGodTime == 0) {
        protectHome(false);
      }
    }

  }

  public void moreFood() {

    int x = random.nextInt((Const.TILE_COUNT - 1) * Const.OFFSET_PER_TILE);
    int y = random.nextInt((Const.TILE_COUNT - 1) * Const.OFFSET_PER_TILE);

    int index = random.nextInt(FoodType.values().length);
    FoodType type = FoodType.values()[index];

    Food food = new Food(this, new Point(x, y), type);
    setFood(food);
  }

  private void moreTankStart() {
    if (frame % 250 == 0 && npcTanks.size() < 10) {
      int where = random.nextInt(3);
      switch (where) {
        case 0: {
          tankStarts.add(new TankStart(this, new Point(192, 0)));
          break;
        }
        case 1: {
          tankStarts.add(new TankStart(this, new Point(0, 0)));
          break;
        }
        case 2: {
          tankStarts.add(new TankStart(this, new Point(384, 0)));
          break;
        }
      }
    }
  }


  public List<Obstacle> getObstacles(Tile tile) {
    List<Obstacle> result = new ArrayList<Obstacle>();

    if (gameMap.get(tile.row, tile.col) != null) {
      result.add(gameMap.get(tile.row, tile.col));
    }

    return result;
  }

  public List<Tank> getTanks(Tile tile, Ally ally) {
    List<Tank> result = new ArrayList<Tank>();

    for (Tank tank : getTanks(ally)) {
      if (Rect.intersects(tile.getRect(), tank.getRect())) {
        result.add(tank);
      }
    }
    return result;
  }

  public List<Tank> getTanks(Ally ally) {
    switch (ally) {
      case PLAYER:
        return playerTanks;
      default:
        return npcTanks;
    }
  }


  public List<Tank> getTanks(Tile tile) {
    List<Tank> result = new ArrayList<Tank>();
    for (Ally ally : Ally.values()) {
      result.addAll(getTanks(tile, ally));
    }
    return result;
  }

  public List<Bullet> getBullets(Tile tile, Ally ally) {
    List<Bullet> result = new ArrayList<Bullet>();
    for (Bullet bullet : getBullets(ally)) {
      if (Rect.intersects(tile.getRect(), bullet.getRect())) {
        result.add(bullet);
      }
    }
    return result;
  }

  public List<Bullet> getBullets(Ally ally) {
    switch (ally) {
      case PLAYER:
        return playerBullets;
      default:
        return npcBullets;
    }
  }

  public Food getFood() {
    return food;
  }

  public void setFood(Food food) {
    this.food = food;
  }

  public void clearNpc() {
    for (Tank t : getTanks(Ally.NPC)) {
      bombs.add(new Bomb(this, t.position, Score.ScoreNumber.NONE));
    }

    getTanks(Ally.NPC).clear();
  }

  public void protectHome(boolean god) {
    if (god) {
      homeGodTime = 500;
    }

    gameMap.protectHome(god);
  }

  public void stopNpc() {
    stopNpcTime = 500;
  }
}
