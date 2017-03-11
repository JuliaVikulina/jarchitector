package org.dandj.core.conversion;

import com.jme3.math.FastMath;
import lombok.SneakyThrows;
import org.dandj.core.conversion.obj.ObjFile;
import org.dandj.core.conversion.obj.ObjGeometry;
import org.dandj.core.conversion.obj.ObjImportExport;
import org.dandj.core.conversion.obj.TileSetManager;
import org.dandj.model.Stage;
import org.yaml.snakeyaml.Yaml;

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
        writeStartPoint(stage, destFolder);
        TileSetManager tileSetManager = new TileSetManager();
        tileSetManager.addTileSet(ObjImportExport.parseObj(new File("tiles/qtile-tech-4/qtile-tech-4.obj")), "room", 0.1f);
        tileSetManager.addTileSet(ObjImportExport.parseObj(new File("tiles/qtile-tech-2/qtile-tech-2.obj")), "maze", 0.1f);
        tileSetManager.addJunction(ObjImportExport.parseObj(new File("tiles/qdoor-tech1/qdoor-tech1.obj")));
        ObjFile result = new ObjFile(stage.name());
        destFolder.mkdirs();
        stage.regions().forEach(region ->
                region.cells().forEach(cell ->
                        cell.getFragments().forEach(fragment ->
                                result.getObjects().add(tileSetManager.createFragment(stage.name(), region.style(), fragment, cell.getX(), cell.getZ()))
                        )
                )
        );
        stage.junctions().forEach(junction -> {
            float x;
            float y;
            float angle = FastMath.HALF_PI; // by default, doors are aligned by z axis
            if (junction.from().getX() == junction.to().getX()) { // vertical case
                x = junction.from().getX() + 0.5f;
                y = Math.max(junction.from().getZ(), junction.to().getZ());
            } else if (junction.from().getZ() == junction.to().getZ()) { // horizontal case
                y = junction.from().getZ() + 0.5f;
                x = Math.max(junction.from().getX(), junction.to().getX());
                angle = 0;
            } else throw new IllegalStateException("Junction with non adjacent tiles");
            result.getObjects().add(tileSetManager.createJunction(x, y, angle));
        });
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

    @SneakyThrows
    public static void writeStartPoint(Stage stage, File destFolder) {
        Map<String, Object> data = new HashMap<>();
        data.put("start", stage.startPosition());

        Yaml yaml = new Yaml();
        FileWriter writer = new FileWriter(new File(destFolder, stage.name() + ".yml"));
        yaml.dump(data, writer);
        writer.close();
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
