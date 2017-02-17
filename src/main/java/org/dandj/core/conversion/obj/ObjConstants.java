package org.dandj.core.conversion.obj;

/**
 * Created by daniil on 17.02.17.
 */
public interface ObjConstants {
    String COMMENT = "#";

    // Geometry
    String MTLLIB = "mtllib ";
    String OBJECT = "o ";
    String VERTEX = "v ";
    String NORMAL = "vn ";
    String TEXCOORD = "vt ";
    String FACE = "f ";
    String MATERIAL = "usemtl ";
    String SMOOTH = "s ";

    // Material
    String NEWMAT = "newmtl ";
    String AMBIENT_COLOR = "Ka ";
    String DIFFUSE_COLOR = "Kd ";
    String DIFFUSE_MAP = "map_Kd ";
    String BUMP_MAP = "map_Bump ";
    String SPECULAR_COLOR = "Ks ";
    String EMISSION_COLOR = "Ke ";
    String SPECULAR_EXP = "Ns ";
    String OPTICAL_DENSITY = "Ni ";
    String DISSOLVE = "d ";

    /*
        0. Color on and Ambient off
        1. Color on and Ambient on
        2. Highlight on
        3. Reflection on and Ray trace on
        4. Transparency: Glass on, Reflection: Ray trace on
        5. Reflection: Fresnel on and Ray trace on
        6. Transparency: Refraction on, Reflection: Fresnel off and Ray trace on
        7. Transparency: Refraction on, Reflection: Fresnel on and Ray trace on
        8. Reflection on and Ray trace off
        9. Transparency: Glass on, Reflection: Ray trace off
        10. Casts shadows onto invisible surfaces
     */
    String ILLUM = "illum ";

}
