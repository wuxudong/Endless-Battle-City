package com.wupipi.tankwar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.wupipi.tankwar.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xudong on 8/2/13.
 */
public class WorkThread extends Thread {

    private TankWarView tankWarView;

    Scene scene;

    private final ScoreBoard scoreBoard;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    private long mMoveDelay = 20;

    private final Paint mPaint = new Paint();

    public WorkThread(TankWarView view) {
        tankWarView = view;

        scene = Scene.restore();

        if (scene == null) {
            scene = new Scene();
        }
        scene.setGameMap(new MapLevel().getGameMap(3));

        scoreBoard = new ScoreBoard();
    }

    public void startThread() {
        scene.mMode = RUNNING;
        super.start();
    }

    public void stopThread() {
        scene.mMode = PAUSE;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (!isInterrupted()) {

            long start = System.currentTimeMillis();
            try {

                scene.nextFrame();

                canvas = tankWarView.getHolder().lockCanvas();

                canvas.drawColor(Color.GRAY);

                float sx = (float) canvas.getWidth() / (float) 512;

                float sy = (float) canvas.getHeight() / (float) 448;

                canvas.scale(sx, sy);


                scoreBoard.draw(canvas, mPaint, scene);


                canvas.translate(32, 16);


                canvas.clipRect(0, 0, Const.OFFSET_PER_TILE * Const.TILE_COUNT, Const.OFFSET_PER_TILE
                        * Const.TILE_COUNT);
                canvas.drawColor(Color.BLACK);


                for (Tank tank : scene.getPlayerTanks()) {
                    tank.draw(canvas, mPaint, scene);
                }

                for (Tank tank : scene.getNpcTanks()) {
                    tank.draw(canvas, mPaint, scene);
                }


                List<Grass> grasses = new ArrayList<Grass>();
                // TODO: grass should draw after bullet, but bullet is after ice and water
                for (int i = 0; i < Const.TILE_COUNT; i++) {
                    for (int j = 0; j < Const.TILE_COUNT; j++) {
                        Obstacle obstacle = scene.getGameMap().get(i, j);
                        if (obstacle != null) {
                            if (obstacle instanceof Grass) {
                                grasses.add((Grass) obstacle);
                            } else {
                                obstacle.draw(canvas, mPaint, scene);
                            }
                        }
                    }
                }

                for (Bullet bullet : scene.getBullets()) {
                    bullet.draw(canvas, mPaint, scene);
                }

                for (Grass grass : grasses) {
                    grass.draw(canvas, mPaint, scene);
                }



                for (Animation animation : scene.getAnimations()) {
                    animation.draw(canvas, mPaint, scene);
                }


                if (scene.getFood() != null) {
                    scene.getFood().draw(canvas, mPaint, scene);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                if (canvas != null) {
                    tankWarView.getHolder().unlockCanvasAndPost(canvas);
                }
            }


            Log.d("Tank_War", "spend " + (System.currentTimeMillis() - start));
            try {

                long delay = mMoveDelay - (System.currentTimeMillis() - start);
                if (delay > 0) {
                    sleep(mMoveDelay);
                }
            } catch (InterruptedException e) {
            }
        }

    }
}
