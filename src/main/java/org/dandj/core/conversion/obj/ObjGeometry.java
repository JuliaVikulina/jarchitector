package org.dandj.core.conversion.obj;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class ObjGeometry {
    Material mtl;

    String name;

    ArrayList<Vertex3d> vertices;

    // texture coordinate
    ArrayList<Vertex2d> uv;

    ArrayList<Vertex3d> normal;

    ArrayList<Face3d> faces;

    public void serialize(PrintWriter out) {
        out.println("mtllib " + (mtl != null ? mtl.filename : "None"));
        out.println("o " + name);
        if (vertices != null)
            vertices.forEach(v ->
                    out.println("v " + v.x + " " + v.y + " " + v.z)
            );
        if (uv != null)
            uv.forEach(vt ->
                    out.println("vt " + vt.x + " " + vt.y)
            );
        if (normal != null)
            normal.forEach(vn ->
                    out.println("vn " + vn.x + " " + vn.y + " " + vn.z)
            );
        out.println("usemtl " + (mtl != null ? mtl.name : "None"));
        out.println("s off");
        if (faces != null)
            faces.forEach(face3d ->
                    out.println("f " + face3d)
            );
    }
}
