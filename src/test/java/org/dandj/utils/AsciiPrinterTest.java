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
    public void printStage() throws Exception {
        System.out.println(
                AsciiPrinter.printStage(
                        StageGenerator.createStage(
                                new Stage().width(80).height(25).mazeStraightness(1f), new Random(42))));
    }

    @Test
    public void testChars() {
        Assert.assertEquals('0', AsciiPrinter.idToChar(0));
        Assert.assertEquals('A', AsciiPrinter.idToChar(11));
        Assert.assertEquals('1', AsciiPrinter.idToChar(1 + AsciiPrinter.CHARS.length()));

    }
}