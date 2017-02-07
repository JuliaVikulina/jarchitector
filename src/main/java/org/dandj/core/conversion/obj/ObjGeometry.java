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
    private ArrayList<Vertex2d> texCoords = new ArrayList<>();

    private ArrayList<Vertex3d> normals = new ArrayList<>();

    private ArrayList<Face3d> faces = new ArrayList<>();

    // used for import/export only!
    private transient Integer smoothGroup;

    private int currentVertex;
    private int currentTexCoord;
    private int currentNormal;

    ObjGeometry(String name) {
        this.name = name;
    }

    public void serialize(PrintWriter out) {
        out.println("o " + name);
        if (vertices != null)
            vertices.forEach(v ->
                    out.println(format("v %f %f %f", v.getX(), v.getY(), v.getZ())));
        if (texCoords != null)
            texCoords.forEach(vt ->
                    out.println(format("vt %f %f", vt.getX(), vt.getY())));
        if (normals != null)
            normals.forEach(vn ->
                    out.println(format("vn %f %f %f", vn.getX(), vn.getY(), vn.getZ())));
        out.println("usemtl " + (material != null ? material : "None"));
        if (faces != null && !faces.isEmpty()) {
            smoothGroup = faces.get(0).getSmoothGroup();
            out.println("s " + (smoothGroup != null ? smoothGroup : "off"));
            faces.forEach(face3d -> {
                if (!Objects.equals(face3d.getSmoothGroup(), smoothGroup))
                    out.println("s " + (smoothGroup != null ? smoothGroup : "off"));
                out.println("f " + face3d);
            });
        }
    }

    void addVertex(String line) {
        vertices.add(new Vertex3d(line));
        currentVertex++;
    }

    void addNormal(String line) {
        normals.add(new Vertex3d(line));
        currentNormal++;
    }

    void addTexCoord(String line) {
        texCoords.add(new Vertex2d(line));
        currentTexCoord++;
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
        String[] faceIndices = line.split(" ");
        Face3d face = new Face3d(smoothGroup);
        for (String chunk : faceIndices) {
            String[] indices = chunk.split("/");
            FaceNode faceNode = new FaceNode(vertices.get(Integer.parseInt(indices[0])));
            if (!indices[1].isEmpty())
                faceNode.setTextureCoord(texCoords.get(Integer.parseInt(indices[1])));
            // there is a normal
            if (!indices[2].isEmpty())
                faceNode.setNormal(normals.get(Integer.parseInt(indices[1])));
            face.getSurface().add(faceNode);
        }
        faces.add(face);
    }
}
