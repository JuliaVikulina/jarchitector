package org.dandj.core.conversion.obj;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * spec : http://www.martinreddy.net/gfx/3d/OBJ.spec
 */
@Data
public class ObjFile {

    private List<String> comment = new ArrayList<>();
    private ObjMaterialLibrary mtllib;
    private List<ObjGeometry> objects = new ArrayList<>();

    public ObjFile() {
    }

    public ObjFile(String name) {
        mtllib = new ObjMaterialLibrary(name + ".mtl");
    }

}
