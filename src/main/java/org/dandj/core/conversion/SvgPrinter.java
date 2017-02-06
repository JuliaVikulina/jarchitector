package org.dandj.core.conversion;

import org.apache.batik.svggen.SVGGraphics2D;
import org.dandj.model.Cell;
import org.dandj.model.Fragment;
import org.dandj.model.Junction;
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

import static org.dandj.model.Fragment.*;

public class SvgPrinter {
    private static Collection<Fragment> TOP_ROW = Arrays.asList(CORNER_UR_INNER, CORNER_UR_OUTER, CORNER_UR_V, CORNER_UR_H, WALL_U, CORNER_UL_INNER, CORNER_UL_OUTER, CORNER_UL_V, CORNER_UL_H);
    private static Collection<Fragment> MIDDLE_ROW = Arrays.asList(WALL_L, FLOOR, WALL_R);
    private static Collection<Fragment> BOTTOM_ROW = Arrays.asList(CORNER_DR_INNER, CORNER_DR_OUTER, CORNER_DR_V, CORNER_DR_H, WALL_D, CORNER_DL_INNER, CORNER_DL_OUTER, CORNER_DL_V, CORNER_DL_H);

    private static Collection<Fragment> LEFT_COLUMN = Arrays.asList(CORNER_UL_H, CORNER_UL_V, CORNER_UL_INNER, CORNER_UL_OUTER, CORNER_DL_H, CORNER_DL_INNER, CORNER_DL_OUTER, CORNER_DL_V, WALL_L);
    private static Collection<Fragment> MIDDLE_COLUMN = Arrays.asList(WALL_U, FLOOR, WALL_D);
    private static Collection<Fragment> RIGHT_COLUMN = Arrays.asList(CORNER_UR_H, CORNER_UR_V, CORNER_UR_INNER, CORNER_UR_OUTER, CORNER_DR_H, CORNER_DR_INNER, CORNER_DR_OUTER, CORNER_DR_V, WALL_R);

    private static Collection<Fragment> TALL = Arrays.asList(WALL_L, WALL_R, FLOOR);
    private static Collection<Fragment> WIDE = Arrays.asList(WALL_U, WALL_D, FLOOR);
    private static Collection<Fragment> CORNERS = Arrays.asList(CORNER_UR_INNER, CORNER_UR_OUTER, CORNER_DR_INNER, CORNER_DR_OUTER, CORNER_UL_INNER, CORNER_UL_OUTER, CORNER_DL_INNER, CORNER_DL_OUTER);

    private static Color WALL_COLOR = Color.decode("#494E6B");
    private static Color FLOOR_COLOR = Color.decode("#98878F");
    private static Color GRID_COLOR = Color.decode("#985e6d");
    private static Color FONT_COLOR = Color.decode("#192231");
    private static Color DOOR_COLOR = Color.decode("#277455");

    public static void printStageAsSvg(Stage stage) {
        try {
            SVGGraphics2D svg = createSvg();
            drawGrid(svg, stage.width(), stage.height(), stage.resolution());
            stage.junctions().forEach(junction -> drawJunction(svg, junction, stage.resolution()));
            stage.regions().forEach(region ->
                    region.cells().forEach(cell ->
                            cell.fragments().forEach(fragment ->
                                    drawFragment(svg, fragment, cell.x(), cell.y(), stage.resolution()))));
            svg.stream(new FileWriter("target/" + stage.name() + ".svg"), true);
        } catch (IOException | ParserConfigurationException e) {
            System.out.println("OH MY GOOOOOD" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void drawJunction(SVGGraphics2D svg, Junction junction, int r) {
        svg.setColor(DOOR_COLOR);
        if (junction.from().x() == junction.to().x()) { // vertical case
            int x = junction.from().x() * r;
            int y = Math.max(junction.from().y(), junction.to().y()) * r - r / 8;
            svg.fillRect(x, y, r, r / 4);
        } else if (junction.from().y() == junction.to().y()) { // horizontal case
            int y = junction.from().y() * r;
            int x = Math.max(junction.from().x(), junction.to().x()) * r - r / 8;
            svg.fillRect(x, y, r / 4, r);
        } else throw new IllegalStateException("Junction with non adjacent tiles");
    }

    public static void printCellsAsSvg(Stage stage) throws IOException, ParserConfigurationException {
        SVGGraphics2D svg = createSvg();
        drawGrid(svg, stage.width(), stage.height(), stage.resolution());
        for (Cell[] row : stage.cells())
            for (Cell cell : row)
                if (cell != null)
                    cell.fragments().forEach(fragment -> drawFragment(svg, fragment, cell.x(), cell.y(), stage.resolution()));
        svg.stream(new FileWriter("cells.svg"), true);
    }

    private static void drawGrid(SVGGraphics2D svg, int width, int height, int r) {
        for (int y = 0; y < height + 1; y++) {
            svg.setColor(GRID_COLOR);
            if (y % 10 == 0) {
                svg.setColor(FONT_COLOR);
                svg.drawString("" + y, width * r, y * r);
            }
            svg.drawLine(0, y * r, width * r, y * r);
        }
        for (int x = 0; x < width + 1; x++) {
            svg.setColor(GRID_COLOR);
            if (x % 10 == 0) {
                svg.setColor(FONT_COLOR);
                svg.drawString("" + x, x * r, height * r);
            }
            svg.drawLine(x * r, 0, x * r, height * r);
        }
    }

    private static void drawFragment(SVGGraphics2D svg, Fragment f, int cellX, int cellY, int r) {
        int x = 0;
        int y = 0;
        int small = r / 4;
        int big = r / 2;
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
            svg.setColor(FLOOR_COLOR);
        else
            svg.setColor(WALL_COLOR);
        if (CORNERS.contains(f)) {
            int[] polyX = new int[3];
            int[] polyY = new int[3];
            if (f == CORNER_UL_INNER || f == CORNER_DR_OUTER) {
                polyX[0] = cellX * r + x;
                polyX[1] = cellX * r + x + width;
                polyX[2] = cellX * r + x + width;
                polyY[0] = cellY * r + y + height;
                polyY[1] = cellY * r + y + height;
                polyY[2] = cellY * r + y;
            } else if (f == CORNER_UR_INNER || f == CORNER_DL_OUTER) {
                polyX[0] = cellX * r + x;
                polyX[1] = cellX * r + x;
                polyX[2] = cellX * r + x + width;
                polyY[0] = cellY * r + y;
                polyY[1] = cellY * r + y + height;
                polyY[2] = cellY * r + y + height;
            } else if (f == CORNER_DR_INNER || f == CORNER_UL_OUTER) {
                polyX[0] = cellX * r + x;
                polyX[1] = cellX * r + x;
                polyX[2] = cellX * r + x + width;
                polyY[0] = cellY * r + y;
                polyY[1] = cellY * r + y + height;
                polyY[2] = cellY * r + y;
            } else if (f == CORNER_DL_INNER || f == CORNER_UR_OUTER) {
                polyX[0] = cellX * r + x;
                polyX[1] = cellX * r + x + width;
                polyX[2] = cellX * r + x + width;
                polyY[0] = cellY * r + y;
                polyY[1] = cellY * r + y + height;
                polyY[2] = cellY * r + y;
            }

            svg.fillPolygon(polyX, polyY, 3);
        } else {
            svg.fill(new Rectangle(
                    cellX * r + x,
                    cellY * r + y,
                    width,
                    height));
        }
    }

    private static SVGGraphics2D createSvg() throws ParserConfigurationException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = f.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        return new SVGGraphics2D(document);
    }
}
