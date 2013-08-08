package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.WALL;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/25/13.
 */
public class Wall extends Obstacle implements Drawable {

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.images[WALL.ordinal()], null, rect, paint);
  }
}
