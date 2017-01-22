package org.dandj.core.conversion.obj;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class FaceNode {
    Integer vertexIndex;

    Integer textureCoordIndex;

    Integer normalIndex;

    @Override
    public String toString() {
        return String.valueOf(vertexIndex)
                + '/'
                + (textureCoordIndex == null ? "" : textureCoordIndex)
                + '/'
                + (normalIndex == null ? "" : normalIndex);

    }
}
