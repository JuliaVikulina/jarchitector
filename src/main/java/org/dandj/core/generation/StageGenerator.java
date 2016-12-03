package org.dandj.core.generation;

import org.dandj.model.Cell;
import org.dandj.model.Region;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static org.dandj.model.Direction.*;

public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) {
        int roomSizeX = 3; //todo make input parameter
        int roomSizeY = 3; //todo make input parameter
        int roomTries = 5; //todo make input parameter
        float mazeStraightness = 0.1f; //todo make input parameter

        int xrange = stage.width();
        int yrange = stage.height();

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
            Region region = new Region();
            for (int x = 0; x < roomSizeX; x++) {
                for (int y = 0; y < roomSizeY; y++) {
                    Cell cell = new Cell().x(roomX + x).y(roomY + y).region(region);
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
                Cell currentCell = getStartCell(startingRegion, stageGrid, r);
                if (currentCell == null)
                    // todo connect this region using junction
                    continue;

                while (destinations.isEmpty() && currentCell != null) { // todo make protection from infinite loop
                    Cell cell = new Cell().x(currentCell.x()).y(currentCell.y()).region(maze);
                    maze.cells().add(cell);
                    stageGrid[currentCell.x()][currentCell.y()] = cell;
                    // todo check if it not connected already
                    destinations = findNearRegion(currentCell, stageGrid, startingRegion, notConnected);
                    if (destinations.isEmpty())
                        currentCell = getNextCell(currentCell, stageGrid, mazeStraightness, r);
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

    static Cell getNextCell(Cell currentCell, Cell[][] stageGrid, float mazeStraightness, Random r) {
        List<Cell> adjacentAvailableCells = getAdjacentAvailableCells(currentCell.x(), currentCell.y(), stageGrid);
        if (adjacentAvailableCells.isEmpty()) {
            return null;
        } else if (adjacentAvailableCells.size() == 1) {
            return adjacentAvailableCells.get(0);
        }

        List<Cell> oldDirCell = adjacentAvailableCells.stream()
                .filter(tile -> tile.direction() == currentCell.direction())
                .collect(Collectors.toList());

        boolean changeDirection = r.nextFloat() > mazeStraightness;
        if (!changeDirection && !oldDirCell.isEmpty()) {
            return oldDirCell.get(0);
        } else if (changeDirection) {
            adjacentAvailableCells.removeAll(oldDirCell);
        }

        return adjacentAvailableCells.get(r.nextInt(adjacentAvailableCells.size()));
    }

    static List<Region> findNearRegion(Cell newCell, Cell[][] stageGrid, Region startingRegion, Set<Region> notConnected) {
        return getUpDownLeftRightCells(newCell.x(), newCell.y()).stream()
                .filter(tile -> tile.insideStage(stageGrid))
                .filter(tile -> stageGrid[tile.x()][tile.y()] != null) // there is a cell at the point (x,y)
                .filter(tile -> !notConnected.contains(stageGrid[tile.x()][tile.y()].region()))
                .map(tile -> stageGrid[tile.x()][tile.y()].region()).collect(Collectors.toList());
    }

    static Cell getStartCell(@Nonnull Region from, @Nonnull Cell[][] stageGrid, Random r) {
        Cell startingCell = findValidStartingCell(from, stageGrid);
        if (startingCell == null)
            return null;
        List<Cell> adjacentCells = getAdjacentAvailableCells(startingCell.x(), startingCell.x(), stageGrid);
        return adjacentCells.get(r.nextInt(adjacentCells.size()));
    }

    static Cell findValidStartingCell(@Nonnull Region from, @Nonnull Cell[][] stageGrid) {
        for (Cell cell : from.cells()) {
            if (!getAdjacentAvailableCells(cell.x(), cell.y(), stageGrid).isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    static List<Cell> getAdjacentAvailableCells(int x, int y, @Nonnull Cell[][] stageGrid) {
        return getUpDownLeftRightCells(x, y).stream()
                .filter((tile -> tile.available(stageGrid)))
                .collect(Collectors.toList());
    }

    static List<Cell> getUpDownLeftRightCells(int x, int y) {
        List<Cell> result = new ArrayList<>();
        result.add(new Cell().x(x - 1).y(y).direction(LEFT));
        result.add(new Cell().x(x).y(y - 1).direction(UP));
        result.add(new Cell().x(x).y(y + 1).direction(DOWN));
        result.add(new Cell().x(x + 1).y(y).direction(RIGHT));
        return result;
    }

}
