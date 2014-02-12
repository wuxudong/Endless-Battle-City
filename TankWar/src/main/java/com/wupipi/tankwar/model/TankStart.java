package com.wupipi.tankwar.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class TankStart extends AbstractEntity implements Animation {

    private int frame = 0;
    private int time = 0;
    private int num = 0;

    private Tank tank;

    public TankStart(Tank tank) {
        this.tank = tank;
        this.position = tank.position;
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        canvas
                .drawBitmap(TankWarImage.tankStart[frame], null, getRect(), paint);
    }

    public Tank getTank() {
        return tank;
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time % 3 == 1) {
            frame++;
        }
        if (frame > 6) {
            num++;
            frame = 0;
        }

        if (num >= 3) {
            scene.destroyTankStart(this);
        }
    }
}
