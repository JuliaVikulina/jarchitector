package org.dandj.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Accessors(chain = true)
@Data
@ToString(exclude = {"region", "fragments"})
public class Cell {
    private int x;

    private int z;

    private transient Region region;

    private Set<Fragment> fragments = new HashSet<>();

    private CellType type;

    private Direction direction;

    public Cell(int x, int z, Region region, CellType type, Collection<Fragment> fragments) {
        this.x = x;
        this.z = z;
        this.region = region;
        this.fragments = new HashSet<>(fragments);
        this.type = type;
    }

    public Cell(int x, int z, Direction direction) {
        this.x = x;
        this.z = z;
        this.direction = direction;
    }

    public Cell(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * @return whether the tile is inside stage and empty or not
     */
    public boolean available(Cell[][] stageGrid) {
        return insideStage(stageGrid)
                && stageGrid[z][x] == null;
    }

    public boolean insideStage(Cell[][] stageGrid) {
        return z >= 0 && z < stageGrid.length && x >= 0 && x < stageGrid[0].length;
    }
}