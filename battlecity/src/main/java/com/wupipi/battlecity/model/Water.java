package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.TankWarImage;

import static com.wupipi.battlecity.ImageType.WATER;

/**
 * Created by xudong on 7/25/13.
 */
public class Water extends Obstacle {

    @Override
    boolean isBlock() {
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.images[WATER.ordinal()], position.x, position.y, paint);
    }

}
