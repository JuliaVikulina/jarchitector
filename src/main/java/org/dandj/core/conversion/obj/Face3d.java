package org.dandj.core.conversion.obj;

import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class Face3d {
    ArrayList<FaceNode> surface = new ArrayList<>();
    Integer smoothGroup;

    public Face3d(String[] faces, Integer smooth) {
        smoothGroup = smooth;
        for (String face : faces) {
            surface.add(new FaceNode(face.split("/")));
        }
    }

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
