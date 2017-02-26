package org.dandj.core.generation;

import org.dandj.model.Cell;
import org.dandj.model.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.dandj.core.generation.StageGenerator.getAdjacentAvailableCells;
import static org.dandj.core.generation.StageGenerator.getUpDownLeftRightCells;

public class StageGeneratorUtilTest {
    private static final int SIZE = 3;
    private Cell cells[][];

    @Before
    public void setUp() {
        cells = new Cell[SIZE][];
        cells[0] = new Cell[SIZE];
        cells[1] = new Cell[SIZE];
        cells[2] = new Cell[SIZE];

    }

    @Test
    public void testGetUpDownLeftRightTiles() {
        List<Cell> tiles = getUpDownLeftRightCells(0, 0);
        assert tiles.get(0).getX() == -1;
        assert tiles.get(1).getX() == 0;
        assert tiles.get(2).getX() == 0;
        assert tiles.get(3).getX() == 1;

        assert tiles.get(0).getZ() == 0;
        assert tiles.get(1).getZ() == -1;
        assert tiles.get(2).getZ() == 1;
        assert tiles.get(3).getZ() == 0;

        assert tiles.get(0).getDirection() == Direction.LEFT;
        assert tiles.get(1).getDirection() == Direction.UP;
        assert tiles.get(2).getDirection() == Direction.DOWN;
        assert tiles.get(3).getDirection() == Direction.RIGHT;
    }

    @Test
    public void testGetAdjacentAvailableTiles() {
        cells[1][0] = new Cell(0, 0, Direction.DOWN);
        List<Cell> tiles = getAdjacentAvailableCells(0, 0, cells);
        assert tiles.size() == 1;
        assert tiles.get(0).getX() == 1;
        assert tiles.get(0).getZ() == 0;
    }

}
