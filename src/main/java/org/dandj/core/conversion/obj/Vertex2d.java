package org.dandj.core.conversion.obj;

import lombok.Value;

/**
 * File ${FILE}
 * Created by Denolia on 11/12/16.
 */
@Value
public class Vertex2d {
    public float x;
    public float y;

    public Vertex2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vertex2d(Vertex2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vertex2d(String[] line) {
        x = Float.parseFloat(line[0]);
        y = Float.parseFloat(line[1]);
    }

    public Vertex2d add(Vertex2d v) {
        return new Vertex2d(this.x + v.x, this.y + v.y);
    }

    public Vertex2d mult(float f) {
        return new Vertex2d(this.x * f, this.y * f);
    }

}
