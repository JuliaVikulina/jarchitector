package org.dandj.core.conversion.obj;

import lombok.Data;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class FaceNode {
    final private Vertex3d vertex;
    private Vertex2d textureCoord;
    private Vertex3d normal;
}
