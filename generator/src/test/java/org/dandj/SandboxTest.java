package org.dandj;

import org.dandj.core.conversion.ObjPrinter;
import org.dandj.core.conversion.SvgPrinter;
import org.dandj.core.generation.StageGenerator;
import org.dandj.model.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class SandboxTest {
    public static void main(String[] args) throws IOException {
        Stage stage = new Stage().name("SandboxTest2").height(32).width(32).resolution(16)
                .roomTries(32).roomSizeXMin(4).roomSizeXMax(16).roomSizeZMin(4).roomSizeZMax(16)
                .mergeObjects(true)
                .mazeStraightness(0.7f);
        StageGenerator.createStage(stage, new Random(42));
        ObjPrinter.printAsObj(stage, new File("build/levels"));
        SvgPrinter.printStageAsSvg(stage);
    }
}
