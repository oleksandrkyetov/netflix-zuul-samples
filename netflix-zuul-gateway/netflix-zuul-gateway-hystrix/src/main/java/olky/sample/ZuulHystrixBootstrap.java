package olky.sample;

import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import com.netflix.governator.InjectorBuilder;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZuulHystrixBootstrap {

    public static void main(final String[] args) {
        new ZuulHystrixBootstrap().start();
    }

    private void start() {
        LOGGER.debug("Zuul + Hystrix is starting ...");

        int exitCode = 0;

        Server server = null;
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("application");

            final Injector injector = InjectorBuilder.fromModule(new ZuulHystrixModule()).createInjector();
            server = injector.getInstance(BaseServerStartup.class).server();

            LOGGER.debug("Zuul + Hystrix successfully started");
            server.start(true);
        } catch (final Throwable t) {
            LOGGER.error("Zuul + Hystrix failed to start");
            exitCode = 1;
        } finally {
            if (server != null) {
                server.stop();
            }

            System.exit(exitCode);
        }
    }

}
