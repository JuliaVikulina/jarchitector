package org.dandj.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

import static org.dandj.model.CellOrientation.*;
import static org.dandj.model.Direction.*;

@Accessors(fluent = true)
@Data
@ToString(exclude = "region")
public class Cell {

    private int x;

    private int y;

    private Direction direction;

    private Direction previous;

    private CellOrientation orientation;

    private Region region;

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

    public void orient(@Nonnull Direction next) {
        if (direction == UP && next == RIGHT || direction == LEFT && next == DOWN)
            orientation = CORNER_UR;
        else if (direction == RIGHT && next == DOWN || direction == UP && next == LEFT)
            orientation = CORNER_RD;
        else if (direction == DOWN && next == RIGHT || direction == LEFT && next == UP)
            orientation = CORNER_DR;
        else if (direction == RIGHT && next == UP || direction == DOWN && next == LEFT)
            orientation = CORNER_RU;
        else if (direction == RIGHT && next == RIGHT || direction == LEFT && next == LEFT)
            orientation = HORIZONTAL;
        else if (direction == UP && next == UP || direction == DOWN && next == DOWN)
            orientation = CellOrientation.VERTICAL;
    }

}
