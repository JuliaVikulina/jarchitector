package org.dandj.core.conversion.obj;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static org.dandj.core.conversion.obj.ObjConstants.*;

public class ObjImportExport {
    private ObjImportExport() {
    }

    static Vector2f vector2fromString(String line) {
        String[] split = line.split(" ");
        return new Vector2f(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
    }

    static Vector3f vector3fromString(String line) {
        String[] split = line.split(" ");
        return new Vector3f(
                Float.parseFloat(split[0]),
                Float.parseFloat(split[1]),
                Float.parseFloat(split[2])
        );
    }

    static ColorRGBA colorRGBAfromString(String line) {
        String[] split = line.split(" ");
        return new ColorRGBA(
                Float.parseFloat(split[0]),
                Float.parseFloat(split[1]),
                Float.parseFloat(split[2]),
                0
        );
    }

    static void serializeVector3(Vector3f v, PrintWriter out, String type) {
        out.println(String.format("%s%f %f %f", type, v.getX(), v.getY(), v.getZ()));
    }

    static void serializeVector2(Vector2f v, PrintWriter out, String type) {
        out.println(String.format("%s%f %f", type, v.getX(), v.getY()));
    }

    static void serializeGeometry(ObjGeometry g, PrintWriter out, IndexOffset offset) {
        if (g.getFaces().isEmpty())
            return;
        out.println(OBJECT + g.getName());
        StringWriter buff = new StringWriter();
        PrintWriter faceBuffer = new PrintWriter(buff);
        faceBuffer.println(MATERIAL + g.getMaterial().getName());
        faceBuffer.println(SMOOTH + "off"); //todo implement smooth groups
        g.getFaces().forEach(f -> {
            faceBuffer.print("f");
            f.getNodes().forEach(n -> {
                serializeVector3(n.getVertex(), out, "v ");
                offset.vertex++;
                faceBuffer.print(" " + offset.vertex + "/");
                if (n.getUv() != null) {
                    serializeVector2(n.getUv(), out, "vt ");
                    offset.uv++;
                    faceBuffer.print(offset.uv);
                }
                faceBuffer.print("/");
                if (n.getNormal() != null) {
                    serializeVector3(n.getVertex(), out, "vn ");
                    offset.normal++;
                    faceBuffer.print(offset.normal);
                }
                faceBuffer.print(" ");
            });
            faceBuffer.println();
        });
    }

    public static void serializeObjfile(ObjFile objFile, PrintWriter out) throws IOException {
        objFile.getComment().forEach(out::println);
        out.println(MTLLIB + objFile.getMtllib());
        final IndexOffset offset = new IndexOffset();
        objFile.getObjects().forEach(o -> serializeGeometry(o, out, offset));
    }

    public static ObjFile parseObj(File file) throws IOException {
        ObjGeometry currentObject = null;
        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector2f> texCoords = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ObjFile result = new ObjFile();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            if (line.trim().startsWith(COMMENT))
                result.getComment().add(line);
            else if (line.startsWith(MTLLIB))
                result.setMtllib(parseMatLib(strip(line, MTLLIB), file.getParent()));
            else if (line.startsWith(OBJECT)) {
                currentObject = new ObjGeometry(strip(line, OBJECT));
                result.getObjects().add(currentObject);
            } else if (line.startsWith(VERTEX))
                vertices.add(vector3fromString(strip(line, VERTEX)));
            else if (line.startsWith(TEXCOORD))
                texCoords.add(vector2fromString(strip(line, TEXCOORD)));
            else if (line.startsWith(NORMAL))
                normals.add(vector3fromString(strip(line, NORMAL)));
            else if (line.startsWith(MATERIAL) && currentObject != null)
                currentObject.setMaterial(result.getMtllib().getMaterials().get(strip(line, MATERIAL)));
            else if (line.startsWith(SMOOTH) && currentObject != null)
                currentObject.setSmoothGroup(strip(line, SMOOTH));
            else if (line.startsWith(FACE) && currentObject != null)
                currentObject.addFace(strip(line, FACE), vertices, texCoords, normals);
            else System.err.println("Not recognized command:\n" + line);
        }
        return result;
    }

    public static String strip(String line, String token) {
        if (line != null && token != null && line.contains(token))
            return line.substring(line.indexOf(token) + token.length(), line.length());
        return null;
    }

    static ObjMaterialLibrary parseMatLib(String name, String absolutePath) throws IOException {
        ObjMaterialLibrary result = new ObjMaterialLibrary(name);
        ObjMaterial currentMaterial = null;
        BufferedReader reader = new BufferedReader(new FileReader(new File(absolutePath, name)));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            if (line.startsWith(COMMENT))
                result.getComment().add(line);
            else if (line.startsWith(NEWMAT)) {
                currentMaterial = new ObjMaterial(strip(line, NEWMAT));
                result.getMaterials().put(currentMaterial.getName(), currentMaterial);
            } else if (currentMaterial == null)
                continue;
            else if (line.startsWith(SPECULAR_COLOR))
                currentMaterial.setSpecularColor(colorRGBAfromString(strip(line, SPECULAR_COLOR)));
            else if (line.startsWith(DIFFUSE_COLOR))
                currentMaterial.setDiffuseColor(colorRGBAfromString(strip(line, DIFFUSE_COLOR)));
            else if (line.startsWith(DIFFUSE_MAP))
                currentMaterial.setDiffuseMap(new File(new File(absolutePath), strip(line, DIFFUSE_MAP)));
            else if (line.startsWith(BUMP_MAP))
                currentMaterial.setBumpMap(new File(new File(absolutePath), strip(line, BUMP_MAP)));
            else if (line.startsWith(AMBIENT_COLOR))
                currentMaterial.setAmbientColor(colorRGBAfromString(strip(line, AMBIENT_COLOR)));
            else if (line.startsWith(SPECULAR_EXP))
                currentMaterial.setSpecularExponent(parseDouble(strip(line, SPECULAR_EXP)));
            else if (line.startsWith(EMISSION_COLOR))
                currentMaterial.setEmissionColor(colorRGBAfromString(strip(line, EMISSION_COLOR)));
            else if (line.startsWith(DISSOLVE))
                currentMaterial.setDissolve(parseDouble(strip(line, DISSOLVE)));
            else if (line.startsWith(OPTICAL_DENSITY))
                currentMaterial.setOpticalDensity(parseDouble(strip(line, OPTICAL_DENSITY)));
            else if (line.startsWith(ILLUM))
                currentMaterial.setIlluminationMode(Integer.parseInt(strip(line, ILLUM)));
            else System.err.println("Not recognized command:\n" + line);
        }
        return result;
    }

    public static void serializeMatLib(ObjMaterialLibrary lib, File folderName) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(new File(folderName, lib.getName())))) {
            lib.getMaterials().values().forEach(m ->
                    serializeMaterial(m, out, folderName)
            );
        }
    }

    public static void serializeMaterial(ObjMaterial m, PrintWriter out, File folderName) {
        out.println(NEWMAT + m.getName());
        out.println(String.format("%s%f", SPECULAR_EXP, m.getSpecularExponent()));
        out.println(AMBIENT_COLOR + m.getAmbientColor());
        out.println(DIFFUSE_COLOR + m.getDiffuseColor());
        out.println(SPECULAR_COLOR + m.getSpecularColor());
        out.println(EMISSION_COLOR + m.getEmissionColor());
        out.println(String.format("%s%f", OPTICAL_DENSITY, m.getOpticalDensity()));
        out.println(String.format("%s%f", DISSOLVE, m.getDissolve()));
        out.println(String.format("%s%d", ILLUM, m.getIlluminationMode()));
        if (m.getDiffuseMap() != null && !new File(folderName, m.getDiffuseMap().getName()).exists()) {
            out.println(DIFFUSE_MAP + m.getDiffuseMap().getName());
            try (FileOutputStream f = new FileOutputStream(new File(folderName, m.getDiffuseMap().getName()))) {
                Files.copy(m.getDiffuseMap().toPath(), f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (m.getBumpMap() != null && !new File(folderName, m.getBumpMap().getName()).exists()) {
            out.println(BUMP_MAP + m.getBumpMap().getName());
            try (FileOutputStream f = new FileOutputStream(new File(folderName, m.getBumpMap().getName()))) {
                Files.copy(m.getBumpMap().toPath(), f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out.println();
    }

}
