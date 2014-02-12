package com.wupipi.tankwar.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.TankWarImage;
import com.wupipi.tankwar.model.Drawable;
import com.wupipi.tankwar.model.Scene;

/**
 * Created by xudong on 8/10/13.
 */
public class ScoreBoard implements Drawable {

    private void drawPlayer(Canvas canvas, Paint paint, Scene scene) {
        // TODO: replace text with images
        canvas.drawBitmap(TankWarImage.playerLife[0], 464, 52 * 0 + 256, paint);
        // TODO: remove 10 when use images
        canvas.drawText(String.valueOf(scene.player.life), 482, 48 * 0 + 272 + 10, paint);
    }

    private void drawNpc(Canvas canvas, Paint paint, Scene scene) {
        // TODO: replace text with images


        for (int i = 0; i < scene.npcPlayer.life; i++) {
            int tx = 0;
            if (i % 2 == 0) {
                tx = 0;
            } else {
                tx = 16;
            }

            int ty = (i / 2) * 16;

            canvas.drawBitmap(TankWarImage.npcLife, 466 + tx, 34 + ty, paint);
        }
    }


    private void drawLevel(Canvas canvas, Paint paint, Scene scene) {
        canvas.drawBitmap(TankWarImage.level, 464, 352, paint);
        canvas.drawText(String.valueOf(4), 468, 384, paint);
    }


    @Override
    public void draw(Canvas canvas, Paint paint, Scene scene) {
        drawPlayer(canvas, paint, scene);
        drawLevel(canvas, paint, scene);
        drawNpc(canvas, paint, scene);
    }
}
