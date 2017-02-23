package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.dandj.core.conversion.obj.ObjConstants.*;
/**
 * spec : http://www.martinreddy.net/gfx/3d/OBJ.spec
 */
@Data
public class ObjFile {

    private List<String> comment = new ArrayList<>();
    private ObjMaterialLibrary mtllib;
    private List<ObjGeometry> objects = new ArrayList<>();

    public ObjFile() {
    }

    public ObjFile(File file) throws IOException {
        ObjGeometry currentObject = null;
        ArrayList<Vertex3d> vertices = new ArrayList<>();
        ArrayList<Vertex2d> texCoords = new ArrayList<>();
        ArrayList<Vertex3d> normals = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            if (line.trim().startsWith(COMMENT))
                comment.add(line);
            else if (line.startsWith(MTLLIB))
                mtllib = new ObjMaterialLibrary(strip(line, MTLLIB), file.getParent());
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
                currentObject.setMaterial(mtllib.getMaterials().get(strip(line, MATERIAL)));
            else if (line.startsWith(SMOOTH) && currentObject != null)
                currentObject.setSmoothGroup(strip(line, SMOOTH));
            else if (line.startsWith(FACE) && currentObject != null)
                currentObject.addFace(strip(line, FACE), vertices, texCoords, normals);
            else System.err.println("Not recognized command:\n" + line);
        }
    }

    public ObjFile(String materialLibName) {
        mtllib = new ObjMaterialLibrary(materialLibName + ".mtl");
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
