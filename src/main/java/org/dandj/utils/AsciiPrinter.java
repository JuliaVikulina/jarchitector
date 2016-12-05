package org.dandj.utils;

import org.dandj.model.Cell;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;

public class AsciiPrinter {
    static final String CHARS = "01234567890ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz+/|-=()[]{}~`!@#$%^&*";
    static final String BLOCK = "█";
    static final String HORIZONTAL = "─";
    static final String VERTICAL = "│";
    static final String CORNER_UR = "┌"; // ur, ld
    static final String CORNER_RD = "┐"; // rd, ul
    static final String CORNER_DR = "└"; // dr, lu
    static final String CORNER_RU = "┘"; // ru, dl
    static final String CROSS_VR = "├"; // vr
    static final String CROSS_VL = "┤"; // vl
    static final String CROSS_HD = "┬"; // hd
    static final String CROSS_HU = "┴"; // hu
    static final String CROSS = "┼"; // cross

    static String printStageWithChars(@Nonnull Stage input) {
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

    public static String printStageWithBorders(@Nonnull Stage input) {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : input.cells()) {
            for (Cell cell : row) {
                if (cell != null && cell.orientation() != null) {
                    sb.append(cell.orientation().c);
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();

    }


}
