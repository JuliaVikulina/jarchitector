package org.dandj.model;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction reverse() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        throw new IllegalStateException("Unknown direction: " + this);
    }
}
