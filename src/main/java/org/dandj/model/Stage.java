package org.dandj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Data
@AllArgsConstructor
public class Stage {
    private final List<Region> regions;
    private int resolution;
    private int width;
    private int height;
}
