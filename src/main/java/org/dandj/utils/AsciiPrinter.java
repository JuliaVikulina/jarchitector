package org.dandj.utils;

import org.dandj.model.Cell;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;

public class AsciiPrinter {
    static final String CHARS = "01234567890ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz+/|-=()[]{}~`!@#$%^&*";
    public static String printStage(@Nonnull Stage input) {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : input.cells()) {
            for (Cell cell : row) {
                if (cell != null)
                    sb.append(idToChar(cell.region().id()));
                else
                    sb.append('.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    static char idToChar(int id) {
        return CHARS.charAt(id % CHARS.length());
    }
}
