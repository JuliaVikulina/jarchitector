package org.dandj.core.generation;

import java.util.Random;

import static org.dandj.api.API.*;

public class StageGenerator {
    public static Stage createStage(Stage input, Random r) {
        Stage.Builder stageBuilder = Stage.newBuilder(input);
        int roomSizeX = 3; //todo make random
        int roomSizeY = 3; //todo make random

        int roomTries = 15; //todo make random

        int xrange = input.getWidth();
        int yrange = input.getHeight();

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
        return stageBuilder.build();
    }
}
