package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.GRASS;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by xudong on 7/25/13.
 */
public class Hit extends  Movable {

  private int frame = 0;
  private int time = -3;

  private Battle battle;

  public Hit(Battle battle, Point point) {
    this.battle = battle;
    this.position = new Point(point.x - 16, point.y - 16);
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
        .drawBitmap(tankWarImage.hit[frame], null, getRect(), paint);
  }

  @Override
  public void move() {
    if (time % 4 == 1) {
      frame++;
    }
    if (frame > 2) {
      battle.hits.remove(this);
    }

    time ++;
  }
}
