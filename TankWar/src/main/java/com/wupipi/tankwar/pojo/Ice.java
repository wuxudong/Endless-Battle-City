package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.GRID;
import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.ICE;

/**
 * Created by xudong on 7/25/13.
 */
public class Ice extends Obstacle{

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    canvas
        .drawBitmap(tankWarImage.images[ICE.ordinal()], null, rect, paint);
  }
}
