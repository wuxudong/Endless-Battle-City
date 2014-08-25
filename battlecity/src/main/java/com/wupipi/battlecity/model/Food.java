package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.battlecity.FoodType;
import com.wupipi.battlecity.FrameAware;
import com.wupipi.battlecity.TankWarImage;

/**
 * Created by xudong on 8/8/13.
 */
public class Food extends AbstractEntity implements FrameAware {
    private FoodType foodType;

    private int time;

    public Food(MyPoint position, FoodType foodType) {
        this.position = position;
        this.foodType = foodType;
    }

    @Override
    protected int getWidth() {
        return 30;
    }

    @Override
    protected int getHeight() {
        return 28;
    }

    @Override
    public void nextFrame(Scene scene) {
        time++;
        if (time > 300) {
            scene.delayDestroyFood(this);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if ((time / 5) % 2 == 0) {
            canvas.drawBitmap(TankWarImage.food[foodType.ordinal()], position.x, position.y, paint);
        }
    }

    public FoodType getFoodType() {
        return foodType;
    }
}
