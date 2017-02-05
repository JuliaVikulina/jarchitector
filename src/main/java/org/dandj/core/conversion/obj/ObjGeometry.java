package org.dandj.core.conversion.obj;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class ObjGeometry {
    String material;

    String name;

    ArrayList<Vertex3d> vertices = new ArrayList<>();

    // texture coordinate
    ArrayList<Vertex2d> uv = new ArrayList<>();

    ArrayList<Vertex3d> normal = new ArrayList<>();

    ArrayList<Face3d> faces = new ArrayList<>();

    public ObjGeometry(String name) {
        this.name = name;
    }

    public void serialize(PrintWriter out) {
        out.println("o " + name);
        if (vertices != null)
            vertices.forEach(v ->
                    out.println("v " + v.x + " " + v.y + " " + v.z));
        if (uv != null)
            uv.forEach(vt ->
                    out.println("vt " + vt.x + " " + vt.y));
        if (normal != null)
            normal.forEach(vn ->
                    out.println("vn " + vn.x + " " + vn.y + " " + vn.z));
        out.println("usemtl " + (material != null ? material : "None"));
        out.println("s off"); // todo use smooth group
        if (faces != null)
            faces.forEach(face3d ->
                    out.println("f " + face3d));
    }

    public void addVertex(String line) {

    }

    public void addNormal(String line) {

    }

    public void addTexCoord(String line) {

    }

    public void setMaterial(String line) {

    }

    public void setSmooth(String smooth) {

    }

    public void addFace(String line) {

    }
}
