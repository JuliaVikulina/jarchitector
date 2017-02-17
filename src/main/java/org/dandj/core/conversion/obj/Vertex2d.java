package org.dandj.core.conversion.obj;

import lombok.Data;

import javax.annotation.Nonnull;
import java.io.PrintWriter;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Data
public class Vertex2d {
    private final double x;
    private final double y;
    private int index;

    public Vertex2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex2d(Vertex2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vertex2d(String line) {
        String[] split = line.split(" ");
        x = Double.parseDouble(split[0]);
        y = Double.parseDouble(split[1]);
    }

    public Vertex2d add(Vertex2d v) {
        return new Vertex2d(this.x + v.x, this.y + v.y);
    }

    public Vertex2d mult(double f) {
        return new Vertex2d(this.x * f, this.y * f);
    }

    public Vertex2d rotate(double a) {
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        return new Vertex2d(x * cos - y * sin, y * cos + x * sin);
    }

    public void serialize(@Nonnull PrintWriter out, @Nonnull String type) {
        out.println(String.format("%s%f %f", type, x, y));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
