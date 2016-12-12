package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Stage {
    // requirements
    private int roomSizeXMin = 3;
    private int roomSizeXMax = 6;
    private int roomSizeYMin = 2;
    private int roomSizeYMax = 6;
    private int roomTries = 5;
    private float mazeStraightness = 0.1f;
    private int resolution;
    private int width;
    private int height;

    // result
    private List<Region> regions = new ArrayList<>();

    private transient Cell cells[][];
}
