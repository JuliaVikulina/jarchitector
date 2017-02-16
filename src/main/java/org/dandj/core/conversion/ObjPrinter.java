package org.dandj.core.conversion;

import org.dandj.core.conversion.obj.ObjFile;
import org.dandj.core.conversion.obj.ObjGeometry;
import org.dandj.core.conversion.obj.TileSetManager;
import org.dandj.model.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import static java.util.stream.Collectors.toList;

/**
 * Created by daniil on 06.02.17.
 */
public class ObjPrinter {
    public static void printAsObj(Stage stage, File destFolder) throws IOException {
        TileSetManager tileSetManager = new TileSetManager();
        tileSetManager.addTileSet(new ObjFile(new File("tiles/qtile-tech-4/qtile-tech-4.obj")), "room", 0.1d);
        tileSetManager.addTileSet(new ObjFile(new File("tiles/qtile-tech-2/qtile-tech-2.obj")), "maze", 0.1d);
        ObjFile result = new ObjFile(stage.name());
        destFolder.mkdir();
        stage.regions().forEach(region ->
                region.cells().forEach(cell ->
                        cell.fragments().forEach(fragment ->
                                result.getObjects().add(tileSetManager.createFragment(region.style(), fragment, cell.x(), cell.y()))
                        )
                )
        );
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(destFolder, stage.name() + ".mtl")))) {
            new HashSet<>(result.getObjects().stream().map(ObjGeometry::getMaterial).collect(toList())).forEach(
                    objMaterial -> objMaterial.serialize(out, destFolder)
            );
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(new File(destFolder, stage.name() + ".obj")))) {
            result.serialize(out);
        }
    }
}
