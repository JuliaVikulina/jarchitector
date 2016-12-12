package org.dandj.resources;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dandj.core.generation.StageGenerator;
import org.dandj.model.Stage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Random;

@Path("/generate")
public class GenerateResource {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String generate(
            @FormParam("width") @DefaultValue("21") int width,
            @FormParam("height") @DefaultValue("22") int height,
            @FormParam("seed") @DefaultValue("0") int seed) throws JsonProcessingException {
        Stage stage = new Stage().height(height).width(width);
        if (seed == 0)
            seed = new Random().nextInt(Integer.MAX_VALUE);
        StageGenerator.createStage(stage, new Random(seed));
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(om.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE));
        return om.writeValueAsString(stage);
    }
}
