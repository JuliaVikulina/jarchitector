package org.dandj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Accessors(fluent = true)
@Data
@EqualsAndHashCode(exclude = "cells")
public class Region {
    private final UUID id = UUID.randomUUID();
    private List<Cell> cells = new ArrayList<>();
}
