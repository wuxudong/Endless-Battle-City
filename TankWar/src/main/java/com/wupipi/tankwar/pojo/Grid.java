package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.GRID;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/25/13.
 */
public class Grid extends Obstacle {

  public Grid() {}

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.images[GRID.ordinal()], null, rect, paint);
  }
}
