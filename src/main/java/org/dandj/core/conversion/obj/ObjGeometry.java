package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class ObjGeometry {
    private String material;

    private String name;

    private ArrayList<Face3d> faces = new ArrayList<>();

    // used for import/export only!
    private transient Integer smoothGroup;


    public ObjGeometry(String name) {
        this.name = name;
    }

    public void serialize(PrintWriter out, IndexOffset offset) {
        if (faces.isEmpty())
            return;
        out.println("o " + name);
        List<Vertex3d> vertices = new ArrayList<>();
        List<Vertex2d> uvs = new ArrayList<>();
        List<Vertex3d> normals = new ArrayList<>();

        faces.forEach(f -> f.getNodes().forEach(n -> {
            if (n.getVertex().getIndex() == 0) {
                n.getVertex().setIndex(++offset.vertex);
                vertices.add(n.getVertex());
            }
            if (n.getUv() != null && n.getUv().getIndex() == 0) {
                n.getUv().setIndex(++offset.uv);
                uvs.add(n.getUv());
            }
            if (n.getNormal() != null && n.getNormal().getIndex() == 0) {
                n.getNormal().setIndex(++offset.normal);
                normals.add(n.getNormal());
            }
        }));
        vertices.forEach(v -> v.serialize(out, "v"));
        uvs.forEach(u -> u.serialize(out, "vt"));
        normals.forEach(n -> n.serialize(out, "vn"));
        out.println("usemtl " + material);
        out.println("s off"); //todo implement smooth groups
        faces.forEach(f -> f.serialize(out));
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

    void addFace(String line, ArrayList<Vertex3d> vertices, ArrayList<Vertex2d> texCoords, ArrayList<Vertex3d> normals) {
        String[] faceIndices = line.split(" ");
        Face3d face = new Face3d();
        face.setSmoothGroup(smoothGroup);
        for (String chunk : faceIndices) {
            String[] indices = chunk.split("/");
            FaceNode faceNode = new FaceNode(vertices.get(Integer.parseInt(indices[0]) - 1));
            if (!indices[1].isEmpty())
                faceNode.setUv(texCoords.get(Integer.parseInt(indices[1]) - 1));
            if (!indices[2].isEmpty())
                faceNode.setNormal(normals.get(Integer.parseInt(indices[2]) - 1));
            face.getNodes().add(faceNode);
        }
        faces.add(face);
    }

    public void moveTo(double x, double y) {
        faces.forEach(face3d -> face3d.moveTo(x, y));
    }

    public ObjGeometry duplicate() {
        ObjGeometry geometry = new ObjGeometry(name + UUID.randomUUID());
        geometry.material = material;
        faces.forEach(face3d -> geometry.faces.add(face3d.duplicate()));
        return geometry;
    }
}
