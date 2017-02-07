package org.dandj.core.conversion.obj;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ObjFileTest {
    @Test
    public void testImport() throws Exception {
        ObjFile f = new ObjFile(new File("simple-wall-set2.obj"));
        StringWriter out = new StringWriter();
        f.serialize(new PrintWriter(out));
        System.out.println(out.toString());
        try (FileWriter fileWriter = new FileWriter("simple-wall-set22.obj")) {
            fileWriter.write(out.toString());
        }
    }
}