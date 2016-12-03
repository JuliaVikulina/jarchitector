package org.dandj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Data
@AllArgsConstructor
public class Region {
    private final List<Cell> cells;
}
