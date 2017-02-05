package org.dandj.core.conversion.obj;

import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class Face3d {
    ArrayList<FaceNode> surface;
    Integer smoothGroup;

    @Override
    public String toString() {
        if (surface == null)
            return "";
        StringBuilder out = new StringBuilder();
        surface.forEach(faceNode ->
                out.append(faceNode).append(' ')
        );
        return out.toString();
    }
}
