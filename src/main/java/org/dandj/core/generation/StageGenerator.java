package org.dandj.core.generation;

import org.dandj.utils.AsciiPrinter;

import java.util.Random;

import static org.dandj.api.API.*;

public class StageGenerator {
    public static Stage createStage(Stage.Builder stageBuilder, Random r) {
        int roomSizeX = 3; //todo make random
        int roomSizeY = 3; //todo make random
        int roomTries = 5; //todo make random

        int xrange = stageBuilder.getWidth();
        int yrange = stageBuilder.getHeight();

        int[][] stage = new int[xrange][];
        for (int i = 0; i < xrange; i++) {
            stage[i] = new int[yrange];
        }

        createRoom:
        for (int i = 0; i < roomTries; i++) {
            int roomX = r.nextInt(xrange - roomSizeX);
            int roomY = r.nextInt(yrange - roomSizeY);

            // check that new room does not overlap with existing ones
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    if (stage[roomX + x][roomY + y] != 0) {
                        continue createRoom;
                    }
                }
            }
            Region.Builder regionBuilder = Region.newBuilder();
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    stage[roomX + x][roomY + y] = 1;
                    regionBuilder.addCells(Cell.newBuilder()
                            .setX(roomX + x).setY(roomY + y).build());
                }
            }
            stageBuilder.addRegions(regionBuilder.build());
        }
        System.out.println(AsciiPrinter.printStage(stageBuilder));
        return stageBuilder.build();
    }
}
