package org.dandj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Data
@EqualsAndHashCode(exclude = "cells")
public class Region {
    private final int id;
    private List<Cell> cells = new ArrayList<>();
}
