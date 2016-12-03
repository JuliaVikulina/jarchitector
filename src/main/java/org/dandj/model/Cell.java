package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(fluent = true)
@Data
public class Cell {

    private int x;

    private int y;

    private Direction direction;

    private Region region;

    /**
     * @return whether the tile is inside stage and empty or not
     */
    public boolean available(@Nonnull Cell[][] stageGrid) {
        return insideStage(stageGrid)
                && stageGrid[y][x] == null;
    }

    public boolean insideStage(@Nonnull Cell[][] stageGrid) {
        return x >= 0 && x < stageGrid.length && y >= 0 && y < stageGrid[0].length;
    }

}
