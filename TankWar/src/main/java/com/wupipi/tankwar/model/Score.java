package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class Score extends AbstractEntity implements FrameAware, Animation{

  private int time = 0;

  private ScoreNumber scoreNumber;

  public Score (Point point, ScoreNumber scoreNumber) {
    this.position = new Point(point.x - 14, point.y - 7);
    this.scoreNumber = scoreNumber;
  }

  @Override
  public int getWidth() {
    return 28;
  }

  @Override
  public int getHeight() {
    return 14;
  }

  @Override
  public void draw(Canvas canvas, Paint paint, Scene scene) {
    if (scoreNumber != ScoreNumber.NONE) {
      canvas
          .drawBitmap(TankWarImage.score[scoreNumber.ordinal()], null, getRect(), paint);
    }
  }

  @Override
  public void nextFrame(Scene scene) {
    time++;
    if (time > 30) {
        scene.delayedDestroyScore(this);
    }
  }
}
