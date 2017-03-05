package org.dandj.core.conversion.obj;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Face3f {
    private Integer smoothGroup;
    private List<FaceNode> nodes = new ArrayList<>();


    public Face3f duplicate() {
        Face3f clone = new Face3f();
        nodes.forEach(node -> clone.nodes.add(node.duplicate()));
        return clone;
    }

    public void moveTo(float x, float z) {
        nodes.forEach(node -> node.moveTo(x, z));
    }

    public void rotateY(float radians) {
        nodes.forEach(node -> node.rotateY(radians));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Face3f face3f = (Face3f) o;

        if (smoothGroup != null ? !smoothGroup.equals(face3f.smoothGroup) : face3f.smoothGroup != null) return false;
        return nodes != null ? nodes.equals(face3f.nodes) : face3f.nodes == null;
    }

    @Override
    public int hashCode() {
        int result = smoothGroup != null ? smoothGroup.hashCode() : 0;
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        return result;
    }
}
