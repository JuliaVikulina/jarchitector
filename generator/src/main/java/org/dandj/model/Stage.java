package org.dandj.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;

@Accessors(fluent = true)
@Data
public class Stage {
    private String name;
    // requirements
    private int roomSizeXMin = 3;
    private int roomSizeXMax = 6;
    private int roomSizeZMin = 2;
    private int roomSizeZMax = 6;
    private int roomTries = 5;
    private float mazeStraightness = 0.1f;
    private int resolution = 16;
    private int width;
    private int height;
    private boolean mergeObjects;

    // result
    private Collection<Region> regions = new ArrayList<>();
    private Collection<Junction> junctions = new ArrayList<>();
    private transient Cell cells[][];
}
