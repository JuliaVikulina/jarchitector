package org.dandj.core.conversion.obj;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ObjFileTest {

    /**
     * Test importing code
     */
    @Test
    public void testImport() throws Exception {
        ObjFile f = new ObjFile(new File("tiles/test-tile-1/test-tileset.obj"));
    }

    /**
     * Test exporting code
     */
    @Test
    public void testExport() throws Exception {

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