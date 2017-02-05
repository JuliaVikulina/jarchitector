package org.dandj.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@Accessors(fluent = true)
@Data
@ToString(exclude = {"region","fragments"})
public class Cell {
    private int x;

    private int y;

    private transient Region region;

    private Set<Fragment> fragments = new HashSet<>();

    private CellType type;

    private Direction direction;

    /**
     * @return whether the tile is inside stage and empty or not
     */
    public boolean available(@Nonnull Cell[][] stageGrid) {
        return insideStage(stageGrid)
                && stageGrid[y][x] == null;
    }

    public boolean insideStage(@Nonnull Cell[][] stageGrid) {
        return y >= 0 && y < stageGrid.length && x >= 0 && x < stageGrid[0].length;
    }
}
