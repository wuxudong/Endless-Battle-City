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

  public List<Tank> tanks = new CopyOnWriteArrayList<Tank>();

  public List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();

  public List<Hit> hits = new CopyOnWriteArrayList<Hit>();

  public List<Bomb> bombs = new CopyOnWriteArrayList<Bomb>();

  public List<TankStart> tankStarts = new CopyOnWriteArrayList<TankStart>();

  public Home home = null;
  private TankAI ai = new TankAI(this);

  private int frame = 0;
  public List<Score> scores = new CopyOnWriteArrayList<Score>();


  public Battle() {
    playerTank = new Play1Tank(this, new Point(9 * 16, 24 * 16));
    tanks.add(playerTank);


    Tank tank1 = new Tank1(this, new Point(0, 0));
    tanks.add(tank1);

    Tank tank2 = new Tank2(this, new Point(182, 0));
    tanks.add(tank2);

    Tank tank3 = new Tank3(this, new Point(384, 0));
    tanks.add(tank3);
  }

  public void update() {
    frame ++;

    ai.ai();
    for (Tank obj : tanks) {
      obj.move();
    }

    for (Bullet obj : bullets) {
      obj.move();
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


    if (frame %250 == 0 && tanks.size() < 10) {
      int where = random.nextInt(3);
      switch (where) {
        case 0: {
          tankStarts.add(new TankStart(this,new Point(192, 0)));
          break;
        }
        case 1: {
          tankStarts.add(new TankStart(this,new Point(0, 0)));
          break;
        }
        case 2: {
          tankStarts.add(new TankStart(this,new Point(384, 0)));
          break;
        }
      }
    }

  }

  public GameMap gameMap;

  public List<Obstacle> getObstacles(Tile tile) {
    List<Obstacle> result = new ArrayList<Obstacle>();

    if (gameMap.obstacles[tile.row][tile.col] != null) {
      result.add(gameMap.obstacles[tile.row][tile.col]);
    }

    return result;
  }

  public List<Movable> getMovable(Tile tile) {
    List<Movable> result = new ArrayList<Movable>();
    for (Tank tank : tanks) {
      if (Rect.intersects(tile.getRect(), tank.getRect())) {
        result.add(tank);
      }
    }

    for (Bullet bullet : bullets) {
      if (Rect.intersects(tile.getRect(), bullet.getRect())) {
        result.add(bullet);
      }
    }
    return result;
  }

}
