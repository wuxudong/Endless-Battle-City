package com.wupipi.battlecity;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wupipi.battlecity.model.SceneManager;

/**
 * Created by xudong on 7/26/13.
 */
public class BattleCityView extends SurfaceView implements SurfaceHolder.Callback {

    public final Paint paint = new Paint();

    public WorkThread thread;

    private SceneManager sceneManager;

    public BattleCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

    }

    @Override
    public SurfaceHolder getHolder() {
        return super.getHolder();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void surfaceCreated(SurfaceHolder holder) {
        startGame();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopGame();
    }

    public void startGame() {
        if (thread == null) {
            thread = new WorkThread(sceneManager, this);

            thread.startThread();
        }
    }

    public void stopGame() {
        if (thread != null) {
            thread.stopThread();

            thread = null;
        }
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
