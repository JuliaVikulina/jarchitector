package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Stage {
    private int roomSizeXMin = 3;
    private int roomSizeXMax = 6;
    private int roomSizeYMin = 2;
    private int roomSizeYMax = 6;
    private int roomTries = 5;
    private float mazeStraightness = 0.1f;
    private List<Region> regions = new ArrayList<>();
    private int resolution;
    private int width;
    private int height;

    private transient Cell cells[][];
}
