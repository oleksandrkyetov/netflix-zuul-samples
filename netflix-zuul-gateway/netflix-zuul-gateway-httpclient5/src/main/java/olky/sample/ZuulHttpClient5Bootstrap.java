package olky.sample;

import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import com.netflix.governator.InjectorBuilder;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZuulHttpClient5Bootstrap {

    public static void main(final String[] args) {
        new ZuulHttpClient5Bootstrap().start();
    }

    private void start() {
        LOGGER.debug("Zuul + HttpClient5 is starting ...");

        int exitCode = 0;

        Server server = null;
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("application");

            final Injector injector = InjectorBuilder.fromModule(new ZuulHttpClient5Module()).createInjector();
            server = injector.getInstance(BaseServerStartup.class).server();

            LOGGER.debug("Zuul + HttpClient5 successfully started");
            server.start(true);
        } catch (final Throwable t) {
            LOGGER.error("Zuul + HttpClient5 failed to start");
            exitCode = 1;
        } finally {
            if (server != null) {
                server.stop();
            }

            System.exit(exitCode);
        }
    }

}
