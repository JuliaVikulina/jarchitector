package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.String.format;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class ObjGeometry {
    private String material;

    private String name;

    private ArrayList<Vertex3d> vertices = new ArrayList<>();

    // texture coordinate
    private ArrayList<Vertex2d> uv = new ArrayList<>();

    private ArrayList<Vertex3d> normal = new ArrayList<>();

    private ArrayList<Face3d> faces = new ArrayList<>();

    // used for import/export only!
    private transient Integer smoothGroup;

    ObjGeometry(String name) {
        this.name = name;
    }

    public void serialize(PrintWriter out) {
        out.println("o " + name);
        if (vertices != null)
            vertices.forEach(v ->
                    out.println(format("v %f %f %f", v.getX(), v.getY(), v.getZ())));
        if (uv != null)
            uv.forEach(vt ->
                    out.println(format("vt %f %f", vt.getX(), vt.getY())));
        if (normal != null)
            normal.forEach(vn ->
                    out.println(format("vn %f %f %f", vn.getX(), vn.getY(), vn.getZ())));
        out.println("usemtl " + (material != null ? material : "None"));
        if (faces != null && !faces.isEmpty()) {
            smoothGroup = faces.get(0).smoothGroup;
            out.println("s " + (smoothGroup != null ? smoothGroup : "off"));
            faces.forEach(face3d -> {
                if (!Objects.equals(face3d.smoothGroup, smoothGroup))
                    out.println("s " + (smoothGroup != null ? smoothGroup : "off"));
                out.println("f " + face3d);
            });
        }
    }

    void addVertex(String line) {
        vertices.add(new Vertex3d(line.split(" ")));
    }

    void addNormal(String line) {
        normal.add(new Vertex3d(line.split(" ")));
    }

    void addTexCoord(String line) {
        uv.add(new Vertex2d(line.split(" ")));
    }

    void setMaterial(String line) {
        material = line;
    }

    void setSmoothGroup(String line) {
        try {
            smoothGroup = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            smoothGroup = null;
        }
    }

    void addFace(String line) {
        faces.add(new Face3d(line.split(" "), smoothGroup));
    }
}
