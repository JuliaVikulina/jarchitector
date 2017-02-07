package org.dandj.core.conversion.obj;

import lombok.Data;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Face3d {
    private final Integer smoothGroup;
    private ArrayList<FaceNode> nodes = new ArrayList<>();

    public void serialize(PrintWriter out) {
        out.print("f");
        nodes.forEach(n -> n.serialize(out));
        out.println();
    }
}
