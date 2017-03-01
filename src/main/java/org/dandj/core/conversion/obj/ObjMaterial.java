package org.dandj.core.conversion.obj;

import com.jme3.math.ColorRGBA;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode(exclude = {"diffuseMap", "bumpMap"})
public class ObjMaterial {
    private String name = "default";
    private Double specularExponent = 100d;
    private Double opticalDensity = 1d;
    private Double dissolve = 1d;
    private Integer illuminationMode = 2;
    private ColorRGBA ambientColor = new ColorRGBA(1, 1, 1, 0);
    private ColorRGBA diffuseColor = new ColorRGBA(0.64f, 0.64f, 0.64f, 0);
    private ColorRGBA specularColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 0);
    private ColorRGBA emissionColor = new ColorRGBA(0, 0, 0, 0);
    private File diffuseMap;
    private File bumpMap;

    public ObjMaterial(String name) {
        this.name = name;
    }

}
