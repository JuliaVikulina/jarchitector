package org.dandj.utils;

import org.apache.batik.svggen.SVGGraphics2D;
import org.dandj.model.Cell;
import org.dandj.model.Fragment;
import org.dandj.model.Stage;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static java.awt.Color.*;
import static org.dandj.model.Fragment.*;

public class SvgPrinter {
    static Collection<Fragment> TOP_ROW = Arrays.asList(CORNER_UR_INNER, CORNER_UR_OUTER, CORNER_UR_V, CORNER_UR_H, WALL_U, CORNER_UL_INNER, CORNER_UL_OUTER, CORNER_UL_V, CORNER_UL_H);
    static Collection<Fragment> MIDDLE_ROW = Arrays.asList(WALL_L, FLOOR, WALL_R);
    static Collection<Fragment> BOTTOM_ROW = Arrays.asList(CORNER_DR_INNER, CORNER_DR_OUTER, CORNER_DR_V, CORNER_DR_H, WALL_D, CORNER_DL_INNER, CORNER_DL_OUTER, CORNER_DL_V, CORNER_DL_H);

    static Collection<Fragment> LEFT_COLUMN = Arrays.asList(CORNER_UL_H, CORNER_UL_V, CORNER_UL_INNER, CORNER_UL_OUTER, CORNER_DL_H, CORNER_DL_INNER, CORNER_DL_OUTER, CORNER_DL_V, WALL_L);
    static Collection<Fragment> MIDDLE_COLUMN = Arrays.asList(WALL_U, FLOOR, WALL_D);
    static Collection<Fragment> RIGHT_COLUMN = Arrays.asList(CORNER_UR_H, CORNER_UR_V, CORNER_UR_INNER, CORNER_UR_OUTER, CORNER_DR_H, CORNER_DR_INNER, CORNER_DR_OUTER, CORNER_DR_V, WALL_R);

    static Collection<Fragment> TALL = Arrays.asList(WALL_L, WALL_R, FLOOR);
    static Collection<Fragment> WIDE = Arrays.asList(WALL_U, WALL_D, FLOOR);

    public static void printAsSvg(Stage stage) throws IOException, ParserConfigurationException {
        SVGGraphics2D svgGenerator = createSvg();

        svgGenerator.setPaint(red);
        for (Cell[] row : stage.cells()) {
            for (Cell cell : row) {
                if (cell != null)
                    cell.fragments().forEach(fragment -> drawFragment(svgGenerator, fragment, cell.x(), cell.y(), stage.resolution()));
            }
        }
        svgGenerator.stream(new FileWriter("stage.svg"), true);
    }

    private static void drawFragment(SVGGraphics2D svg, Fragment f, int cellX, int cellY, int resolution) {
        int x = 0;
        int y = 0;
        int small = resolution / 4;
        int big = resolution / 2;
        int width = small;
        int height = small;
        if (TOP_ROW.contains(f))
            y = 0;
        if (MIDDLE_ROW.contains(f))
            y = small;
        if (BOTTOM_ROW.contains(f))
            y = small * 3;
        if (LEFT_COLUMN.contains(f))
            x = 0;
        if (MIDDLE_COLUMN.contains(f))
            x = small;
        if (RIGHT_COLUMN.contains(f))
            x = small * 3;
        if (TALL.contains(f))
            height = big;
        if (WIDE.contains(f))
            width = big;
        if (f == FLOOR)
            svg.setColor(PINK);
        else
            svg.setColor(BLUE);
        svg.fill(new Rectangle(
                cellX * resolution + x,
                cellY * resolution + y,
                width,
                height));
    }

    private static SVGGraphics2D createSvg() throws ParserConfigurationException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = f.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        return new SVGGraphics2D(document);
    }
}
