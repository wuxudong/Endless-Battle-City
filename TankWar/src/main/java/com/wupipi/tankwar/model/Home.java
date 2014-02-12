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
public class Home extends AbstractEntity implements Obstacle {

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
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        if (!destroyed) {
            canvas
                    .drawBitmap(TankWarImage.images[HOME.ordinal()], null, getRect(), paint);
        } else {
            canvas
                    .drawBitmap(TankWarImage.images[HOME_DESTROYED.ordinal()], null, getRect(), paint);
        }
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
