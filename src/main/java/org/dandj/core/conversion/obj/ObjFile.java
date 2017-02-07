package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * spec : http://www.martinreddy.net/gfx/3d/OBJ.spec
 */
@Data
public class ObjFile {
    private static final String COMMENT = "#";
    private static final String MTLLIB = "mtllib ";
    private static final String OBJECT = "o ";
    private static final String VERTEX = "v ";
    private static final String NORMAL = "vn ";
    private static final String TEXCOORD = "vt ";
    private static final String FACE = "f ";
    private static final String MATERIAL = "usemtl ";
    private static final String SMOOTH = "s ";

    private List<String> comment = new ArrayList<>();
    private ObjMaterialLibrary mtllib;
    private List<ObjGeometry> objects = new ArrayList<>();

    public ObjFile() {
    }

    public ObjFile(File fileName) throws IOException {
        ObjGeometry currentObject = null;
        ArrayList<Vertex3d> vertices = new ArrayList<>();
        ArrayList<Vertex2d> texCoords = new ArrayList<>();
        ArrayList<Vertex3d> normals = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            if (line.trim().startsWith(COMMENT))
                comment.add(line);
            else if (line.startsWith(MTLLIB))
                mtllib = new ObjMaterialLibrary(strip(line, MTLLIB));
            else if (line.startsWith(OBJECT)) {
                currentObject = new ObjGeometry(strip(line, OBJECT));
                objects.add(currentObject);
            } else if (line.startsWith(VERTEX))
                vertices.add(new Vertex3d(strip(line, VERTEX)));
            else if (line.startsWith(TEXCOORD))
                texCoords.add(new Vertex2d(strip(line, TEXCOORD)));
            else if (line.startsWith(NORMAL))
                normals.add(new Vertex3d(strip(line, NORMAL)));
            else if (line.startsWith(MATERIAL) && currentObject != null)
                currentObject.setMaterial(strip(line, MATERIAL));
            else if (line.startsWith(SMOOTH) && currentObject != null)
                currentObject.setSmoothGroup(strip(line, SMOOTH));
            else if (line.startsWith(FACE) && currentObject != null)
                currentObject.addFace(strip(line, FACE), vertices, texCoords, normals);
            else throw new IllegalStateException("Not recognized command:\n" + line);
        }
    }

    public static String strip(String line, String token) {
        if (line != null && token != null && line.contains(token))
            return line.substring(line.indexOf(token) + token.length(), line.length());
        return null;
    }

    public void serialize(PrintWriter out) throws IOException {
        comment.forEach(out::println);
        out.println(MTLLIB + mtllib);
        final IndexOffset offset = new IndexOffset();
        objects.forEach(o -> o.serialize(out, offset));
    }
}
