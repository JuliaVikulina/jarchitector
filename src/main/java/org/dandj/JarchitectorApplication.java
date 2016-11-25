package org.dandj;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final JarchitectorConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
