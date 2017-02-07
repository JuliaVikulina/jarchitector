package org.dandj.core.conversion;

import org.dandj.core.conversion.obj.ObjFile;
import org.dandj.model.Cell;
import org.dandj.model.Fragment;
import org.dandj.model.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by daniil on 06.02.17.
 */
public class ObjPrinter {
    public static void printAsObj(Stage stage) throws IOException {
        // 1. load tileset(s)
        ObjFile tileSet = new ObjFile(new File("simple-wall-set2.obj"));
        ObjFile result = new ObjFile();
        result.setMtllib(tileSet.getMtllib());
        stage.regions().forEach(region ->
                region.cells().forEach(cell ->
                        cell.fragments().forEach(fragment ->
                                drawFragment(fragment, cell, result, tileSet)
                        )));
    }

    private static void drawFragment(Fragment fragment, Cell cell, ObjFile result, ObjFile tileSet) {

    }
}
