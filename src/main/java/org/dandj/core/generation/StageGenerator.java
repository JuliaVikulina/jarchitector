package org.dandj.core.generation;

import org.dandj.model.Cell;
import org.dandj.model.Region;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static org.dandj.core.generation.StageGenerator.Tile.Direction.*;

public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) {
        int roomSizeX = 3; //todo make input parameter
        int roomSizeY = 3; //todo make input parameter
        int roomTries = 5; //todo make input parameter
        float mazeStraightness = 0.1f; //todo make input parameter

        int xrange = stage.width();
        int yrange = stage.height();

        // simple two dimensional array to represent the layout
        Cell[][] stageGrid  = new Cell[xrange][];
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
            Region region = new Region();
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    Cell cell = new Cell(roomX + x,roomY + y, region);
                    region.cells().add(cell);
                    stageGrid[roomX + x][roomY + y] = cell;
                }
            }
            stage.regions().add(region);
        }

        if (stage.regions().size() > 1) {
            // carve corridors to regions:
            Set<Region> notConnected = new HashSet<>(stage.regions());
            List<Region> connected = new LinkedList<>();

            notConnected.remove(stage.regions().get(0));
            connected.add(stage.regions().get(0));
            while (!notConnected.isEmpty()) {
                // we carve a maze starting from any connected region
                Region startingRegion = connected.get(r.nextInt(connected.size()));
                Region maze = new Region();
                List<Region> destinations = new ArrayList<>();
                //todo add check if there are empty cells in a stage
                Tile currentTile = getStartTile(startingRegion, stageGrid, r);
                if (currentTile == null)
                    // todo connect this region using junction
                    continue;

                while (destinations.isEmpty() && currentTile != null) { // todo make protection from infinite loop
                    Cell cell = new Cell(currentTile.x, currentTile.y, maze);
                    maze.cells().add(cell);
                    stageGrid[currentTile.x][currentTile.y] = cell;
                    // todo check if it not connected already
                    destinations = findNearRegion(currentTile, stageGrid, startingRegion, notConnected);
                    if (destinations.isEmpty())
                        currentTile = getNextTile(currentTile, stageGrid, mazeStraightness, r);
                }
                // add cells to maze region until we hit a unconnected region or cannot carve anymore

                // if a carved maze connects yet unconnected regions,
                // add some of them and newly created maze to connected and remove from notConnected
                Collections.shuffle(destinations, r);
                List<Region> toAdd = destinations.subList(0, r.nextInt(destinations.size() - 1) + 1);
                notConnected.removeAll(toAdd);
                connected.addAll(toAdd);
                connected.add(maze);
            }
        }
        return stage;
    }

    static Tile getNextTile(Tile currentTile, Cell[][] stageGrid, float mazeStraightness, Random r) {
        List<Tile> adjacentAvailableTiles = getAdjacentAvailableTiles(currentTile.x, currentTile.y, stageGrid);
        if (adjacentAvailableTiles.isEmpty()) {
            return null;
        } else if (adjacentAvailableTiles.size() == 1) {
            return adjacentAvailableTiles.get(0);
        }

        List<Tile> oldDirTile = adjacentAvailableTiles.stream()
                .filter(tile -> tile.direction == currentTile.direction)
                .collect(Collectors.toList());

        boolean changeDirection = r.nextFloat() > mazeStraightness;
        if (!changeDirection && !oldDirTile.isEmpty()) {
            return oldDirTile.get(0);
        } else if (changeDirection) {
            adjacentAvailableTiles.removeAll(oldDirTile);
        }

        return adjacentAvailableTiles.get(r.nextInt(adjacentAvailableTiles.size()));
    }

    static List<Region> findNearRegion(Tile newTile, Cell[][] stageGrid, Region startingRegion, Set<Region> notConnected) {
        return getUpDownLeftRightTiles(newTile.x, newTile.y).stream()
                .filter(tile -> tile.insideStage(stageGrid))
                .filter(tile -> stageGrid[tile.x][tile.y] != null) // there is a cell at the point (x,y)
                .filter(tile -> !notConnected.contains(stageGrid[tile.x][tile.y].region()))
                .map(tile -> stageGrid[tile.x][tile.y].region()).collect(Collectors.toList());
    }

    static Tile getStartTile(@Nonnull Region from, @Nonnull Cell stageGrid[][], Random r) {
        Cell startingCell = findValidStartingCell(from, stageGrid);
        if (startingCell == null)
            return null;
        List<Tile> adjacentTiles = getAdjacentAvailableTiles(startingCell.x(), startingCell.x(), stageGrid);
        return adjacentTiles.get(r.nextInt(adjacentTiles.size()));
    }

    static Cell findValidStartingCell(@Nonnull Region from, @Nonnull Cell[][] stageGrid) {
        for (Cell cell : from.cells()) {
            if (!getAdjacentAvailableTiles(cell.x(), cell.y(), stageGrid).isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    static List<Tile> getAdjacentAvailableTiles(int x, int y, @Nonnull Cell[][] stageGrid) {
        return getUpDownLeftRightTiles(x, y).stream()
                .filter((tile -> tile.available(stageGrid)))
                .collect(Collectors.toList());
    }

    static List<Tile> getUpDownLeftRightTiles(int x, int y) {
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

        Tile(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        /**
         * @return whether the tile is inside stage and empty or not
         */
        boolean available(@Nonnull Cell stageGrid[][]) {
            return insideStage(stageGrid)
                    && stageGrid[x][y] == null;
        }

        boolean insideStage(@Nonnull Cell[][] stageGrid) {
            return x >= 0 && x < stageGrid.length && y >= 0 && y < stageGrid[0].length;
        }

        enum Direction {
            UP, DOWN, LEFT, RIGHT
        }
    }

}
