package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.FrameAware;
import com.wupipi.battlecity.TankWarImage;

/**
 * Created by xudong on 7/25/13.
 */
public class Explosion extends AbstractEntity implements FrameAware {

    private int frame = 0;
    private int time = -3;

    private ScoreNumber scoreNumber;

    public Explosion(MyPoint point, ScoreNumber scoreNumber) {
        this.position = new MyPoint(point.x - 16, point.y - 16);
        this.scoreNumber = scoreNumber;
    }

    @Override
    protected int getWidth() {
        return 66;
    }

    @Override
    protected int getHeight() {
        return 66;
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time % 4 == 1) {
            frame++;
        }
        if (frame > 3) {
            scene.delayedDestroyBomb(this);
        }
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.bomb[frame], position.x, position.y, paint);
    }

    public ScoreNumber getScoreNumber() {
        return scoreNumber;
    }
}
