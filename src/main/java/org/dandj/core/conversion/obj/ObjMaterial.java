package org.dandj.core.conversion.obj;

/**
 * Created by daniil on 05.02.17.
 */
public class ObjMaterial {
    private String mtllibLine;

    public ObjMaterial(String mtllibLine) {
        this.mtllibLine = mtllibLine;
    }

    @Override
    public String toString() {
        return mtllibLine;
    }
}
