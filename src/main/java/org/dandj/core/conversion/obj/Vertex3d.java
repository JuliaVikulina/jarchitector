package org.dandj.core.conversion.obj;

import lombok.Value;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Value
public class Vertex3d {
    private double x;
    private double y;
    private double z;

    public Vertex3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3d(String[] line) {
        x = Double.parseDouble(line[0]);
        y = Double.parseDouble(line[1]);
        z = Double.parseDouble(line[2]);
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

    public Vertex3d mult(double f) {
        return new Vertex3d(x * f, y * f, z * f);
    }

    public Vertex3d rotate(double a) {
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Vertex3d(x * cos - y * sin, y * cos + x * sin, z);
    }

}
