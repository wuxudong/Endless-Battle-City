package com.wupipi.tankwar;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wupipi.tankwar.model.*;

/**
 * Created by xudong on 8/2/13.
 */
public class WorkThread extends Thread {



    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    private final SceneManager scenceManager;
    private final TankWarView view;

    private long mMoveDelay = 20;
    private int mMode;

    public WorkThread(SceneManager sceneManager, TankWarView view) {
        this.scenceManager = sceneManager;
        this.view = view;
    }

    public void startThread() {
        mMode = RUNNING;
        super.start();
    }

    public void stopThread() {
        mMode = PAUSE;
    }

    @Override
    public void run() {
        while (mMode == RUNNING) {
            long start = System.currentTimeMillis();

            scenceManager.nextFrame();

            Canvas canvas = view.getHolder().lockCanvas();

            if (canvas == null) {
                continue;
            }

            Paint paint = view.paint;
            scenceManager.render(canvas, paint);

            view.getHolder().unlockCanvasAndPost(canvas);
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
