package org.dandj.core.conversion.obj;

import lombok.Data;

@Data
public class ObjMaterial {
    private final String name;
    private Float specular;
    private Float opticalDensity;
    private Float specularExponent;
    private Float dissolve;
    private Integer illuminationMode;
    private Vertex3d ambientColor;
    private Vertex3d diffuseColor;
    private Vertex3d specularColor;
    private Vertex3d emissionColor;

    public ObjMaterial(String name) {
        this.name = name;
    }
}
