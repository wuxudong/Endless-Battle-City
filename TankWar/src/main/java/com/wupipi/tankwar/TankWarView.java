package com.wupipi.tankwar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wupipi.tankwar.pojo.*;

/**
 * Created by xudong on 7/26/13.
 */
public class TankWarView extends SurfaceView  implements SurfaceHolder.Callback{

  public WorkThread thread;

  public TankWarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    getHolder().addCallback(this);
  }

  @Override
  public SurfaceHolder getHolder() {
    return super.getHolder();
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
  {
  }

  public void surfaceCreated(SurfaceHolder holder)
  {
    startGame();
  }

  public void surfaceDestroyed(SurfaceHolder holder)
  {
    stopGame();
  }

  public void startGame()
  {
    if (thread == null)
    {
      thread = new WorkThread(this);

      thread.startThread();
    }
  }

  public void stopGame()
  {
    if (thread != null)
    {
      thread.stopThread();

      // Waiting for the thread to die by calling thread.join,
      // repeatedly if necessary
      boolean retry = true;
      while (retry)
      {
        try
        {
          thread.join();
          retry = false;
        }
        catch (InterruptedException e)
        {
        }
      }
      thread = null;
    }
  }
 }
