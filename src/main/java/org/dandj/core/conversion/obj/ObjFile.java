package org.dandj.core.conversion.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjFile {
    private final String COMMENT = "#";
    private final String MTLLIB = "mtllib ";
    private final String OBJECT = "o ";
    private final String VERTEX = "v ";
    private final String NORMAL = "vn ";
    private final String TEXCOORD = "vt ";
    private final String FACE = "f ";
    private final String MATERIAL = "usemtl ";
    private final String SMOOTH = "s ";

    StringBuilder comment = new StringBuilder();
    ObjMaterial mtllib;
    List<ObjGeometry> objects = new ArrayList<>();
    ObjGeometry currentObject;

    public ObjFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith(COMMENT))
                comment.append(line).append('\n');
            else if (line.startsWith(MTLLIB))
                mtllib = new ObjMaterial(strip(line, MTLLIB));
            else if (line.startsWith(OBJECT))
                currentObject = new ObjGeometry(strip(line, OBJECT));
            else if (line.startsWith(VERTEX))
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
}
