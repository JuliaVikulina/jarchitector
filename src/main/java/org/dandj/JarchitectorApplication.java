package org.dandj;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dandj.resources.GenerateResource;

public class JarchitectorApplication extends Application<JarchitectorConfiguration> {

    public static void main(final String[] args) throws Exception {
        new JarchitectorApplication().run(args);
    }

    @Override
    public String getName() {
        return "Jarchitector";
    }

    @Override
    public void initialize(final Bootstrap<JarchitectorConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        // TODO: application initialization
    }

    @Override
    public void run(final JarchitectorConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new GenerateResource());
    }
}
