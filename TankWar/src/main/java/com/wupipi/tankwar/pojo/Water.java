package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.WATER;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/25/13.
 */
public class Water extends Obstacle {

  public Water() {}

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.images[WATER.ordinal()], null, rect, paint);
  }
}
