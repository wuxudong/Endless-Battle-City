package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by xudong on 7/25/13.
 */
public class Tank1 extends Tank {

  public Tank1(Battle battle, Point position) {
    super(battle, position);
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {

    canvas
        .drawBitmap(tankWarImage.tank1[direction.ordinal()], null, getRect(),
            paint);

    Log.d("Tank_War", getRect().toString());

    Log.d("Tank_War", "CANVAS " + canvas.getWidth() + " " + canvas.getHeight());
  }

  @Override
  Ally getAlly() {
    return Ally.NPC;
  }
}
