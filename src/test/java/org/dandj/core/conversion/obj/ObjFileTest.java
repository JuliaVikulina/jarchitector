package org.dandj.core.conversion.obj;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.junit.Test;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ObjFileTest {

    /**
     * Test importing code
     */
    @Test
    public void testImport() throws Exception {
        ObjFile f = ObjImportExport.parseObj(new File("tiles/qtile-tech-2/qtile-tech-2.obj"));
        assertEquals(2, f.getComment().size());
        assertEquals(5, f.getObjects().size());
        assertEquals(2, f.getMtllib().getMaterials().size());
    }

    /**
     * Test exporting code
     */
    @Test
    public void simpleExport() throws Exception {
        ObjFile objFile = new ObjFile();
        {
            ObjGeometry geometry = new ObjGeometry("sampleObject");
            List<Vector3f> vertices = Arrays.asList(new Vector3f(1, 2, 3), new Vector3f(4, 5, 6), new Vector3f(7, 8, 9));
            List<Vector2f> uvs = asList(new Vector2f(10, 11), new Vector2f(12, 13), new Vector2f(14, 15));
            List<Vector3f> normals = asList(new Vector3f(16, 17, 18), new Vector3f(19, 20, 21), new Vector3f(22, 23, 24));
            geometry.addFace("1/1/1 2/2/2 3/3/3", vertices, uvs, normals);
            ObjMaterial sampleMaterial = new ObjMaterial("sampleMaterial");
            geometry.setMaterial(sampleMaterial);
            objFile.getObjects().add(geometry);
            ObjMaterialLibrary sampleMtlib = new ObjMaterialLibrary("sampleMtlib.mtl");
            sampleMtlib.getMaterials().put("someTestKey", sampleMaterial);
            objFile.setMtllib(sampleMtlib);
        }
        StringWriter out = new StringWriter();
        ObjImportExport.serializeObject(objFile, new PrintWriter(out));
        String expected = new String(IOUtils.readFully(this.getClass().getResourceAsStream("test.obj"), -1, true));
        assertEquals(expected, out.toString());
    }

    /**
     * Test that objects after export/import are equal
     */
    @Test
    public void testImportExportEqual() throws Exception {
        ObjFile f = ObjImportExport.parseObj(new File("tiles/test-tile-1/test-tileset.obj"));
        StringWriter out = new StringWriter();
        ObjImportExport.serializeObject(f, new PrintWriter(out));
        System.out.println(out.toString());
        try (FileWriter fileWriter = new FileWriter("ObjFileTest-result.obj")) {
            fileWriter.write(out.toString());
        }
    }
}