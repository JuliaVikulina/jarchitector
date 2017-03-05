package org.dandj.core.conversion.obj;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
@ToString(exclude = "material")
public class ObjGeometry {
    private ObjMaterial material;

    private String name;

    private Set<Face3f> faces = new HashSet<>();

    public ObjGeometry(String name) {
        this.name = name;
    }

    void setMaterial(ObjMaterial line) {
        material = line;
    }

    void addFace(String line, List<Vector3f> vertices, List<Vector2f> texCoords, List<Vector3f> normals) {
        String[] faceIndices = line.split(" ");
        Face3f face = new Face3f();
        face.setSmoothGroup(null);
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

    public void moveTo(float x, float z) {
        faces.forEach(face3F -> face3F.moveTo(x, z));
    }

    public void rotateY(float radians) {
        faces.forEach(face3F -> face3F.rotateY(radians));
    }

    public ObjGeometry duplicate(String newName) {
        ObjGeometry geometry = new ObjGeometry(newName);
        geometry.material = material;
        faces.forEach(face3F -> geometry.faces.add(face3F.duplicate()));
        return geometry;
    }
}
