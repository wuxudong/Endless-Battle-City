package com.wupipi.tankwar.pojo;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import com.wupipi.tankwar.Const;

/**
 * Created by xudong on 7/30/13.
 */
public class Tile {
  public int row;
  public int col;

  public Tile(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public Rect getRect() {
    return new Rect(col * Const.OFFSET_PER_TILE, row * Const.OFFSET_PER_TILE, (col + 1)
        * Const.OFFSET_PER_TILE, (row + 1) * Const.OFFSET_PER_TILE);

  }

  public static List<Tile> getCrossedTiles(Rect rect) {
    int minRow = rect.top / Const.OFFSET_PER_TILE;

    int maxRow =
        rect.bottom % Const.OFFSET_PER_TILE == 0
            ? (rect.bottom / Const.OFFSET_PER_TILE - 1)
            : (rect.bottom / Const.OFFSET_PER_TILE);

    int minCol = rect.left
        / Const.OFFSET_PER_TILE;

    int maxCol =
        rect.right % Const.OFFSET_PER_TILE == 0 ? (rect.right
            / Const.OFFSET_PER_TILE - 1) : (rect.right / Const.OFFSET_PER_TILE);

    List<Tile> result = new ArrayList<Tile>();
    for (int i = minRow; i <= maxRow; i++) {
      for (int j = minCol; j <= maxCol; j++) {
        result.add(new Tile(i, j));
      }
    }

    return result;


  }


}
