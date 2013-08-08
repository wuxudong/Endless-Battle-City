package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/25/13.
 */
public class WallRightChip extends Obstacle implements Drawable {

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.wallRightChip, null, rect, paint);
  }
}
