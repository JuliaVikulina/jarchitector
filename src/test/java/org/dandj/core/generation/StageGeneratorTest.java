package org.dandj.core.generation;


import org.dandj.core.conversion.SvgPrinter;
import org.dandj.model.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.Random;

import static org.dandj.core.generation.StageGenerator.connectRegionsByTwoSets;
import static org.dandj.core.generation.StageGenerator.createStage;
import static org.junit.Assert.assertEquals;

public class StageGeneratorTest {
    private static final int SIZE = 3;
    private final int SEED = 42;
    @Rule
    public TestName testName = new TestName();
    private Stage stage;

    @Before
    public void setUp() {

        stage = new Stage().width(SIZE).height(SIZE).name(testName.getMethodName());
        StageGenerator.initCells(stage);
    }

    @After
    public void drawStage() {
        SvgPrinter.printStageAsSvg(stage);
    }

    @Test
    public void testSingleRoom() {
        stage.roomSizeYMin(SIZE).roomSizeXMin(SIZE)
                .roomSizeYMax(SIZE).roomSizeXMax(SIZE);
        assertEquals(1, createStage(stage, new Random(SEED)).regions().size());
    }

    @Test
    public void testTwoRoomsConnectedByMaze() {
        StageGenerator.addRoom(stage, 1, 1, 0, 0);
        StageGenerator.addRoom(stage, 1, 1, 2, 2);
        connectRegionsByTwoSets(stage, new Random(SEED));
        assertEquals(3, stage.regions().size());
    }

    @Test
    public void testTwoAdjacentRooms() {
        StageGenerator.addRoom(stage, 3, 2, 0, 0);
        StageGenerator.addRoom(stage, 3, 1, 0, 2);
        connectRegionsByTwoSets(stage, new Random(SEED));
        assertEquals(2, stage.regions().size());
        assertEquals(1, stage.junctions().size());
    }
}