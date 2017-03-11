package org.dandj.core.generation;

import com.jme3.math.Vector2f;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dandj.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.util.Collections.*;
import static org.dandj.model.Direction.*;
import static org.dandj.model.Fragment.*;

@Slf4j
public class StageGenerator {
    public static Stage createStage(Stage stage, Random r) {
        initCells(stage);
        for (int i = 0; i < stage.roomTries(); i++) {
            addRoom(stage, r);
        }
        setStartPosition(stage);
        connectRegionsByTwoSets(stage, r);
        return stage;
    }

    @SneakyThrows
    static void setStartPosition(Stage stage) {
        Optional<Region> firstRoom = stage.regions().stream().findFirst();
        if (firstRoom.isPresent()){
            Region startRoom = firstRoom.get();
            Optional<Cell> firstCell = startRoom.cells().stream().findFirst();
            if (firstCell.isPresent()){
                Cell startCell= firstCell.get();
                Vector2f startPosition = new Vector2f(startCell.getX() + 0.5f, startCell.getZ() + 0.5f);
                stage.startPosition(startPosition);
                log.debug("Set start position: " + startPosition);
            } else {
                throw new IllegalStateException("No cell was found in a room");
            }
        } else {
            throw new IllegalStateException("No room was found in a stage");
        }
    }

    static void connectRegionsByTwoSets(Stage stage, Random r) {
        if (stage.regions().size() <= 1)
            return;
        // carve corridors to regions:
        List<Region> notConnected = new LinkedList<>(stage.regions());
        List<Region> connected = new LinkedList<>();
        Region region = stage.regions().stream().findFirst().get();
        notConnected.remove(region);
        connected.add(region);
        while (!notConnected.isEmpty()) {
            connectRegions(stage, r, notConnected, connected);
        }
        stage.regions().forEach(room -> formRegionFragments(room, stage.cells(), stage.junctions()));
    }

    private static boolean stageHasEmptyCells(Stage stage) {
        for (Cell[] row : stage.cells())
            for (Cell cell : row)
                if (cell == null)
                    return true;
        return false;
    }

    private static void connectRegions(Stage stage, Random r, List<Region> notConnected, List<Region> connected) {
        if (!stageHasEmptyCells(stage)) {
            // pick random unconnected region, pick a border cell from it and make a door outwards
            Region region = notConnected.get(r.nextInt(notConnected.size()));
            Junction junction = getAnyJunctionToDifferentRegion(region, r, connected, stage.cells());
            if (junction == null)
                return; // this is LITERALLY impossible (currently)
            stage.addJunction(junction.from().toPoint(), junction.to().toPoint(), junction);
            notConnected.remove(region);
            connected.add(region);
            return;
        }
        // we carve a maze starting from any connected region
        Region startingRegion = connected.get(r.nextInt(connected.size()));
        Region maze = new Region("maze");
        List<Junction> destinations = new ArrayList<>();
        Junction starting = getStartPosition(startingRegion, stage.cells(), r);
        if (starting == null)
            return;
        Cell currentCell = starting.to();
        while (destinations.isEmpty() && currentCell != null) { // todo make protection from infinite loop
            currentCell.setType(CellType.MAZE);
            maze.cells().add(currentCell);
            currentCell.setRegion(maze);
            stage.cells()[currentCell.getZ()][currentCell.getX()] = currentCell;
            destinations = findAdjacentRegions(currentCell, stage.cells(), notConnected);
            if (destinations.isEmpty()) {
                currentCell = getNextCell(currentCell, stage.cells(), stage.mazeStraightness(), r);
            }
        }

        // if a carved maze connects yet unconnected regions,
        // add some of them and newly created maze to connected and remove from notConnected.
        // currentCell will never be null here, but check is required
        if (destinations.size() > 0 && currentCell != null) {
            // todo: make several connections
            Junction ending = destinations.get(0);
            stage.addJunction(starting.from().toPoint(), starting.to().toPoint(), starting);
            stage.addJunction(ending.from().toPoint(), ending.to().toPoint(), ending);
            notConnected.remove(ending.from().getRegion());
            connected.add(ending.from().getRegion());
            connected.add(maze);
            stage.regions().add(maze);
        } else {
            // maze was build but led nowhere
            // erase it from stageGrid
            for (Cell c : maze.cells()) {
                stage.cells()[c.getZ()][c.getX()] = null;
            }
        }
    }

    /**
     * pick any junction that will lead to another (connected) region
     */
    private static Junction getAnyJunctionToDifferentRegion(Region region, Random r, List<Region> connected, Cell[][] cells) {
        List<Cell> shuffled = region.cells();
        shuffle(shuffled, r);
        for (Cell cell : shuffled) {
            for (Cell adjacent : getUpDownLeftRightCells(cell.getX(), cell.getZ()).stream().filter(c -> c.insideStage(cells)).collect(Collectors.toList())) {
                Cell targetCell = cells[adjacent.getZ()][adjacent.getX()];
                if (targetCell != null && targetCell.getRegion() != null && connected.contains(targetCell.getRegion())) {
                    return new Junction().from(cell).to(targetCell);
                }
            }
        }
        return null;
    }


    private static void addRoom(Stage stage, Random r) {
        int roomSizeX = min(stage.width(), r.nextInt(stage.roomSizeXMax() - stage.roomSizeXMin() + 1) + stage.roomSizeXMin());
        int roomSizeZ = min(stage.height(), r.nextInt(stage.roomSizeZMax() - stage.roomSizeZMin() + 1) + stage.roomSizeZMin());

        int roomX = r.nextInt(stage.width() - roomSizeX + 1);
        int roomZ = r.nextInt(stage.height() - roomSizeZ + 1);
        addRoom(stage, roomSizeX, roomSizeZ, roomX, roomZ);
    }

    public static void addRoom(Stage stage, int roomSizeX, int roomSizeZ, int roomX, int roomZ) {
        // check that new room does not overlap with existing ones
        for (int z = 0; z < roomSizeZ; z++) {
            for (int x = 0; x < roomSizeX; x++) {
                if (stage.cells()[roomZ + z][roomX + x] != null) {
                    return;
                }
            }
        }
        Region region = formRectangleRoom(stage.cells(), roomSizeX, roomSizeZ, roomX, roomZ);
        stage.regions().add(region);
    }

    public static Cell[][] initCells(Stage stage) {
        // simple two dimensional array to represent the layout
        Cell[][] stageGrid = new Cell[stage.height()][];
        for (int i = 0; i < stage.height(); i++) {
            stageGrid[i] = new Cell[stage.width()];
        }
        stage.cells(stageGrid);
        return stageGrid;
    }

    private static Region formRectangleRoom(Cell[][] stageGrid, int roomSizeX, int roomSizeZ, int roomX, int roomZ) {
        Region region = new Region("room");
        for (int x = 0; x < roomSizeX; x++) {
            for (int z = 0; z < roomSizeZ; z++) {
                Cell cell = new Cell(roomX + x, roomZ + z, region, CellType.ROOM);
                region.cells().add(cell);
                stageGrid[roomZ + z][roomX + x] = cell;
            }
        }
        return region;
    }

    private static Cell getNextCell(Cell currentCell, Cell[][] stageGrid, float mazeStraightness, Random r) {
        List<Cell> adjacentAvailableCells = getAdjacentAvailableCells(currentCell.getX(), currentCell.getZ(), stageGrid);
        if (adjacentAvailableCells.isEmpty()) {
            return null;
        } else if (adjacentAvailableCells.size() == 1) {
            return adjacentAvailableCells.get(0);
        }

        List<Cell> oldDirCell = adjacentAvailableCells.stream()
                .filter(cell -> cell.getDirection() == currentCell.getDirection())
                .collect(Collectors.toList());

        boolean changeDirection = r.nextFloat() > mazeStraightness;
        if (!changeDirection && !oldDirCell.isEmpty()) {
            return oldDirCell.get(0);
        } else if (changeDirection) {
            adjacentAvailableCells.removeAll(oldDirCell);
        }

        return adjacentAvailableCells.get(r.nextInt(adjacentAvailableCells.size()));
    }

    private static List<Junction> findAdjacentRegions(Cell newCell, Cell[][] stageGrid, List<Region> notConnected) {
        return getUpDownLeftRightCells(newCell.getX(), newCell.getZ()).stream()
                .filter(cell -> cell.insideStage(stageGrid))
                .filter(cell -> stageGrid[cell.getZ()][cell.getX()] != null) // there is a cell at the point (getX,z)
                .filter(cell -> notConnected.contains(stageGrid[cell.getZ()][cell.getX()].getRegion())) // connect only unconnected regions
                .map(cell -> new Junction().to(newCell).from(stageGrid[cell.getZ()][cell.getX()]))
                .collect(Collectors.toList());
    }

    private static Junction getStartPosition(Region from, Cell[][] stageGrid, Random r) {
        Cell startingCell = findValidStartingCell(from, stageGrid);
        if (startingCell == null)
            return null;
        List<Cell> adjacentCells = getAdjacentAvailableCells(startingCell.getX(), startingCell.getZ(), stageGrid);
        Cell targetCell = adjacentCells.get(r.nextInt(adjacentCells.size()));
        return new Junction()
                .from(startingCell)
                .to(targetCell);
    }

    private static Cell findValidStartingCell(Region from, Cell[][] stageGrid) {
        for (Cell cell : from.cells()) {
            if (!getAdjacentAvailableCells(cell.getX(), cell.getZ(), stageGrid).isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    static List<Cell> getAdjacentAvailableCells(int x, int z, Cell[][] stageGrid) {
        return getUpDownLeftRightCells(x, z).stream()
                .filter((cell -> cell.available(stageGrid)))
                .collect(Collectors.toList());
    }

    static List<Cell> getUpDownLeftRightCells(int x, int z) {
        List<Cell> result = new ArrayList<>();
        Cell c = new Cell(x, z);
        result.add(up(c));
        result.add(down(c));
        result.add(left(c));
        result.add(right(c));
        return result;
    }

    /**
     * Add appropriate fragments to each cell in a region
     *
     * @param r         Region to fill
     * @param junctions
     */
    static void formRegionFragments(Region r, Cell[][] grid, Map<Set<Point>, Junction> junctions) {
        r.cells().forEach(cell -> {
            Set<Fragment> fragments = cell.getFragments();
            getUpDownLeftRightCells(cell.getX(), cell.getZ()).forEach(c -> {
                if (!isCellInRegion(r, grid, c, cell, junctions)) {
                    switch (c.getDirection()) {
                        case UP:
                            fragments.add(WALL_U);
                            // UL
                            if (isCellInRegion(r, grid, new Cell(cell.getX() - 1, cell.getZ()), cell, junctions)) {
                                fragments.add(CORNER_UL_H);
                            } else {
                                fragments.add(CORNER_UL_INNER);
                            }
                            // UR
                            if (isCellInRegion(r, grid, new Cell(cell.getX() + 1, cell.getZ()), cell, junctions)) {
                                fragments.add(CORNER_UR_H);
                            } else {
                                fragments.add(CORNER_UR_INNER);
                            }
                            break;
                        case DOWN:
                            fragments.add(WALL_D);
                            // DL
                            if (isCellInRegion(r, grid, new Cell(cell.getX() - 1, cell.getZ()), cell, junctions)) {
                                fragments.add(CORNER_DL_H);
                            } else {
                                fragments.add(CORNER_DL_INNER);
                            }
                            // DR
                            if (isCellInRegion(r, grid, new Cell(cell.getX() + 1, cell.getZ()), cell, junctions)) {
                                fragments.add(CORNER_DR_H);
                            } else {
                                fragments.add(CORNER_DR_INNER);
                            }
                            break;
                        case LEFT:
                            fragments.add(WALL_L);
                            // UL
                            if (isCellInRegion(r, grid, new Cell(cell.getX(), cell.getZ() - 1), cell, junctions)) {
                                fragments.add(CORNER_UL_V);
                            } else {
                                fragments.add(CORNER_UL_INNER);
                            }
                            // DL
                            if (isCellInRegion(r, grid, new Cell(cell.getX(), cell.getZ() + 1), cell, junctions)) {
                                fragments.add(CORNER_DL_V);
                            } else {
                                fragments.add(CORNER_DL_INNER);
                            }
                            break;
                        case RIGHT:
                            fragments.add(WALL_R);
                            // UR
                            if (isCellInRegion(r, grid, new Cell(cell.getX(), cell.getZ() - 1), cell, junctions)) {
                                fragments.add(CORNER_UR_V);
                            } else {
                                fragments.add(CORNER_UR_INNER);
                            }
                            // DR
                            if (isCellInRegion(r, grid, new Cell(cell.getX(), cell.getZ() + 1), cell, junctions)) {
                                fragments.add(CORNER_DR_V);
                            } else {
                                fragments.add(CORNER_DR_INNER);
                            }
                            break;
                    }
                }
            });
            if (isCellInRegion(r, grid, left(cell), cell, junctions) && isCellInRegion(r, grid, up(cell), cell, junctions) && !isCellInRegion(r, grid, new Cell(cell.getX() - 1, cell.getZ() - 1), cell, junctions)) {
                fragments.add(CORNER_UL_OUTER);
            }
            if (isCellInRegion(r, grid, right(cell), cell, junctions) && isCellInRegion(r, grid, up(cell), cell, junctions) && !isCellInRegion(r, grid, new Cell(cell.getX() + 1, cell.getZ() - 1), cell, junctions)) {
                fragments.add(CORNER_UR_OUTER);
            }
            if (isCellInRegion(r, grid, left(cell), cell, junctions) && isCellInRegion(r, grid, down(cell), cell, junctions) && !isCellInRegion(r, grid, new Cell(cell.getX() - 1, cell.getZ() + 1), cell, junctions)) {
                fragments.add(CORNER_DL_OUTER);
            }
            if (isCellInRegion(r, grid, right(cell), cell, junctions) && isCellInRegion(r, grid, down(cell), cell, junctions) && !isCellInRegion(r, grid, new Cell(cell.getX() + 1, cell.getZ() + 1), cell, junctions)) {
                fragments.add(CORNER_DR_OUTER);
            }
            cell.getFragments().add(FLOOR);
            cell.getFragments().add(CEILING);
        });
    }

    /**
     * checks if the cell 'a' belongs to the region r,
     * or there is a junction from cell 'a' to 'b'
     */
    static boolean isCellInRegion(Region r, Cell[][] grid, Cell a, Cell b, Map<Set<Point>, Junction> junctions) {
        return a.insideStage(grid)
                && grid[a.getZ()][a.getX()] != null
                && grid[a.getZ()][a.getX()].getRegion().equals(r)
                || junctions.containsKey(unmodifiableSet(new HashSet<Point>() {{
            add(a.toPoint());
            add(b.toPoint());
        }}));
    }

    private static Cell up(Cell c) {
        return new Cell(c.getX(), c.getZ() - 1, UP);
    }

    private static Cell left(Cell c) {
        return new Cell(c.getX() - 1, c.getZ(), LEFT);
    }

    private static Cell down(Cell c) {
        return new Cell(c.getX(), c.getZ() + 1, DOWN);
    }

    private static Cell right(Cell c) {
        return new Cell(c.getX() + 1, c.getZ(), RIGHT);
    }

}
