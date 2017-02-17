package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import static org.dandj.core.conversion.obj.ObjConstants.*;

@Data
public class ObjMaterial {
    private String name = "default";
    private Double specularExponent = 100d;
    private Double opticalDensity = 1d;
    private Double dissolve = 1d;
    private Integer illuminationMode = 2;
    private Vertex3d ambientColor = new Vertex3d(1, 1, 1);
    private Vertex3d diffuseColor = new Vertex3d(0.64, 0.64, 0.64);
    private Vertex3d specularColor = new Vertex3d(0.5, 0.5, 0.5);
    private Vertex3d emissionColor = new Vertex3d(0, 0, 0);
    private File diffuseMap;
    private File bumpMap;

    public ObjMaterial(String name) {
        this.name = name;
    }

    public void serialize(PrintWriter out, File folderName) {
        out.println(NEWMAT + name);
        out.println(String.format("%s%f", SPECULAR_EXP, specularExponent));
        out.println(AMBIENT_COLOR + ambientColor);
        out.println(DIFFUSE_COLOR + diffuseColor);
        out.println(SPECULAR_COLOR + specularColor);
        out.println(EMISSION_COLOR + emissionColor);
        out.println(String.format("%s%f", OPTICAL_DENSITY, opticalDensity));
        out.println(String.format("%s%f", DISSOLVE, dissolve));
        out.println(String.format("%s%d", ILLUM, illuminationMode));
        if (diffuseMap != null && !new File(folderName, diffuseMap.getName()).exists()) {
            out.println(DIFFUSE_MAP + diffuseMap.getName());
            try (FileOutputStream f = new FileOutputStream(new File(folderName, diffuseMap.getName()))) {
                Files.copy(diffuseMap.toPath(), f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bumpMap != null && !new File(folderName, bumpMap.getName()).exists()) {
            out.println(BUMP_MAP + bumpMap.getName());
            try (FileOutputStream f = new FileOutputStream(new File(folderName, bumpMap.getName()))) {
                Files.copy(bumpMap.toPath(), f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.println();
    }
}
