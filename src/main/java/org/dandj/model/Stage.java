package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Stage {
    private String name;
    // requirements
    private int roomSizeXMin = 3;
    private int roomSizeXMax = 6;
    private int roomSizeYMin = 2;
    private int roomSizeYMax = 6;
    private int roomTries = 5;
    private float mazeStraightness = 0.1f;
    private int resolution = 16;
    private int width;
    private int height;

    // result
    private Collection<Region> regions = new ArrayList<>();
    private Collection<Junction> junctions = new ArrayList<>();
    private transient Cell cells[][];
}
