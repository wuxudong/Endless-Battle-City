package com.wupipi.tankwar.pojo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by xudong on 7/25/13.
 */
public class Tank2 extends Tank {

  public Tank2(Battle battle, Point position) {
    super(battle, position);
  }

  @Override
  public void draw(Canvas canvas, Paint paint, TankWarImage tankWarImage) {

    canvas
        .drawBitmap(tankWarImage.tank2[direction.ordinal()], position.x, position.y,
            paint);

    Log.d("Tank_War", getRect().toString());

    Log.d("Tank_War", "CANVAS " + canvas.getWidth() + " " + canvas.getHeight());
  }

  @Override
  Ally getAlly() {
    return Ally.NPC;
  }
}
