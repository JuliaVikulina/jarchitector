package org.dandj.core.conversion.obj;

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
        ObjFile f = new ObjFile(new File("tiles/qtile-tech-2/qtile-tech-2.obj"));
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
            List<Vertex3d> vertices = Arrays.asList(new Vertex3d(1, 2, 3), new Vertex3d(4, 5, 6), new Vertex3d(7, 8, 9));
            List<Vertex2d> uvs = asList(new Vertex2d(10, 11), new Vertex2d(12, 13), new Vertex2d(14, 15));
            List<Vertex3d> normals = asList(new Vertex3d(16, 17, 18), new Vertex3d(19, 20, 21), new Vertex3d(22, 23, 24));
            geometry.addFace("1/1/1 2/2/2 3/3/3", vertices, uvs, normals);
            ObjMaterial sampleMaterial = new ObjMaterial("sampleMaterial");
            geometry.setMaterial(sampleMaterial);
            objFile.getObjects().add(geometry);
            ObjMaterialLibrary sampleMtlib = new ObjMaterialLibrary("sampleMtlib");
            sampleMtlib.getMaterials().put("someTestKey", sampleMaterial);
            objFile.setMtllib(sampleMtlib);
        }
        StringWriter out = new StringWriter();
        objFile.serialize(new PrintWriter(out));
        String expected = new String(IOUtils.readFully(this.getClass().getResourceAsStream("test.obj"), -1, true));
        assertEquals(expected, out.toString());
    }

    /**
     * Test that objects after export/import are equal
     */
    @Test
    public void testImportExportEqual() throws Exception {
        ObjFile f = new ObjFile(new File("tiles/test-tile-1/test-tileset.obj"));
        StringWriter out = new StringWriter();
        f.serialize(new PrintWriter(out));
        System.out.println(out.toString());
        try (FileWriter fileWriter = new FileWriter("ObjFileTest-result.obj")) {
            fileWriter.write(out.toString());
        }
    }
}