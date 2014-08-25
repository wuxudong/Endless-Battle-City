package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.TankWarImage;

import static com.wupipi.battlecity.ImageType.GRID;


/**
 * Created by xudong on 7/25/13.
 */
public class Grid extends  Obstacle {
    @Override
    public boolean isBlock() {
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.images[GRID.ordinal()], position.x, position.y, paint);
    }
}
