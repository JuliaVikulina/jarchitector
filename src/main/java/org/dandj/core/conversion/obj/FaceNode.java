package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.PrintWriter;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class FaceNode {
    private Vertex3d vertex;
    private Vertex2d uv;
    private Vertex3d normal;

    public FaceNode(Vertex3d vertex) {
        this.vertex = vertex;
    }

    public void serialize(PrintWriter out) {
        String node = " " + vertex.getIndex()
                + '/' + (uv != null && uv.getIndex() != 0 ? uv.getIndex() : "")
                + '/' + (normal != null && normal.getIndex() != 0 ? normal.getIndex() : "");
        out.print(node);
    }

    public FaceNode duplicate() {
        FaceNode faceNode = new FaceNode(new Vertex3d(vertex));
        if (uv != null)
            faceNode.uv = new Vertex2d(uv);
        if (normal != null)
            faceNode.normal = new Vertex3d(normal);
        return faceNode;
    }

    public void moveTo(double x, double y) {
        vertex = new Vertex3d(vertex.getX() + x, vertex.getY() + y, vertex.getZ());
    }
}
