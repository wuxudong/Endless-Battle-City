package com.wupipi.tankwar;

import com.wupipi.tankwar.model.Scene;

import java.util.List;

/**
 * Created by xudong on 1/23/14.
 */
public interface FrameAware {
  void nextFrame(Scene scene);
}
