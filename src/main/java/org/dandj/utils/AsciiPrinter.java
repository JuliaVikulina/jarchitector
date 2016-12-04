package org.dandj.utils;

import org.dandj.model.Cell;
import org.dandj.model.Stage;

import javax.annotation.Nonnull;

import static org.dandj.model.Direction.*;

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

    static String printStageWithBorders(@Nonnull Stage input) {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : input.cells()) {
            for (Cell cell : row) {
                if (cell != null) {
                    if (cell.next() == null) {
                        sb.append(BLOCK);
                    } else if (cell.direction() == null) {
                        switch (cell.next()) {
                            case UP:
                            case DOWN:
                                sb.append(VERTICAL);
                                break;
                            case LEFT:
                            case RIGHT:
                                sb.append(HORIZONTAL);
                                break;
                        }
                    } else {
                        if (cell.direction() == UP && cell.next() == RIGHT || cell.direction() == LEFT && cell.next() == DOWN)
                            sb.append(CORNER_UR);
                        else if (cell.direction() == RIGHT && cell.next() == DOWN || cell.direction() == UP && cell.next() == LEFT)
                            sb.append(CORNER_RD);
                        else if (cell.direction() == DOWN && cell.next() == RIGHT || cell.direction() == LEFT && cell.next() == UP)
                            sb.append(CORNER_DR);
                        else if (cell.direction() == RIGHT && cell.next() == UP || cell.direction() == DOWN && cell.next() == LEFT)
                            sb.append(CORNER_RU);
                        else if (cell.direction() == RIGHT && cell.next() == RIGHT || cell.direction() == LEFT && cell.next() == LEFT)
                            sb.append(HORIZONTAL);
                        else if (cell.direction() == UP && cell.next() == UP || cell.direction() == DOWN && cell.next() == DOWN)
                            sb.append(VERTICAL);
                        else
                            sb.append('?');
                    }
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();

    }


}
