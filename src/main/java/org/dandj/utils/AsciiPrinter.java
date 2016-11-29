package org.dandj.utils;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

import static org.dandj.api.API.Region;
import static org.dandj.api.API.StageOrBuilder;
import static org.dandj.api.API.Cell;

public class AsciiPrinter {
    public static String printStage(@Nonnull StageOrBuilder input) {
        int xrange = input.getWidth();
        int yrange = input.getHeight();
        char[][] stage = new char[yrange][];
        for (int i = 0; i < yrange; i++) {
            stage[i] = new char[xrange];
            for (int j = 0; j < stage[i].length; j++) {
                stage[i][j] = '.';
            }
        }

        final AtomicInteger ai = new AtomicInteger();
        input.getRegionsList().forEach((Region r) -> {
            int digit = ai.incrementAndGet();
            r.getCellsList().forEach((Cell c) -> {
                stage[c.getY()][c.getX()] = Character.forDigit(digit, 36);

            });
        });
        StringBuilder result = new StringBuilder();
        for (char[] row : stage) {
            for (char cell : row) {
                result.append(cell);
            }
            result.append('\n');
        }
        return result.toString();
    }
}
