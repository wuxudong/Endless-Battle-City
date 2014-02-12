package com.wupipi.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class TankWar extends Activity {

  private TankWarView mTankWarView;

  private View controlContainer;

  private View fireContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

    setContentView(R.layout.activity_main);
    final TankWarView tankWarView = mTankWarView = (TankWarView) findViewById(R.id.map);
    findViewById(R.id.arrowContainer).setVisibility(View.VISIBLE);

    controlContainer = findViewById(R.id.controlContainer);


    controlContainer.setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN: {

            // Normalize x,y between 0 and 1
            float x = event.getX() / v.getWidth();
            float y = event.getY() / v.getHeight();

            // Direction will be [0,1,2,3] depending on quadrant
            int direction = 0;
            direction = (x > y) ? 1 : 0;
            direction |= (x > 1 - y) ? 2 : 0;

            // Direction is same as the quadrant which was clicked
            tankWarView.thread.scene.actPlayerMove(direction);

            return true;
          }
          case MotionEvent.ACTION_MOVE: {

            // Normalize x,y between 0 and 1
            float x = event.getX() / v.getWidth();
            float y = event.getY() / v.getHeight();

            // Direction will be [0,1,2,3] depending on quadrant
            int direction = 0;
            direction = (x > y) ? 1 : 0;
            direction |= (x > 1 - y) ? 2 : 0;

            // Direction is same as the quadrant which was clicked
            tankWarView.thread.scene.actPlayerMove(direction);

            return true;
          }

          case MotionEvent.ACTION_UP: {
            tankWarView.thread.scene.actPlayerMove(Const.MOVE_NONE);

            return true;

          }
        }
        return false;
      }


    });

    fireContainer = findViewById(R.id.fireContainer);
    fireContainer.setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN: {

            tankWarView.thread.scene.actPlayerFire(true);

            return true;
          }

          case MotionEvent.ACTION_UP: {
            tankWarView.thread.scene.actPlayerFire(false);
            return true;
          }
        }
        return false;
      }


    });
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
