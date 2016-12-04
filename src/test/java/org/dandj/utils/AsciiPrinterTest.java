package org.dandj.utils;

import org.dandj.core.generation.StageGenerator;
import org.dandj.model.Stage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by daniil on 26.11.16.
 */
public class AsciiPrinterTest {
    @Test
    public void printStageWithChars() throws Exception {
        System.out.println(
                AsciiPrinter.printStageWithChars(
                        StageGenerator.createStage(
                                new Stage().width(80).height(25).roomTries(20).mazeStraightness(0.3f), new Random(42))));
    }

    @Test
    public void printStageWithBorders() throws Exception {
        System.out.println(
                AsciiPrinter.printStageWithBorders(
                        StageGenerator.createStage(
                                new Stage().width(80).height(25)
                                        .roomTries(15)
//                                        .roomSizeXMin(2).roomSizeXMax(2)
//                                        .roomSizeYMin(2).roomSizeYMax(2)
                                        .mazeStraightness(0.3f), new Random(42))));
    }

    @Test
    public void testChars() {
        Assert.assertEquals('0', AsciiPrinter.idToChar(0));
        Assert.assertEquals('A', AsciiPrinter.idToChar(11));
        Assert.assertEquals('1', AsciiPrinter.idToChar(1 + AsciiPrinter.CHARS.length()));

    }
}