package org.dandj.model;

/**
 * Created by daniil on 04.12.16.
 */
public enum CellOrientation {

    BLOCK('█'),
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
