package org.dandj.core.generation;

import org.dandj.model.Cell;
import org.dandj.model.Direction;
import org.dandj.model.Region;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static org.dandj.model.Direction.*;

public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) {


        int xrange = stage.width();
        int yrange = stage.height();

        // simple two dimensional array to represent the layout
        Cell[][] stageGrid = new Cell[yrange][];
        for (int i = 0; i < yrange; i++) {
            stageGrid[i] = new Cell[xrange];
        }
        stage.cells(stageGrid);
        int regionId = 0;
        createRoom:
        for (int i = 0; i < stage.roomTries(); i++) {

            int roomSizeX = r.nextInt(stage.roomSizeXMax() - stage.roomSizeXMin() + 1) + stage.roomSizeXMin();
            int roomSizeY = r.nextInt(stage.roomSizeYMax() - stage.roomSizeYMin() + 1) + stage.roomSizeYMin();

            int roomX = r.nextInt(xrange - roomSizeX);
            int roomY = r.nextInt(yrange - roomSizeY);

            // check that new room does not overlap with existing ones
            for (int y = 0; y < roomSizeY; y++) {
                for (int x = 0; x < roomSizeX; x++) {
                    if (stageGrid[roomY + y][roomX + x] != null) {
                        continue createRoom;
                    }
                }
            }
            Region region = formRectangleRoom(stageGrid, roomSizeX, roomSizeY, roomX, roomY, regionId++);
//            Region region = formRaggedRoom(stageGrid, roomSizeX, roomSizeY, roomX, roomY, i, regionId++);

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
                Region maze = new Region(regionId++);
                List<Region> destinations = new ArrayList<>();
                //todo add check if there are empty cells in a stage
                Cell currentCell = getStartCell(startingRegion, stageGrid, r);
                if (currentCell == null)
                    // todo connect this region using junction
                    continue;
                Direction previous = null;
                while (destinations.isEmpty() && currentCell != null) { // todo make protection from infinite loop
                    Cell cell = new Cell()
                            .x(currentCell.x())
                            .y(currentCell.y())
                            .direction(currentCell.direction())
                            .previous(previous)
                            .region(maze);
                    maze.cells().add(cell);
                    stageGrid[cell.y()][cell.x()] = cell;
                    // todo check if it not connected already
                    destinations = findNearRegion(cell, stageGrid, startingRegion, notConnected);
                    if (destinations.isEmpty()) {
                        previous = currentCell.direction();
                        currentCell = getNextCell(cell, stageGrid, stage.mazeStraightness(), r);
                    }
                }
                // add cells to maze region until we hit a unconnected region or cannot carve anymore

                // if a carved maze connects yet unconnected regions,
                // add some of them and newly created maze to connected and remove from notConnected
                if (destinations.size() > 0) {
                    Collections.shuffle(destinations, r);
                    destinations = destinations.subList(0, r.nextInt(destinations.size()) + 1);
                    notConnected.removeAll(destinations);
                    connected.addAll(destinations);
                    connected.add(maze);
                    stage.regions().add(maze);
                } else {
                    // maze was build but led nowhere
                    // erase it from stageGrid
                    for (Cell cell : maze.cells()) {
                        stageGrid[cell.y()][cell.x()] = null;
                    }
                }
            }
        }

        return stage;
    }

    private static Region formRectangleRoom(Cell[][] stageGrid, int roomSizeX, int roomSizeY, int roomX, int roomY, int id) {
        Region region = new Region(id);
        for (int x = 0; x < roomSizeX; x++) {
            for (int y = 0; y < roomSizeY; y++) {
                Cell cell = new Cell().x(roomX + x).y(roomY + y).region(region);
                region.cells().add(cell);
                stageGrid[roomY + y][roomX + x] = cell;
            }
        }
        return region;
    }

    private static Region formRaggedRoom(Cell[][] stageGrid, int roomSizeX, int roomSizeY, int roomX, int roomY, int id, Random r) {
        Region region = new Region(id);

        int biasX = r.nextInt(max(roomSizeX / 2, 1));
        for (int x = biasX; x < roomSizeX - biasX; x++) {
            int biasY = r.nextInt(max(roomSizeY / 2, 1));
            for (int y = biasY; y < roomSizeY - biasY; y++) {
                Cell cell = new Cell().x(roomX + x).y(roomY + y).region(region);
                region.cells().add(cell);
                stageGrid[roomY + y][roomX + x] = cell;
            }
        }
        return region;
    }

    static Cell getNextCell(Cell currentCell, Cell[][] stageGrid, float mazeStraightness, Random r) {
        List<Cell> adjacentAvailableCells = getAdjacentAvailableCells(currentCell.x(), currentCell.y(), stageGrid);
        if (adjacentAvailableCells.isEmpty()) {
            return null;
        } else if (adjacentAvailableCells.size() == 1) {
            return adjacentAvailableCells.get(0);
        }

        List<Cell> oldDirCell = adjacentAvailableCells.stream()
                .filter(cell -> cell.direction() == currentCell.direction())
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
                .filter(cell -> cell.insideStage(stageGrid))
                .filter(cell -> stageGrid[cell.y()][cell.x()] != null) // there is a cell at the point (x,y)
                .filter(cell -> notConnected.contains(stageGrid[cell.y()][cell.x()].region())) // we do not want to connect connected regions
                .map(cell -> stageGrid[cell.y()][cell.x()].region()).collect(Collectors.toList());
    }

    static Cell getStartCell(@Nonnull Region from, @Nonnull Cell[][] stageGrid, Random r) {
        Cell startingCell = findValidStartingCell(from, stageGrid);
        if (startingCell == null)
            return null;
        List<Cell> adjacentCells = getAdjacentAvailableCells(startingCell.x(), startingCell.y(), stageGrid);
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
                .filter((cell -> cell.available(stageGrid)))
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
