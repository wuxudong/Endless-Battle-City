package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by xudong on 7/25/13.
 */
public class Score extends Movable {
  enum ScoreNumber {
    NONE, _100, _200, _400, _500
  }

  private int time = 0;

  private Battle battle;

  private ScoreNumber scoreNumber;

  public Score(Battle battle, Point point, ScoreNumber scoreNumber) {
    this.battle = battle;
    this.position = new Point(point.x - 14, point.y - 7);
    this.scoreNumber = scoreNumber;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    if (scoreNumber != ScoreNumber.NONE) {
      canvas
          .drawBitmap(tankWarImage.score[scoreNumber.ordinal()], null, getRect(), paint);
    }
  }

  @Override
  public void move() {
    time++;
    if (time > 30) {
      battle.scores.remove(this);
    }
  }
}
