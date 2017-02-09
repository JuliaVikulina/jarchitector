package org.dandj.core.conversion;

import org.dandj.core.conversion.obj.ObjFile;
import org.dandj.core.conversion.obj.TileSetManager;
import org.dandj.model.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by daniil on 06.02.17.
 */
public class ObjPrinter {
    public static void printAsObj(Stage stage) throws IOException {
        ObjFile tileSet = new ObjFile(new File("cubes.obj"));
        TileSetManager tileSetManager = new TileSetManager();
        tileSetManager.addTileSet(tileSet);
        ObjFile result = new ObjFile();
        result.setMtllib(tileSet.getMtllib());
        stage.regions().forEach(region ->
                region.cells().forEach(cell ->
                        cell.fragments().forEach(fragment ->
                                result.getObjects().add(tileSetManager.createFragment(fragment, cell.x(), cell.y()))
                        )
                )
        );
        result.serialize(new PrintWriter(new FileWriter("result.obj")));
    }
}
