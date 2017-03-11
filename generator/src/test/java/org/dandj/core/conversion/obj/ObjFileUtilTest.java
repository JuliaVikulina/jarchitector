package org.dandj.core.conversion.obj;

import com.jme3.math.Vector3f;
import org.apache.commons.io.IOUtils;
import org.dandj.model.Stage;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.dandj.core.conversion.ObjPrinter.writeStartPoint;
import static org.junit.Assert.*;

public class ObjFileUtilTest {
    @Test
    public void strip() throws Exception {
        assertEquals("Cone", ObjImportExport.strip("o Cone", "o "));
    }

    @Test
    public void stripMissingToken() {
        assertNull(ObjImportExport.strip("asdf", "no!"));
    }

    @Test
    public void stripNull() {
        assertNull(ObjImportExport.strip(null, null));
    }

    @Test
    public void testWriteStartPoint() throws IOException {
        Stage stage = new Stage();
        stage.startPosition(new Vector3f(0.5f, 0, 0.5f));
        String filename = "test_start_point";
        stage.name(filename);
        File destFolder = new File("build");
        writeStartPoint(stage, destFolder);
        File startPosFile = new File(destFolder, filename + ".yml");
        assertTrue(startPosFile.exists());

        String expected = "startPosition: !!com.jme3.math.Vector3f {x: 0.5, y: 0.0, z: 0.5}\n";
        String actual = IOUtils.toString(new FileReader(startPosFile));
        assertEquals(expected, actual);
    }
}