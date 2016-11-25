package org.dandj.resources;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.dandj.api.API;
import org.dandj.core.generation.StageGenerator;

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
            @FormParam("seed") @DefaultValue("23") int seed) {
        API.Stage.Builder builder = API.Stage.newBuilder();
        builder.setHeight(height).setWidth(width).setResolution(1);
        Random r = new Random(seed);
        StageGenerator.createStage(builder, r);
        try {
            return JsonFormat.printer().print(builder.build());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    @GET
    public String test() {
        return "all ok!";
    }
}
