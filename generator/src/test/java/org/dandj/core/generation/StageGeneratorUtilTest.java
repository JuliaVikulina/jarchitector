package org.dandj.core.generation;

import com.jme3.math.Vector2f;
import org.dandj.core.conversion.SvgPrinter;
import org.dandj.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.dandj.core.generation.StageGenerator.*;
import static org.dandj.model.Fragment.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StageGeneratorUtilTest {
    private static final int SIZE = 3;
    private Cell cells[][];

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        cells = new Cell[SIZE][];
        cells[0] = new Cell[SIZE];
        cells[1] = new Cell[SIZE];
        cells[2] = new Cell[SIZE];

    }

    @After
    public void drawResult() throws Exception{
        Stage s = new Stage().name(testName.getMethodName())
                .cells(cells).resolution(16).width(SIZE).height(SIZE);
        SvgPrinter.printCellsAsSvg(s);
    }
    @Test
    public void testGetUpDownLeftRightTiles() {
        List<Cell> tiles = getUpDownLeftRightCells(0, 0);
        assert tiles.get(0).getX() == 0;
        assert tiles.get(1).getX() == 0;
        assert tiles.get(2).getX() == -1;
        assert tiles.get(3).getX() == 1;

        assert tiles.get(0).getZ() == -1;
        assert tiles.get(1).getZ() == 1;
        assert tiles.get(2).getZ() == 0;
        assert tiles.get(3).getZ() == 0;

        assert tiles.get(0).getDirection() == Direction.UP;
        assert tiles.get(1).getDirection() == Direction.DOWN;
        assert tiles.get(2).getDirection() == Direction.LEFT;
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

    /**
     * Test walls for single cell
     */
    @Test
    public void testCreateWallsSingleCell() {
        Region well = new Region("room");
        Cell cell = new Cell(1, 1);
        cell.setRegion(well);
        well.cells().add(cell);
        cells[1][1] = cell;
        formRegionFragments(well, cells, new HashMap<>());
        Set<Fragment> expected = new HashSet<>(asList(WALL_D, WALL_R, WALL_U, WALL_L,
                CORNER_UL_INNER, CORNER_DL_INNER, CORNER_UR_INNER, CORNER_DR_INNER, CEILING, FLOOR));
        assertEquals(expected, cell.getFragments());
    }

    /**
     * Test walls for 3 by 3 room
     */
    @Test
    public void testCreateWallsRegion3by3HoleInside() {
        Region well3x3 = new Region("room");
        for (int z = 0; z < cells.length; z++) {
            Cell[] row = cells[z];
            for (int x = 0; x < row.length; x++) {
                if (z != 1 || x != 1) {
                    Cell cell = new Cell(x, z);
                    well3x3.cells().add(cell);
                    cell.setRegion(well3x3);
                    cells[z][x] = cell;
                }
            }
        }
        formRegionFragments(well3x3, cells, new HashMap<>());

        assertEquals(cells[0][0].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_U, WALL_L, CORNER_UL_INNER, CORNER_UR_H, CORNER_DL_V, CORNER_DR_OUTER)));
        assertEquals(cells[0][1].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_D, WALL_U, CORNER_UR_H, CORNER_UL_H, CORNER_DR_H, CORNER_DL_H)));
        assertEquals(cells[0][2].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_R, WALL_U, CORNER_UL_H, CORNER_UR_INNER, CORNER_DR_V, CORNER_DL_OUTER)));
        assertEquals(cells[1][0].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_R, WALL_L, CORNER_UL_V, CORNER_UR_V, CORNER_DL_V, CORNER_DR_V)));
        assertNull(cells[1][1]);
        assertEquals(cells[1][2].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_R, WALL_L, CORNER_UL_V, CORNER_UR_V, CORNER_DL_V, CORNER_DR_V)));
        assertEquals(cells[2][0].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_L, WALL_D, CORNER_UL_V, CORNER_UR_OUTER, CORNER_DR_H, CORNER_DL_INNER)));
        assertEquals(cells[2][1].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_D, WALL_U, CORNER_UR_H, CORNER_UL_H, CORNER_DR_H, CORNER_DL_H)));
        assertEquals(cells[2][2].getFragments(), new HashSet<>(asList(CEILING, FLOOR, WALL_R, WALL_D, CORNER_UL_OUTER, CORNER_UR_V, CORNER_DR_INNER, CORNER_DL_H)));
    }

    @Test
    public void testSetStartPosition(){
        Stage stage = new Stage();
        Region well = new Region("room");
        stage.regions().add(well);
        Cell cell = new Cell(0, 0);
        cell.setRegion(well);
        well.cells().add(cell);
        setStartPosition(stage);
        assertEquals(new Vector2f(0.5f, 0.5f), stage.startPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStartPositionNoRoom(){
        Stage stage = new Stage();

        setStartPosition(stage);
        assertEquals(new Vector2f(0.5f, 0.5f), stage.startPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetStartPositionNoCell(){
        Stage stage = new Stage();
        Region well = new Region("room");
        stage.regions().add(well);
        setStartPosition(stage);
        assertEquals(new Vector2f(0.5f, 0.5f), stage.startPosition());
    }
}
