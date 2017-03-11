package org.dandj.model;

import com.jme3.math.Vector2f;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

import static java.util.Collections.*;

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
    // Key is a unordered pair of cell
    private Map<Set<Point>, Junction> junctions = new HashMap<>();
    private transient Cell cells[][];
    private Vector2f startPosition = new Vector2f(0, 0);

    public void addJunction(Point a, Point b, Junction junction) {
        junctions().put(unmodifiableSet(new HashSet<Point>() {{
            add(a);
            add(b);
        }}), junction);
    }
}
