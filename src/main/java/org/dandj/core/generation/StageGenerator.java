package org.dandj.core.generation;

import org.dandj.model.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.dandj.model.Direction.*;

public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) {
        initCells(stage);
        for (int i = 0; i < stage.roomTries(); i++) {
            addRoom(stage, r);
        }
        if (stage.regions().size() > 1) {
            // carve corridors to regions:
            Set<Region> notConnected = new HashSet<>(stage.regions());
            List<Region> connected = new LinkedList<>();
            notConnected.remove(stage.regions().get(0));
            connected.add(stage.regions().get(0));
            while (!notConnected.isEmpty()) {
                connectRegions(stage, r, notConnected, connected);
            }
        }
        return stage;
    }

    private static void connectRegions(Stage stage, Random r, Set<Region> notConnected, List<Region> connected) {
        // we carve a maze starting from any connected region
        Region startingRegion = connected.get(r.nextInt(connected.size()));
        Region maze = new Region();
        Map<Direction, Region> destinations = new HashMap<>();
        //todo add check if there are empty cells in a stage
        // add cells to maze region until we hit a unconnected region or cannot carve anymore
        Cell currentCell = getStartCell(startingRegion, stage.cells(), r);
        if (currentCell == null)
            // todo connect this region using junction
            return;
        boolean destinationFound = false;
        while (!destinationFound && currentCell != null) { // todo make protection from infinite loop
            maze.cells().add(currentCell);
            currentCell.region(maze);
            stage.cells()[currentCell.y()][currentCell.x()] = currentCell;
            // todo check if it not connected already
            destinations = findAdjacentRegions(currentCell, stage.cells(), notConnected);
            destinationFound = destinations.isEmpty();

            if (!destinationFound) {
                currentCell = getNextCell(currentCell, stage.cells(), stage.mazeStraightness(), r);
            }
        }

        // if a carved maze connects yet unconnected regions,
        // add some of them and newly created maze to connected and remove from notConnected
        if (destinations.size() > 0 && currentCell != null) {
//                    Collections.shuffle(destinations, r);
//                    destinations = destinations.subList(0, r.nextInt(destinations.size()) + 1);
//                    notConnected.removeAll(destinations);
//                    connected.addAll(destinations);
//                    connected.add(maze);
//                    stage.regions().add(maze);
            // todo: make several connections
            Map.Entry<Direction, Region> entry = destinations.entrySet().stream().findAny().get();
            notConnected.remove(entry.getValue());
            connected.add(entry.getValue());
            connected.add(maze);
            stage.regions().add(maze);
        } else {
            // maze was build but led nowhere
            // erase it from stageGrid
            for (Cell c : maze.cells()) {
                stage.cells()[c.y()][c.x()] = null;
            }
        }
    }

    private static void addRoom(Stage stage, Random r) {
        int roomSizeX = min(stage.width(), r.nextInt(stage.roomSizeXMax() - stage.roomSizeXMin() + 1) + stage.roomSizeXMin());
        int roomSizeY = min(stage.height(), r.nextInt(stage.roomSizeYMax() - stage.roomSizeYMin() + 1) + stage.roomSizeYMin());

        int roomX = r.nextInt(stage.width() - roomSizeX);
        int roomY = r.nextInt(stage.height() - roomSizeY);

        // check that new room does not overlap with existing ones
        for (int y = 0; y < roomSizeY; y++) {
            for (int x = 0; x < roomSizeX; x++) {
                if (stage.cells()[roomY + y][roomX + x] != null) {
                    return;
                }
            }
        }
        Region region = formRectangleRoom(stage.cells(), roomSizeX, roomSizeY, roomX, roomY);
//            Region region = formRaggedRoom(stageGrid, roomSizeX, roomSizeY, roomX, roomY, i);

        stage.regions().add(region);
    }

    private static Cell[][] initCells(Stage stage) {
        // simple two dimensional array to represent the layout
        Cell[][] stageGrid = new Cell[stage.height()][];
        for (int i = 0; i < stage.height(); i++) {
            stageGrid[i] = new Cell[stage.width()];
        }
        stage.cells(stageGrid);
        return stageGrid;
    }

    private static Region formRectangleRoom(Cell[][] stageGrid, int roomSizeX, int roomSizeY, int roomX, int roomY) {
        Region region = new Region();
        for (int x = 0; x < roomSizeX; x++) {
            for (int y = 0; y < roomSizeY; y++) {
                Cell cell = new Cell()
                        .x(roomX + x)
                        .y(roomY + y)
                        .region(region)
                        .type(CellType.ROOM)
                        .fragments(createCellfragments(x, y, roomSizeX, roomSizeY));
                region.cells().add(cell);
                stageGrid[roomY + y][roomX + x] = cell;
            }
        }
        return region;
    }

    private static Set<Fragment> createCellfragments(int x, int y, int roomSizeX, int roomSizeY) {
// TODO
        return null;
    }

    private static Region formRaggedRoom(Cell[][] stageGrid, int roomSizeX, int roomSizeY, int roomX, int roomY, int id, Random r) {
        Region region = new Region();

        int biasX = r.nextInt(max(roomSizeX / 2, 1));
        for (int x = biasX; x < roomSizeX - biasX; x++) {
            int biasY = r.nextInt(max(roomSizeY / 2, 1));
            for (int y = biasY; y < roomSizeY - biasY; y++) {
                Cell cell = new Cell()
                        .x(roomX + x)
                        .y(roomY + y)
                        .region(region)
                        .type(CellType.ROOM);
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

    static Map<Direction, Region> findAdjacentRegions(Cell newCell, Cell[][] stageGrid, Set<Region> notConnected) {
        return getUpDownLeftRightCells(newCell.x(), newCell.y()).stream()
                .filter(cell1 -> cell1.insideStage(stageGrid))
                .filter(cell1 -> stageGrid[cell1.y()][cell1.x()] != null) // there is a cell at the point (x,y)
                .filter(cell1 -> notConnected.contains(stageGrid[cell1.y()][cell1.x()].region()))
                .collect(Collectors.toMap(Cell::direction, cell -> stageGrid[cell.y()][cell.x()].region()));
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
