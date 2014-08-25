package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.TankWarImage;

import static com.wupipi.battlecity.ImageType.GRASS;

/**
 * Created by xudong on 7/25/13.
 */
public class Grass extends Obstacle {

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.images[GRASS.ordinal()], position.x, position.y, paint);
    }

    @Override
    public boolean isBlock() {
        return false;
    }
}
