package org.dandj.core.generation;

import org.dandj.utils.AsciiPrinter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.dandj.api.API.*;

public class StageGenerator {
    public static Stage createStage(Stage.Builder sb, Random r) {
        int roomSizeX = 3; //todo make input parameter
        int roomSizeY = 3; //todo make input parameter
        int roomTries = 5; //todo make input parameter

        int xrange = sb.getWidth();
        int yrange = sb.getHeight();

        // simple two dimensional array to represent the layout
        int[][] stageGrid = new int[xrange][];
        for (int i = 0; i < xrange; i++) {
            stageGrid[i] = new int[yrange];
        }

        createRoom:
        for (int i = 0; i < roomTries; i++) {
            int roomX = r.nextInt(xrange - roomSizeX);
            int roomY = r.nextInt(yrange - roomSizeY);

            // check that new room does not overlap with existing ones
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    if (stageGrid[roomX + x][roomY + y] != 0) {
                        continue createRoom;
                    }
                }
            }
            Region.Builder regionBuilder = Region.newBuilder();
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    stageGrid[roomX + x][roomY + y] = 1;
                    regionBuilder.addCells(
                            Cell.newBuilder().setX(roomX + x).setY(roomY + y).build());
                }
            }
            sb.addRegions(regionBuilder.build());
        }

        if (!sb.getRegionsList().isEmpty()) {
            // carve corridors to regions:
            Set<Region> notConnected = new HashSet<>(sb.getRegionsList());
            Set<Region> connected = new HashSet<>();

            notConnected.remove(sb.getRegions(0));
            connected.add(sb.getRegions(0));
            while (!notConnected.isEmpty()) {
                // we carve a maze starting from any connected region
                // if a carved maze connects an yet unconnected region,
                // add it and mentioned region to connected and remove from notConnected
            }
        }
        System.out.println(AsciiPrinter.printStage(sb));
        return sb.build();
    }

    private static Region carveMaze() {
        return Region.newBuilder().build();
    }
}
