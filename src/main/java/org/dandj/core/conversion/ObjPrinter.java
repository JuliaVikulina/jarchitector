package org.dandj.core.conversion;

import org.dandj.core.conversion.obj.ObjFile;
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

        // 2. distribute fragments
    }
}
