package org.dandj.core.conversion.obj;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
public class Vertex3d {
    public float x;
    public float y;
    public float z;

    public Vertex3d(String[] line) {
        x = Float.parseFloat(line[0]);
        y = Float.parseFloat(line[1]);
        z = Float.parseFloat(line[2]);
    }
}
