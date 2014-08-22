package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.TankWarImage;

import static com.wupipi.tankwar.ImageType.HOME;
import static com.wupipi.tankwar.ImageType.HOME_DESTROYED;

/**
 * Created by xudong on 7/25/13.
 */
public class Home extends Obstacle {

    private boolean destroyed = false;

    @Override
    protected int getWidth() {
        return Const.OFFSET_PER_TILE * 2;
    }

    @Override
    protected int getHeight() {
        return Const.OFFSET_PER_TILE * 2;
    }

    @Override
    boolean isBlock() {
        return true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (!destroyed) {
            canvas.drawBitmap(TankWarImage.images[HOME.ordinal()], position.x, position.y, paint);
        } else {
            canvas.drawBitmap(TankWarImage.images[HOME_DESTROYED.ordinal()], position.x, position.y, paint);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
