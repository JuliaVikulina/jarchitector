package org.dandj.utils;

import org.dandj.core.generation.StageGenerator;
import org.dandj.model.Stage;
import org.junit.Test;

import java.util.Random;

/**
 * Created by daniil on 26.11.16.
 */
public class AsciiPrinterTest {

    @Test
    public void printStageWithBorders() throws Exception {
        System.out.println(
                AsciiPrinter.printStageWithBorders(
                        StageGenerator.createStage(
                                new Stage().width(80).height(25)
                                        .roomTries(5)
                                        .mazeStraightness(0.4f), new Random(42))));
    }
}