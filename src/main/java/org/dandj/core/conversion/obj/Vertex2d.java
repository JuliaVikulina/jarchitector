package org.dandj.core.conversion.obj;

import lombok.Value;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Value
public class Vertex2d {
    public double x;
    public double y;

    public Vertex2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex2d(Vertex2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vertex2d(String[] line) {
        x = Double.parseDouble(line[0]);
        y = Double.parseDouble(line[1]);
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

}
