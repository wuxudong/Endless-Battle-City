package com.wupipi.battlecity.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wupipi.battlecity.BattleCityActivity;
import com.wupipi.battlecity.Const;
import com.wupipi.battlecity.TankWarImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xudong on 8/22/14.
 */
public class SceneManager {
    private Scene scene;
    private BattleCityActivity.ScoreListener scoreListener;
    private final int WIDTH = 512;
    private int HEIGHT = 448;

    public void nextFrame() {

        if (scene != null) {

            scene.nextFrame();
        }

    }

    public void render(Canvas canvas, Paint paint) {
        if (scene == null) {
            return;
        }

        canvas.drawColor(Color.GRAY);


        float sx = (float) canvas.getWidth() / (float) WIDTH;
        float sy = (float) canvas.getHeight() / (float) HEIGHT;

        canvas.scale(sx, sy);

        drawScoreBoard(canvas, paint);

        canvas.translate(32, 16);

        canvas.clipRect(0, 0, Const.OFFSET_PER_TILE * Const.TILE_COUNT, Const.OFFSET_PER_TILE
                * Const.TILE_COUNT);
        canvas.drawColor(Color.BLACK);

        if (scene.getPlayerTank() != null) {
            scene.getPlayerTank().draw(canvas, paint);
        }

        for (Tank tank : scene.getNpcTanks()) {
            tank.draw(canvas, paint);
        }

        List<Grass> grasses = new ArrayList<Grass>();
        // grass should drawScoreBoard after bullet, but bullet is after ice and water
        for (int i = 0; i < Const.TILE_COUNT; i++) {
            for (int j = 0; j < Const.TILE_COUNT; j++) {
                Obstacle obstacle = scene.getBattleField().get(i, j);
                if (obstacle != null) {
                    if (obstacle instanceof Grass) {
                        grasses.add((Grass) obstacle);
                    } else {
                        obstacle.draw(canvas, paint);
                    }
                }
            }
        }

        for (Bullet bullet : scene.getBullets()) {
            bullet.draw(canvas, paint);
        }

        for (Grass grass : grasses) {
            grass.draw(canvas, paint);
        }

        for (AbstractEntity animation : scene.getAnimations()) {
            animation.draw(canvas, paint);
        }

        if (scene.getFood() != null) {
            scene.getFood().draw(canvas, paint);
        }

        if (scene.isLose()) {
            int color = paint.getColor();
            float textSize = paint.getTextSize();
            paint.setARGB(255, 255, 0, 0);
            paint.setTextSize(100);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Lose!!", WIDTH / 2, HEIGHT / 2, paint);
            paint.setColor(color);
            paint.setTextSize(textSize);

        }
    }


    public void drawScoreBoard(Canvas canvas, Paint paint) {
        drawPlayerScore(canvas, paint);
        drawLevel(canvas, paint);
        drawNpcScore(canvas, paint);
    }

    private void drawPlayerScore(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.playerLife[0], 464, 52 * 0 + 256, paint);
        canvas.drawText(String.valueOf(scene.getPlayer().life), 482, 48 * 0 + 272 + 10, paint);
    }

    private void drawNpcScore(Canvas canvas, Paint paint) {

        int npcLife = scene.getNpcPlayer().life;
        npcLife = Math.min(npcLife, 20);
        for (int i = 0; i < npcLife; i++) {
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

    private void drawLevel(Canvas canvas, Paint paint) {
        canvas.drawBitmap(TankWarImage.level, 464, 352, paint);
        canvas.drawText(String.valueOf(1), 472, 384, paint);
    }


    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScoreListener(BattleCityActivity.ScoreListener scoreListener) {
        this.scoreListener = scoreListener;
    }

    public BattleCityActivity.ScoreListener getScoreListener() {
        return scoreListener;
    }
}
