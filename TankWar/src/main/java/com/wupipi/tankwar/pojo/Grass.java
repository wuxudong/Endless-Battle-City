package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.GRASS;
import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.WALL;

/**
 * Created by xudong on 7/25/13.
 */
public class Grass extends Obstacle {

  public Grass() {
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.images[GRASS.ordinal()], null, rect, paint);
  }
}
