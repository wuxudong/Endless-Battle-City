package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.TankWarImage;

import static com.wupipi.battlecity.ImageType.ICE;

/**
 * Created by xudong on 7/25/13.
 */
public class Ice extends Obstacle {

    @Override
    boolean isBlock() {
        return false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.images[ICE.ordinal()], position.x, position.y, paint);
    }
}
