package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by xudong on 7/29/13.
 */
public interface Drawable {
  void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage);
}
