package org.dandj.utils;

import org.dandj.model.Cell;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;

public class AsciiPrinter {
    public static String printStage(@Nonnull Stage input) {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : input.cells()) {
            for (Cell cell : row) {
                if (cell != null)
                    sb.append(cell.region().id());
                else
                    sb.append('.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
