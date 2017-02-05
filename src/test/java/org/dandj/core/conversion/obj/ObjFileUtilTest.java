package org.dandj.core.conversion.obj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjFileUtilTest {
    @Test
    public void strip() throws Exception {
        assertEquals("Cone", ObjFile.strip("o Cone", "o "));
    }

    @Test
    public void stripMissingToken() {
        assertNull(ObjFile.strip("asdf", "no!"));
    }

    @Test
    public void stripNull() {
        assertNull(ObjFile.strip(null, null));
    }
}