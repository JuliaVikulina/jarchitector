package org.dandj.core.conversion.obj;

import lombok.Data;

import javax.annotation.Nonnull;
import java.io.PrintWriter;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Vertex3d {
    private final double x;
    private final double y;
    private final double z;
    private int index;

    public Vertex3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3d(String line) {
        String[] split = line.split(" ");
        x = Double.parseDouble(split[0]);
        y = Double.parseDouble(split[1]);
        z = Double.parseDouble(split[2]);
    }

    public Vertex3d(Vertex3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vertex3d(Vertex2d v) {
        x = v.getX();
        y = v.getY();
        z = 0;
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

    public void serialize(@Nonnull PrintWriter out, @Nonnull String type) {
        out.println(String.format("%s %f %f %f", type, x, y, z));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ')';
    }
}
