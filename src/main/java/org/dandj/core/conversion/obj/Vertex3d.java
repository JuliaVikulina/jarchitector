package org.dandj.core.conversion.obj;

import lombok.Value;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Value
public class Vertex3d {
    private float x;
    private float y;
    private float z;

    public Vertex3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3d(String[] line) {
        x = Float.parseFloat(line[0]);
        y = Float.parseFloat(line[1]);
        z = Float.parseFloat(line[2]);
    }

    public Vertex3d(Vertex3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vertex3d add(Vertex3d op) {
        return new Vertex3d(
                x + op.x,
                y + op.y,
                z + op.z);
    }

    public Vertex3d mult(float f) {
        return new Vertex3d(x * f, y * f, z * f);
    }

//    public Vertex3d rotate(float a) {
//        return new Vertex3d(x * Math.si);
//    }

}
