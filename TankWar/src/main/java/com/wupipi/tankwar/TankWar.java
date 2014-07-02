package com.wupipi.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class TankWar extends Activity {

  private TankWarView mTankWarView;

  private JoystickView joystickView;

  private View fireContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

    setContentView(R.layout.activity_main);
    final TankWarView tankWarView = mTankWarView = (TankWarView) findViewById(R.id.map);

    joystickView = (JoystickView) findViewById(R.id.joystick);


    joystickView.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
        @Override
        public void onValueChanged(Direction direction) {
            tankWarView.thread.scene.actPlayerMove(direction);
        }
    } );



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
