package org.dandj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

import static org.dandj.model.CellOrientation.*;
import static org.dandj.model.Direction.*;

@Accessors(fluent = true)
@Data
@ToString(exclude = "region")
//@EqualsAndHashCode(exclude = "region")
public class Cell {

    private int x;

    private int y;

    private Direction direction;

    private Direction previous;

    private CellOrientation orientation;

    private transient Region region;

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
        if (orientation == null) {
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
                orientation = VERTICAL;
            else throw new IllegalStateException("Illegal state of directions: next:" + next + " cell: " + this);
        } else {
            throw new IllegalStateException("Orientation must be null");
        }

    }

    public void merge(@Nonnull Direction from) {
        if (orientation == CORNER_UR && from == DOWN
                || orientation == CORNER_DR && from == UP
                || orientation == VERTICAL && from == LEFT) {
            orientation = CROSS_VR;
        } else if (orientation == CORNER_RU && from == UP
                || orientation == CORNER_RD && from == DOWN
                || orientation == VERTICAL && from == RIGHT) {
            orientation = CROSS_VL;
        } else if (orientation == CORNER_RD && from == LEFT
                || orientation == CORNER_UR && from == RIGHT
                || orientation == HORIZONTAL && from == UP) {
            orientation = CROSS_HD;
        } else if (orientation == CORNER_RU && from == LEFT
                || orientation == CORNER_DR && from == RIGHT
                || orientation == HORIZONTAL && from == DOWN) {
            orientation = CROSS_HU;
        } else {
//            throw new IllegalStateException("Could not merge from: " + from + " cell: " + this);
        }
    }

    public void branch(@Nonnull Direction to) {
        if (orientation == CORNER_UR && to == UP
                || orientation == CORNER_DR && to == DOWN
                || orientation == VERTICAL && to == RIGHT) {
            orientation = CROSS_VR;
        } else if (orientation == CORNER_RU && to == DOWN
                || orientation == CORNER_RD && to == UP
                || orientation == VERTICAL && to == LEFT) {
            orientation = CROSS_VL;
        } else if (orientation == CORNER_RD && to == RIGHT
                || orientation == CORNER_UR && to == LEFT
                || orientation == HORIZONTAL && to == DOWN) {
            orientation = CROSS_HD;
        } else if (orientation == CORNER_RU && to == RIGHT
                || orientation == CORNER_DR && to == LEFT
                || orientation == HORIZONTAL && to == UP) {
            orientation = CROSS_HU;
        } else {
//            throw new IllegalStateException("Could not branch to: " + to + " cell: " + this);
        }
    }
}
