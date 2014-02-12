package com.wupipi.tankwar;

import android.app.Application;

/**
 * Created by xudong on 1/25/14.
 */
public class TankWarApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    TankWarImage.init(this.getResources());
  }
}
