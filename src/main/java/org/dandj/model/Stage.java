package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Stage {
    int roomSizeXMin = 3;
    int roomSizeXMax = 6;
    int roomSizeYMin = 2;
    int roomSizeYMax = 6;

    int roomTries = 5;
    float mazeStraightness = 0.1f;
    private List<Region> regions = new ArrayList<>();
    private int resolution;
    private int width;
    private int height;

    private transient Cell cells[][];
}
