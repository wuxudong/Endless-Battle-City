package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by xudong on 7/25/13.
 */
public class Bomb extends Movable {

  private int frame = 0;
  private int time = -3;

  private Score.ScoreNumber scoreNumber;

  private Battle battle;

  public Bomb(Battle battle, Point point, Score.ScoreNumber scoreNumber) {
    this.battle = battle;
    this.position = new Point(point.x - 16, point.y - 16);
    this.scoreNumber = scoreNumber;
  }

  @Override
  public int getWidth() {
    return 66;
  }

  @Override
  public int getHeight() {
    return 66;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.bomb[frame], null, getRect(), paint);
  }

  @Override
  public void move() {
    if (time % 4 == 1) {
      frame++;
    }
    if (frame > 3) {
      battle.bombs.remove(this);
      Score s = new Score(battle, new Point(position.x + 33, position.y + 33), scoreNumber);
      battle.scores.add(s);
    }

    time++;
  }
}
