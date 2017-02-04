package org.dandj.core.generation;


import org.dandj.model.Cell;
import org.dandj.model.Direction;
import org.dandj.model.Stage;
import org.dandj.utils.SvgPrinter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.Random;

import static org.dandj.core.generation.StageGenerator.*;
import static org.junit.Assert.assertEquals;

public class StageGeneratorTest {
    private final int SEED = 42;
    private Cell cells[][];
    private Stage stage;

    @Before
    public void setUp() {
        int SIZE = 3;
        cells = new Cell[SIZE][];
        cells[0] = new Cell[SIZE];
        cells[1] = new Cell[SIZE];
        cells[2] = new Cell[SIZE];

        stage = new Stage().width(SIZE).height(SIZE);
        StageGenerator.initCells(stage);
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

    @Test
    public void testSingleRoom() {
        int size = 3;
        stage.roomSizeYMin(size).roomSizeXMin(size)
                .roomSizeYMax(size).roomSizeXMax(size);
        assertEquals(1, createStage(stage, new Random(SEED)).regions().size());
    }

    @Test
    public void testTwoRoomsConnectedByMaze() {
        StageGenerator.addRoom(stage, 1, 1, 0, 0);
        StageGenerator.addRoom(stage, 1, 1, 2, 2);
        connectRegionsByTwoSets(stage, new Random(SEED));
        assertEquals(3, stage.regions().size());
        SvgPrinter.printStageAsSvg(stage);
    }

    @Test(timeout = 100)
    public void testTwoAdjacentRooms() {
        StageGenerator.addRoom(stage, 3, 2, 0, 0);
        StageGenerator.addRoom(stage, 3, 1, 0, 2);
        connectRegionsByTwoSets(stage, new Random(SEED));
        assertEquals(2, stage.regions().size());
    }
}