package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static org.dandj.core.conversion.obj.ObjConstants.*;
import static org.dandj.core.conversion.obj.ObjFile.strip;

@Data
public class ObjMaterialLibrary {

    private String name;
    private Map<String, ObjMaterial> materials = new HashMap<>();
    private List<String> comment = new ArrayList<>();

    public ObjMaterialLibrary(String name) {
        this.name = name;
    }

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
            else if (line.startsWith(BUMP_MAP))
                currentMaterial.setBumpMap(new File(new File(absolutePath), strip(line, BUMP_MAP)));
            else if (line.startsWith(AMBIENT_COLOR))
                currentMaterial.setAmbientColor(new Vertex3d(strip(line, AMBIENT_COLOR)));
            else if (line.startsWith(SPECULAR_EXP))
                currentMaterial.setSpecularExponent(parseDouble(strip(line, SPECULAR_EXP)));
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

    public void serialize(File folderName) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(folderName, name)))) {
            materials.values().forEach(m ->
                    m.serialize(out, folderName)
            );
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
