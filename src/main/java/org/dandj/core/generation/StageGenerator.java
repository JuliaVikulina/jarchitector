package org.dandj.core.generation;

import org.dandj.model.*;
import org.dandj.utils.SvgPrinter;

import javax.annotation.Nonnull;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static org.dandj.model.Direction.*;
import static org.dandj.model.Fragment.*;

public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) throws IOException, ParserConfigurationException {
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

    private static void connectRegions(Stage stage, Random r, Set<Region> notConnected, List<Region> connected) throws IOException, ParserConfigurationException {
        // we carve a maze starting from any connected region
        Region startingRegion = connected.get(r.nextInt(connected.size()));
        Region maze = new Region();
        Map<Direction, Region> destinations = new HashMap<>();
        //todo add check if there are empty cells in a stage
        // add cells to maze region until we hit a unconnected region or cannot carve anymore
        Cell currentCell = getStartCell(startingRegion, stage.cells(), r);
        Cell previousCell = null;
        if (currentCell == null)
            return;
        // todo connect this region using junction
        boolean destinationFound = false;
        while (!destinationFound && currentCell != null) { // todo make protection from infinite loop
            if (previousCell != null) {
                // we are inside the maze. let's create walls for previous cell
                previousCell.fragments(createMazeWalls(currentCell.direction(), previousCell.direction()));
            }
            currentCell.type(CellType.MAZE);
            currentCell.fragments().add(FLOOR);
            maze.cells().add(currentCell);
            currentCell.region(maze);
            stage.cells()[currentCell.y()][currentCell.x()] = currentCell;
            // todo check if it not connected already
            destinations = findAdjacentRegions(currentCell, stage.cells(), notConnected);
            destinationFound = !destinations.isEmpty();

            if (!destinationFound) {
                previousCell = currentCell;
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
            currentCell.fragments(createMazeWalls(entry.getKey(), currentCell.direction()));
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

    private static Set<Fragment> createMazeWalls(Direction current, Direction previous) {
        HashSet<Fragment> fragments = new HashSet<>();
        if (previous == UP && current == UP || previous == DOWN && current == DOWN) {
            fragments.addAll(asList(WALL_L, WALL_R, CORNER_DL_V, CORNER_DR_V, CORNER_UL_V, CORNER_UR_V));
        } else if (previous == LEFT && current == LEFT || previous == RIGHT && current == RIGHT) {
            fragments.addAll(asList(WALL_U, WALL_D, CORNER_DL_H, CORNER_DR_H, CORNER_UL_H, CORNER_UR_H));
        } else if (previous == UP && current == RIGHT || previous == LEFT && current == DOWN) {
            fragments.addAll(asList(WALL_L, WALL_U, CORNER_UL_INNER, CORNER_DR_OUTER, CORNER_UR_H, CORNER_DL_V));
        } else if (previous == DOWN && current == LEFT || previous == RIGHT && current == UP) {
            fragments.addAll(asList(WALL_D, WALL_R, CORNER_UL_OUTER, CORNER_UR_V, CORNER_DR_INNER, CORNER_DL_H));
        } else if (previous == DOWN && current == RIGHT || previous == LEFT && current == UP) {
            fragments.addAll(asList(WALL_L, WALL_D, CORNER_DL_INNER, CORNER_DR_H, CORNER_UL_V, CORNER_UR_OUTER));
        } else if (previous == RIGHT && current == DOWN || previous == UP && current == LEFT) {
            fragments.addAll(asList(WALL_U, WALL_R, CORNER_DL_OUTER, CORNER_DR_V, CORNER_UL_H, CORNER_UR_INNER));
        } else {
            throw new IllegalStateException("wtf: " + current + previous);
        }
        fragments.add(FLOOR);
        return fragments;
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
                        .fragments(createRoomWalls(x, y, roomSizeX, roomSizeY));
                region.cells().add(cell);
                stageGrid[roomY + y][roomX + x] = cell;
            }
        }
        return region;
    }

    private static Set<Fragment> createRoomWalls(int x, int y, int roomSizeX, int roomSizeY) {
        Set<Fragment> fragments = new HashSet<>();
        if (x == 0) {
            fragments.add(WALL_L);
        }
        if (x == roomSizeX - 1) {
            fragments.add(WALL_R);
        }
        if (y == 0) {
            fragments.add(WALL_U);
        }
        if (y == roomSizeY - 1) {
            fragments.add(WALL_D);
        }
        // Corners
        if (x == 0 && y == 0) {
            fragments.add(CORNER_UL_INNER);
        }
        if (x == roomSizeX - 1 && y == 0) {
            fragments.add(CORNER_UR_INNER);
        }
        if (y == roomSizeY - 1 && x == roomSizeX - 1) {
            fragments.add(CORNER_DR_INNER);
        }
        if (y == roomSizeY - 1 && x == 0) {
            fragments.add(CORNER_DL_INNER);
        }
        // fillers
        if (x == 0) {
            if (y != roomSizeY - 1)
                fragments.add(CORNER_DL_V);
            if (y != 0)
                fragments.add(CORNER_UL_V);
        }

        if (x == roomSizeX - 1) {
            if (y != roomSizeY - 1)
                fragments.add(CORNER_DR_V);
            if (y != 0)
                fragments.add(CORNER_UR_V);
        }

        if (y == 0) {
            if (x != roomSizeX - 1)
                fragments.add(CORNER_UR_H);
            if (x != 0)
                fragments.add(CORNER_UL_H);
        }

        if (y == roomSizeY - 1) {
            if (x != roomSizeX - 1)
                fragments.add(CORNER_DR_H);
            if (x != 0)
                fragments.add(CORNER_DL_H);
        }
        fragments.add(FLOOR);
        return fragments;
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
                .filter(cell -> cell.insideStage(stageGrid))
                .filter(cell -> stageGrid[cell.y()][cell.x()] != null) // there is a cell at the point (x,y)
                .filter(cell -> notConnected.contains(stageGrid[cell.y()][cell.x()].region()))
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
