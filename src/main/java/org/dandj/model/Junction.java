package org.dandj.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * represents door or gate connecting regions. Cells must be adjacent
 */
@Accessors(fluent = true)
@Data
@ToString
public class Junction {
    Cell from;
    Cell to;
    Direction direction;

    public Junction reverse() {
        return new Junction().from(to).to(from()).direction(direction.reverse());
    }
}
