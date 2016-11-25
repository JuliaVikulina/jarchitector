package org.dandj.core.generation;


import com.google.protobuf.util.JsonFormat;
import org.dandj.utils.AsciiPrinter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.dandj.api.API.Stage;

public class StageGeneratorTest {
    @Test
    public void createStage() throws Exception {
        Stage.Builder builder = Stage.newBuilder()
                .setWidth(6)
                .setHeight(5)
                .setResolution(5);
        Random r = new Random(42);
        Stage stage = StageGenerator.createStage(builder, r);
        String expected = "{\n" +
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
        Assert.assertEquals(expected, JsonFormat.printer().print(stage));
    }
}