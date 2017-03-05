package org.dandj.core.conversion.obj;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import lombok.Data;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class FaceNode {
    private Vector3f vertex;
    private Vector2f uv;
    private Vector3f normal;

    public FaceNode(Vector3f vertex) {
        this.vertex = vertex;
    }


    public FaceNode duplicate() {
        FaceNode faceNode = new FaceNode(new Vector3f(vertex));
        if (uv != null)
            faceNode.uv = new Vector2f(uv);
        if (normal != null)
            faceNode.normal = new Vector3f(normal);
        return faceNode;
    }

    public void moveTo(float x, float z) {
        vertex = new Vector3f(vertex.getX() + x, vertex.getY(), vertex.getZ() + z);
    }

    public void rotateY(float radians) {
        Matrix3f rot = new Matrix3f();
        rot.fromAngleNormalAxis(radians, Vector3f.UNIT_Y);
        vertex = rot.mult(vertex);
        normal = rot.mult(normal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaceNode faceNode = (FaceNode) o;

        if (vertex != null ? !vertex.equals(faceNode.vertex) : faceNode.vertex != null) return false;
        if (uv != null ? !uv.equals(faceNode.uv) : faceNode.uv != null) return false;
        return normal != null ? normal.equals(faceNode.normal) : faceNode.normal == null;
    }

    @Override
    public int hashCode() {
        int result = vertex != null ? vertex.hashCode() : 0;
        result = 31 * result + (uv != null ? uv.hashCode() : 0);
        result = 31 * result + (normal != null ? normal.hashCode() : 0);
        return result;
    }
}
