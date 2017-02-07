package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.PrintWriter;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class FaceNode {
    final private Vertex3d vertex;
    private Vertex2d uv;
    private Vertex3d normal;

    public void serialize(PrintWriter out) {
        String node = " " + vertex.getIndex()
                + '/' + (uv.getIndex() != 0 ? uv.getIndex() : "")
                + '/' + (normal.getIndex() != 0 ? normal.getIndex() : "");
        out.print(node);
    }
}
