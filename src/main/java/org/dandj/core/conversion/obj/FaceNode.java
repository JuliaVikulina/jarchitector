package org.dandj.core.conversion.obj;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class FaceNode {
    final Integer vertexIndex;

    final Integer textureCoordIndex;

    final Integer normalIndex;

    public FaceNode(String[] ints) {
        vertexIndex = Integer.parseInt(ints[0]);
        textureCoordIndex = ints[1].isEmpty() ? null : Integer.parseInt(ints[1]);
        normalIndex = ints[2].isEmpty() ? null : Integer.parseInt(ints[2]);

    }

    @Override
    public String toString() {
        return String.valueOf(vertexIndex)
                + '/'
                + (textureCoordIndex == null ? "" : textureCoordIndex)
                + '/'
                + (normalIndex == null ? "" : normalIndex);

    }
}
