package org.dandj.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
public class Stage {
    private List<Region> regions = new ArrayList<>();
    private int resolution;
    private int width;
    private int height;
}
