package org.dandj.utils;

import static org.dandj.api.API.Region;
import static org.dandj.api.API.StageOrBuilder;
import static org.dandj.api.API.Cell;

public class AsciiPrinter {
    public static String printStage(StageOrBuilder input) {
        int xrange = input.getWidth();
        int yrange = input.getHeight();
        int[][] stage = new int[yrange][];
        for (int i = 0; i < yrange; i++) {
            stage[i] = new int[xrange];
        }

        input.getRegionsList().forEach((Region r) -> {
            r.getCellsList().forEach((Cell c) -> {
                stage[c.getY()][c.getX()] = 1;
            });
        });
        StringBuilder result = new StringBuilder();
        for (int[] row : stage) {
            for (int cell : row) {
                result.append(cell == 0 ? '.' : '#');
            }
            result.append('\n');
        }
        return result.toString();
    }
}
