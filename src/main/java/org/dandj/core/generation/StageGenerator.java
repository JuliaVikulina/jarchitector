package org.dandj.core.generation;

import org.dandj.utils.AsciiPrinter;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static org.dandj.api.API.*;
import static org.dandj.core.generation.StageGenerator.Tile.*;
import static org.dandj.core.generation.StageGenerator.Tile.Direction.*;

public class StageGenerator {
    public static Stage createStage(Stage.Builder sb, Random r) {
        int roomSizeX = 3; //todo make input parameter
        int roomSizeY = 3; //todo make input parameter
        int roomTries = 5; //todo make input parameter

        int xrange = sb.getWidth();
        int yrange = sb.getHeight();

        // simple two dimensional array to represent the layout
        Cell[][] stageGrid = new Cell[xrange][];
        for (int i = 0; i < xrange; i++) {
            stageGrid[i] = new Cell[yrange];
        }

        createRoom:
        for (int i = 0; i < roomTries; i++) {
            int roomX = r.nextInt(xrange - roomSizeX);
            int roomY = r.nextInt(yrange - roomSizeY);

            // check that new room does not overlap with existing ones
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    if (stageGrid[roomX + x][roomY + y] != null) {
                        continue createRoom;
                    }
                }
            }
            Region.Builder regionBuilder = Region.newBuilder();
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    Cell cell = Cell.newBuilder().setX(roomX + x).setY(roomY + y).build();
                    regionBuilder.addCells(cell);
                    stageGrid[roomX + x][roomY + y] = cell;
                }
            }
            final Region region = regionBuilder.build();
            region.getCellsList().forEach(cell -> cell.parent = region);
            sb.addRegions(region);
        }

        if (!sb.getRegionsList().isEmpty()) {
            // carve corridors to regions:
            Set<Region> notConnected = new HashSet<>(sb.getRegionsList());
            List<Region> connected = new LinkedList<>();

            notConnected.remove(sb.getRegions(0));
            connected.add(sb.getRegions(0));
            while (!notConnected.isEmpty()) {
                // we carve a maze starting from any connected region
                Region startingRegion = connected.get(r.nextInt(connected.size()));

                Region.Builder maze = Region.newBuilder();
                Region destination = null;
                // todo: do the job
                Tile start = getNearPoint(startingRegion, stageGrid, r);
                if (start == null) // todo make protection from infinite loop
                    continue;
                List<Tile.Direction> directions = getAvailableDirections(start.x, start.y, stageGrid);
                if (directions.isEmpty())
                    continue;
                Direction direction = directions.get(r.nextInt(directions.size()));

                // add cells to maze region until hit a unconnected region or cannot carve anymore

                // if a carved maze connects an yet unconnected region,
                // add it and mentioned region to connected and remove from notConnected
                if (destination != null) {
                    notConnected.remove(destination);
                    connected.add(destination);
                    connected.add(maze.build());
                }
                break;
            }
        }
        System.out.println(AsciiPrinter.printStage(sb));
        return sb.build();
    }

    private static List<Tile.Direction> getAvailableDirections(int x, int y, @Nonnull Cell[][] stageGrid) {
        return getAdjacentAvailableTiles(x, y, stageGrid).stream()
                .map(tile -> tile.direction).collect(Collectors.toList());
    }

    private static Tile getNearPoint(@Nonnull Region from, @Nonnull Cell stageGrid[][], Random r) {
        ArrayList<Cell> cells = new ArrayList<>(from.getCellsList());
        Cell cell = cells.get(r.nextInt(cells.size()));
        List<Tile> adjacentTiles = getAdjacentAvailableTiles(cell.getX(), cell.getY(), stageGrid);
        if (adjacentTiles.isEmpty())
            return null;
        return adjacentTiles.get(r.nextInt(adjacentTiles.size()));
    }

    private static List<Tile> getAdjacentAvailableTiles(int x, int y, @Nonnull Cell stageGrid[][]) {
        List<Tile> result = getUpDownLeftRightTiles(x, y);
        return result.stream().filter((tile -> tile.available(stageGrid))).collect(Collectors.toList());
    }

    private static List<Tile> getUpDownLeftRightTiles(int x, int y) {
        List<Tile> result = new ArrayList<>();
        result.add(new Tile(x - 1, y, LEFT));
        result.add(new Tile(x, y - 1, UP));
        result.add(new Tile(x, y + 1, DOWN));
        result.add(new Tile(x + 1, y, RIGHT));
        return result;
    }

    static class Tile {
        final int x;
        final int y;
        final Direction direction;

        /**
         * @return whether the tile is inside stage and empty or not
         */
        boolean available(@Nonnull Cell stageGrid[][]) {
            return x >= 0 && x < stageGrid.length && y >= 0 && y < stageGrid[0].length
                    && stageGrid[x][y] == null;
        }

        Tile(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        enum Direction {
            UP, DOWN, LEFT, RIGHT
        }

    }

}
