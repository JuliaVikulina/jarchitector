package org.dandj.core.conversion;

import org.dandj.core.conversion.obj.ObjFile;
import org.dandj.core.conversion.obj.ObjGeometry;
import org.dandj.core.conversion.obj.ObjImportExport;
import org.dandj.core.conversion.obj.TileSetManager;
import org.dandj.model.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toSet;

/**
 * Created by daniil on 06.02.17.
 */
public class ObjPrinter {
    public static void printAsObj(Stage stage, File destFolder) throws IOException {
        TileSetManager tileSetManager = new TileSetManager();
        tileSetManager.addTileSet(ObjImportExport.parseObj(new File("tiles/qtile-tech-4/qtile-tech-4.obj")), "maze", 0.1f);
        tileSetManager.addTileSet(ObjImportExport.parseObj(new File("tiles/qtile-tech-2/qtile-tech-2.obj")), "room", 0.1f);
        ObjFile result = new ObjFile(stage.name());
        destFolder.mkdirs();
        stage.regions().forEach(region ->
                region.cells().forEach(cell ->
                        cell.getFragments().forEach(fragment ->
                                result.getObjects().add(tileSetManager.createFragment(stage.name(), region.style(), fragment, cell.getX(), cell.getZ()))
                        )
                )
        );
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(destFolder, stage.name() + ".mtl")))) {
            (result.getObjects().stream().map(ObjGeometry::getMaterial).collect(toSet())).forEach(
                    objMaterial -> ObjImportExport.serializeMaterial(objMaterial, out, destFolder)
            );
        }
        if (stage.mergeObjects()) {
            mergeBasedOnMaterials(result);
        }
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(destFolder, stage.name() + ".obj")))) {
            ObjImportExport.serializeObjfile(result, out);
        }
    }

    private static void mergeBasedOnMaterials(ObjFile result) {
        Collection<ObjGeometry> geometries = result.getObjects();
        Map<String, ObjGeometry> addedMaterials = new HashMap<>();
        geometries.forEach(geom -> {
                    if (!addedMaterials.containsKey(geom.getMaterial().getName())) {
                        addedMaterials.put(geom.getMaterial().getName(), geom);
                    } else {
                        ObjGeometry existing = addedMaterials.get(geom.getMaterial().getName());
                        existing.getFaces().addAll(geom.getFaces());
                    }
                }
        );
        result.setObjects(new ArrayList<>(addedMaterials.values()));
    }
}
