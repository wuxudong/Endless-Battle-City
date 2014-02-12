package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.wupipi.tankwar.TankWarImage;

import static com.wupipi.tankwar.ImageType.GRID;


/**
 * Created by xudong on 7/25/13.
 */
public class Grid extends AbstractEntity implements Obstacle {
    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
  public void draw(Canvas canvas, Paint paint, Scene scene) {
    canvas
        .drawBitmap(TankWarImage.images[GRID.ordinal()], null, getRect(), paint);
  }
}
