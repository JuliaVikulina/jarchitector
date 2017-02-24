package org.dandj.core.conversion.obj;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ObjMaterialLibrary {

    private String name;
    private Map<String, ObjMaterial> materials = new HashMap<>();
    private List<String> comment = new ArrayList<>();

    public ObjMaterialLibrary(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
