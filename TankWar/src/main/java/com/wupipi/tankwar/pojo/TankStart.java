package com.wupipi.tankwar.pojo;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by xudong on 7/25/13.
 */
public class TankStart extends Movable {

  private final Tank tank;
  private int frame = 0;
  private int time = 0;
  private int num = 0;
  private Battle battle;

  public TankStart(Battle battle, Tank tank) {
    this.battle = battle;
    this.tank = tank;
    this.position = tank.position;
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
      num++;
      frame = 0;
    }

    if (num >= 3) {
      battle.tankStarts.remove(this);
      battle.getTanks(tank.getAlly()).add(tank);
    }

    time++;

  }

}
