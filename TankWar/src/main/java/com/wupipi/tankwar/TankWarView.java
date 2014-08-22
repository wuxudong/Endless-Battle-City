package com.wupipi.tankwar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wupipi.tankwar.model.AbstractEntity;
import com.wupipi.tankwar.model.Bullet;
import com.wupipi.tankwar.model.Grass;
import com.wupipi.tankwar.model.Obstacle;
import com.wupipi.tankwar.model.Scene;
import com.wupipi.tankwar.model.SceneManager;
import com.wupipi.tankwar.model.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xudong on 7/26/13.
 */
public class TankWarView extends SurfaceView implements SurfaceHolder.Callback {

    public final Paint paint = new Paint();

    public WorkThread thread;

    private SceneManager sceneManager;

    public TankWarView(Context context, AttributeSet attrs) {
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
