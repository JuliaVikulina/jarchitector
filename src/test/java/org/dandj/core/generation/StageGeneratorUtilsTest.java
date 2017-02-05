package org.dandj.core.generation;

import org.dandj.model.Cell;
import org.dandj.model.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.dandj.core.generation.StageGenerator.getAdjacentAvailableCells;
import static org.dandj.core.generation.StageGenerator.getUpDownLeftRightCells;

public class StageGeneratorUtilsTest {
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
        cells[1][0] = new Cell().x(0).y(0);
        List<Cell> tiles = getAdjacentAvailableCells(0, 0, cells);
        assert tiles.size() == 1;
        assert tiles.get(0).x() == 1;
        assert tiles.get(0).y() == 0;
    }

}
