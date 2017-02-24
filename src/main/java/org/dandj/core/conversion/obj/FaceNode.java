package org.dandj.core.conversion.obj;

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

    public void moveTo(float x, float y) {
        vertex = new Vector3f(vertex.getX() + x, vertex.getY() + y, vertex.getZ());
    }

    public void rotate(float radians) {
        // TODO IMPLEMENT ROTATION
//        vertex = vertex.rotate(radians);
//        normal = normal.rotate(radians);
    }
}
