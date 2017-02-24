package org.dandj.core.conversion.obj;

import lombok.Data;

import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Face3f {
    private Integer smoothGroup;
    private ArrayList<FaceNode> nodes = new ArrayList<>();


    public Face3f duplicate() {
        Face3f clone = new Face3f();
        nodes.forEach(node -> clone.nodes.add(node.duplicate()));
        return clone;
    }

    public void moveTo(float x, float y) {
        nodes.forEach(node -> node.moveTo(x, y));
    }

    public void rotate(float radians) {
        nodes.forEach(node -> node.rotate(radians));
    }
}
