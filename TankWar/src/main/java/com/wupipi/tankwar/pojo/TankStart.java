package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by xudong on 7/25/13.
 */
public class TankStart extends  Movable {

  private int frame = 0;
  private int time = 0;
  private int num = 0;
  private Battle battle;

  private Random random = new Random();

  public TankStart(Battle battle, Point point) {
    this.battle = battle;
    this.position = new Point(point.x, point.y);
  }

  @Override
  public int getWidth() {
    return 32;
  }

  @Override
  public int getHeight() {
    return 32;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.tankStart[frame], null, getRect(), paint);
  }

  @Override
  public void move() {
    if (time % 3 == 1) {
      frame++;
    }
    if (frame > 6) {
      num ++;
      frame = 0;
    }

    if(num >= 3) {
      battle.tankStarts.remove(this);
      int type = random.nextInt(3);
      Tank tank = null;
      switch (type) {
        case 0: {
          tank = new Tank1(battle, position);
          break;
        }
        case 1: {
          tank = new Tank2(battle, position);
          break;
        }
        case 2: {
          tank = new Tank3(battle, position);
          break;
        }
      }

      battle.tanks.add(tank);
    }

    time ++;

  }

}
