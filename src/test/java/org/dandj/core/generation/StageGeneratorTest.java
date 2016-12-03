package org.dandj.core.generation;


import org.dandj.model.Cell;
import org.dandj.model.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StageGeneratorTest {
    private static Cell stage[][];

    @Before
    public void setUp(){
        stage = new Cell[3][];
        stage[0] = new Cell[3];
        stage[1] = new Cell[3];
        stage[2] = new Cell[3];
    }

    @Test
    public void testGetUpDownLeftRightTiles() {
        List<Cell> tiles = StageGenerator.getUpDownLeftRightCells(0, 0);
        assert tiles.get(0).x() == -1;
        assert tiles.get(1).x() == 0;
        assert tiles.get(2).x() == 0;
        assert tiles.get(3).x() == 1;

        assert tiles.get(0).y() == 0;
        assert tiles.get(1).y() == -1;
        assert tiles.get(2).y() == 1;
        assert tiles.get(3).y() == 0;

        assert tiles.get(0).direction() == Direction.LEFT;
        assert tiles.get(1).direction() == Direction.UP;
        assert tiles.get(2).direction() == Direction.DOWN;
        assert tiles.get(3).direction() == Direction.RIGHT;
    }

    @Test
    public void testGetAdjacentAvailableTiles() {
        stage[1][0] = new Cell().x(0).y(0);
        List<Cell> tiles = StageGenerator.getAdjacentAvailableCells(0, 0, stage);
        assert tiles.size() == 1;
        assert tiles.get(0).x() == 1;
        assert tiles.get(0).y() == 0;
    }
}