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
    public void printStage() throws Exception {
        System.out.println(
                AsciiPrinter.printStage(
                        StageGenerator.createStage(new Stage().width(80).height(25), new Random())));
    }

}