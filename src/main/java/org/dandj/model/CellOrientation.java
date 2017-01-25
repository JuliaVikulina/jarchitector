package org.dandj.model;

/**
 * Created by daniil on 04.12.16.
 */
public enum CellOrientation {

    CELL_UL('┌'), // upper left corner
    CELL_UR('┐'), // upper right corner
    CELL_UM('─'), // upper middle
    CELL_BM('─'), // bottom middle
    CELL_LM('│'), // upper middle
    CELL_RM('│'), // bottom middle
    CELL_BL('└'), // bottom left corner
    CELL_BR('┘'), // bottom right corner
    CELL_FL('░'), // floor
    HORIZONTAL('─'),
    VERTICAL('│'),
    CORNER_UR('┌'), // ur, ld
    CORNER_RD('┐'), // rd, ul
    CORNER_DR('└'), // dr, lu
    CORNER_RU('┘'), // ru, dl
    CROSS_VR('├'), // vr
    CROSS_VL('┤'), // vl
    CROSS_HD('┬'), // hd
    CROSS_HU('┴'), // hu
    CROSS('┼'); // cross
    public final char c;

    CellOrientation(char c) {
        this.c = c;
    }
}
