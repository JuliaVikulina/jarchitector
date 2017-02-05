package org.dandj.core.conversion.obj;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * spec : http://www.martinreddy.net/gfx/3d/OBJ.spec
 */
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
    private ObjGeometry currentObject;

    private ObjFile(File fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith(COMMENT))
                comment.add(line);
            else if (line.startsWith(MTLLIB))
                mtllib = new ObjMaterialLibrary(strip(line, MTLLIB), fileName.getParent());
            else if (line.startsWith(OBJECT)) {
                currentObject = new ObjGeometry(strip(line, OBJECT));
                objects.add(currentObject);
            } else if (line.startsWith(VERTEX))
                currentObject.addVertex(strip(line, VERTEX));
            else if (line.startsWith(NORMAL))
                currentObject.addNormal(strip(line, NORMAL));
            else if (line.startsWith(TEXCOORD))
                currentObject.addTexCoord(strip(line, TEXCOORD));
            else if (line.startsWith(MATERIAL))
                currentObject.setMaterial(strip(line, MATERIAL));
            else if (line.startsWith(SMOOTH))
                currentObject.setSmooth(strip(line, SMOOTH));
            else if (line.startsWith(FACE))
                currentObject.addFace(strip(line, FACE));
            else throw new IllegalStateException("Not recognized command:\n" + line);
        }
    }

    public static String strip(String line, String token) {
        if (line == null || token == null || !line.contains(token))
            return null;
        return line.substring(line.indexOf(token) + token.length(), line.length());
    }

    public void serialize(PrintWriter out) throws IOException {
        comment.forEach(out::println);
        out.println(MTLLIB + mtllib);
        objects.forEach(o -> o.serialize(out));
    }
}
