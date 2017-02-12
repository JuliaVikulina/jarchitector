package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static org.dandj.core.conversion.obj.ObjFile.strip;

@Data
public class ObjMaterialLibrary {
    private static final String COMMENT = "#";
    private static final String NEWMAT = "newmtl ";
    private static final String AMBIENT_COLOR = "Ka ";
    private static final String DIFFUSE_COLOR = "Kd ";
    private static final String DIFFUSE_MAP = "map_Kd ";
    private static final String SPECULAR_COLOR = "Ks ";
    private static final String EMISSION_COLOR = "Ke ";
    private static final String SPECULAR_EXP = "Ns ";
    private static final String OPTICAL_DENSITY = "Ni ";
    private static final String DISSOLVE = "d ";

    /*
        0. Color on and Ambient off
        1. Color on and Ambient on
        2. Highlight on
        3. Reflection on and Ray trace on
        4. Transparency: Glass on, Reflection: Ray trace on
        5. Reflection: Fresnel on and Ray trace on
        6. Transparency: Refraction on, Reflection: Fresnel off and Ray trace on
        7. Transparency: Refraction on, Reflection: Fresnel on and Ray trace on
        8. Reflection on and Ray trace off
        9. Transparency: Glass on, Reflection: Ray trace off
        10. Casts shadows onto invisible surfaces
     */
    private static final String ILLUM = "illum ";

    private String name;
    private Map<String, ObjMaterial> materials = new HashMap<>();
    private List<String> comment = new ArrayList<>();

    public ObjMaterialLibrary(String name, String absolutePath) throws IOException {
        ObjMaterial currentMaterial = null;
        this.name = name;
        BufferedReader reader = new BufferedReader(new FileReader(new File(absolutePath, name)));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            if (line.startsWith(COMMENT))
                comment.add(line);
            else if (line.startsWith(NEWMAT)) {
                currentMaterial = new ObjMaterial(strip(line, NEWMAT));
                materials.put(currentMaterial.getName(), currentMaterial);
            } else if (currentMaterial == null)
                continue;
            else if (line.startsWith(SPECULAR_COLOR))
                currentMaterial.setSpecularColor(new Vertex3d(strip(line, SPECULAR_COLOR)));
            else if (line.startsWith(DIFFUSE_COLOR))
                currentMaterial.setDiffuseColor(new Vertex3d(strip(line, DIFFUSE_COLOR)));
            else if (line.startsWith(DIFFUSE_MAP))
                currentMaterial.setDiffuseMap(new File(new File(absolutePath), strip(line, DIFFUSE_MAP)));
            else if (line.startsWith(AMBIENT_COLOR))
                currentMaterial.setAmbientColor(new Vertex3d(strip(line, AMBIENT_COLOR)));
            else if (line.startsWith(SPECULAR_EXP))
                currentMaterial.setSpecular(parseDouble(strip(line, SPECULAR_EXP)));
            else if (line.startsWith(EMISSION_COLOR))
                currentMaterial.setEmissionColor(new Vertex3d(strip(line, EMISSION_COLOR)));
            else if (line.startsWith(DISSOLVE))
                currentMaterial.setDissolve(parseDouble(strip(line, DISSOLVE)));
            else if (line.startsWith(OPTICAL_DENSITY))
                currentMaterial.setOpticalDensity(parseDouble(strip(line, OPTICAL_DENSITY)));
            else if (line.startsWith(ILLUM))
                currentMaterial.setIlluminationMode(Integer.parseInt(strip(line, ILLUM)));
            else System.err.println("Not recognized command:\n" + line);

        }
    }

    @Override
    public String toString() {
        return name;
    }
}
