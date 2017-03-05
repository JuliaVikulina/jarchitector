package org.dandj;

import org.dandj.core.conversion.ObjPrinter;
import org.dandj.core.conversion.SvgPrinter;
import org.dandj.core.generation.StageGenerator;
import org.dandj.model.Cell;
import org.dandj.model.Region;
import org.dandj.model.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.dandj.model.CellType.ROOM;
import static org.dandj.model.Fragment.*;


public class SandboxTest {
    public static void main(String[] args) throws IOException {
        Stage stage = new Stage().name("x3e2").height(32).width(32).resolution(16)
                .roomTries(32).roomSizeXMin(4).roomSizeXMax(16).roomSizeZMin(4).roomSizeZMax(16)
                .mergeObjects(true)
                .mazeStraightness(0.7f);
        StageGenerator.createStage(stage, new Random(42));
        ObjPrinter.printAsObj(stage, new File("build/levels"));
        SvgPrinter.printStageAsSvg(stage);
    }

    private static Region createWell() {
        Region room = new Region("room");
        room.cells().add(new Cell(0, 0, room, ROOM, singleton(CORNER_DR_OUTER)));
        room.cells().add(new Cell(1, 0, room, ROOM, asList(WALL_D, CORNER_DL_H, CORNER_DR_H)));
        room.cells().add(new Cell(2, 0, room, ROOM, singleton(CORNER_DL_OUTER)));

        room.cells().add(new Cell(0, 1, room, ROOM, asList(WALL_R, CORNER_DR_V, CORNER_UR_V)));
        room.cells().add(new Cell(1, 1, room, ROOM, asList(WALL_D, WALL_R, WALL_L, WALL_U, CORNER_DL_INNER, CORNER_UR_INNER, CORNER_DR_INNER, CORNER_UL_INNER, FLOOR)));
        room.cells().add(new Cell(2, 1, room, ROOM, asList(WALL_L, CORNER_DL_V, CORNER_UL_V)));

        room.cells().add(new Cell(0, 2, room, ROOM, singleton(CORNER_UR_OUTER)));
        room.cells().add(new Cell(1, 2, room, ROOM, asList(WALL_U, CORNER_UL_H, CORNER_UR_H)));
        room.cells().add(new Cell(2, 2, room, ROOM, singleton(CORNER_UL_OUTER)));
        return room;
    }

}
