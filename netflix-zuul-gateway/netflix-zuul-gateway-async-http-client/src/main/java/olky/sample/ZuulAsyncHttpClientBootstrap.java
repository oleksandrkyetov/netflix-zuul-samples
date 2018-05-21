package olky.sample;

import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import com.netflix.governator.InjectorBuilder;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.Server;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZuulAsyncHttpClientBootstrap {

    public static void main(final String[] args) {
        new ZuulAsyncHttpClientBootstrap().start();
    }

    private void start() {
        LOGGER.debug("Zuul + AsyncHttpClient is starting ...");

        int exitCode = 0;

        Server server = null;
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("application");

            final Injector injector = InjectorBuilder.fromModule(new ZuulAsyncHttpClientModule()).createInjector();
            server = injector.getInstance(BaseServerStartup.class).server();

            LOGGER.debug("Zuul + AsyncHttpClient successfully started");
            server.start(true);
        } catch (final Throwable t) {
            LOGGER.error("Zuul + AsyncHttpClient failed to start");
            exitCode = 1;
        } finally {
            if (server != null) {
                server.stop();
            }

            System.exit(exitCode);
        }
    }

}
