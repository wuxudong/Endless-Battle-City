package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.WALL;

/**
 * Created by xudong on 7/25/13.
 */
public class WallTopChip extends Obstacle implements Drawable {

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.wallTopChip, null, rect, paint);
  }
}
