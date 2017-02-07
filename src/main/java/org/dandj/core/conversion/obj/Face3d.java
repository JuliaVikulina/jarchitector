package org.dandj.core.conversion.obj;

import lombok.Data;

import java.util.ArrayList;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Face3d {
    private final Integer smoothGroup;
    private ArrayList<FaceNode> surface = new ArrayList<>();
}
