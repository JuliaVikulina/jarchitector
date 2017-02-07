package org.dandj.core.conversion.obj;

import lombok.Data;

@Data
public class ObjMaterial {
    private final String name;
    private Double specular;
    private Double opticalDensity;
    private Double specularExponent;
    private Double dissolve;
    private Integer illuminationMode;
    private Vertex3d ambientColor;
    private Vertex3d diffuseColor;
    private Vertex3d specularColor;
    private Vertex3d emissionColor;

    public ObjMaterial(String name) {
        this.name = name;
    }
}
