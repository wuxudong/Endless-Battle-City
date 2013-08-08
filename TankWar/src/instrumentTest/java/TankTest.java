import android.graphics.Point;
import android.test.AndroidTestCase;

import com.wupipi.tankwar.Const;
import com.wupipi.tankwar.pojo.*;
import junit.framework.Assert;

/**
 * Created by xudong on 8/2/13.
 */
public class TankTest extends AndroidTestCase {


  public void testMove() throws Exception {

    Battle battle = new Battle();


    Wall wall = new Wall(WallMaterial.BRICK);
    wall.position = new Point(4 * Const.OFFSET_PER_TILE, 4 * Const.OFFSET_PER_TILE);

    battle.tileGrid[4][4] = wall;

    Tank tank = new Tank(battle, new Point(4 * Const.OFFSET_PER_TILE, 2 * Const.OFFSET_PER_TILE));
    battle.playerTank = tank;

    tank.head(Direction.SOUTH);
    tank.setMove(true);

    tank.move();

    Assert.assertEquals(new Point(4 * Const.OFFSET_PER_TILE, 2 * Const.OFFSET_PER_TILE), tank.getPosition());

  }
}
