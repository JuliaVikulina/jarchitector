package org.dandj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
@AllArgsConstructor
public class Cell {

    private final int x;

    private final int y;

    private Region region;
}
