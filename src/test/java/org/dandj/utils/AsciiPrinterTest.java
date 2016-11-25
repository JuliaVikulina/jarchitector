package org.dandj.utils;

import com.google.protobuf.util.JsonFormat;
import org.dandj.api.API;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by daniil on 26.11.16.
 */
public class AsciiPrinterTest {
    @Test
    public void printStage() throws Exception {
        String result = "{\n" +
                "  \"resolution\": 5,\n" +
                "  \"width\": 6,\n" +
                "  \"height\": 5,\n" +
                "  \"regions\": [{\n" +
                "    \"cells\": [{\n" +
                "      \"x\": 2,\n" +
                "      \"y\": 0\n" +
                "    }, {\n" +
                "      \"x\": 2,\n" +
                "      \"y\": 1\n" +
                "    }, {\n" +
                "      \"x\": 2,\n" +
                "      \"y\": 2\n" +
                "    }, {\n" +
                "      \"x\": 3,\n" +
                "      \"y\": 0\n" +
                "    }, {\n" +
                "      \"x\": 3,\n" +
                "      \"y\": 1\n" +
                "    }, {\n" +
                "      \"x\": 3,\n" +
                "      \"y\": 2\n" +
                "    }, {\n" +
                "      \"x\": 4,\n" +
                "      \"y\": 0\n" +
                "    }, {\n" +
                "      \"x\": 4,\n" +
                "      \"y\": 1\n" +
                "    }, {\n" +
                "      \"x\": 4,\n" +
                "      \"y\": 2\n" +
                "    }]\n" +
                "  }]\n" +
                "}";
        API.Stage.Builder builder = API.Stage.newBuilder();
        JsonFormat.parser().merge(result, builder);
        String expected = "..###.\n" +
                "..###.\n" +
                "..###.\n" +
                "......\n" +
                "......\n";
        Assert.assertEquals(expected, AsciiPrinter.printStage(builder));
    }

}