package org.dandj.core.generation;


import org.dandj.api.API;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StageGeneratorTest {
    private static API.Cell stage[][];

    @Before
    public void setUp(){
        stage = new API.Cell[3][];
        stage[0] = new API.Cell[3];
        stage[1] = new API.Cell[3];
        stage[2] = new API.Cell[3];
    }

    @Test
    public void testGetUpDownLeftRightTiles() {
        List<StageGenerator.Tile> tiles = StageGenerator.getUpDownLeftRightTiles(0, 0);
        assert tiles.get(0).x == -1;
        assert tiles.get(1).x == 0;
        assert tiles.get(2).x == 0;
        assert tiles.get(3).x == 1;

        assert tiles.get(0).y == 0;
        assert tiles.get(1).y == -1;
        assert tiles.get(2).y == 1;
        assert tiles.get(3).y == 0;

        assert tiles.get(0).direction == StageGenerator.Tile.Direction.LEFT;
        assert tiles.get(1).direction == StageGenerator.Tile.Direction.UP;
        assert tiles.get(2).direction == StageGenerator.Tile.Direction.DOWN;
        assert tiles.get(3).direction == StageGenerator.Tile.Direction.RIGHT;
    }

    @Test
    public void testGetAdjacentAvailableTiles() {
        stage[0][1] = API.Cell.newBuilder().setX(0).setY(0).build();
        List<StageGenerator.Tile> tiles = StageGenerator.getAdjacentAvailableTiles(0, 0, stage);
        assert tiles.size() == 1;
        assert tiles.get(0).x == 1;
        assert tiles.get(0).y == 0;
    }
}