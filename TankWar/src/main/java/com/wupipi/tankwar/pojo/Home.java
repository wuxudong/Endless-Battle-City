package com.wupipi.tankwar.pojo;

import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.HOME;
import static com.wupipi.tankwar.pojo.TankWarImage.ImageType.HOME_DESTROYED;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/25/13.
 */
public class Home extends Obstacle {

  public boolean destroyed = false;

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {
    if (!destroyed) {
      canvas
          .drawBitmap(tankWarImage.images[HOME.ordinal()], null, rect, paint);
    } else {
      canvas
          .drawBitmap(tankWarImage.images[HOME_DESTROYED.ordinal()], null, rect, paint);
    }
  }
}
