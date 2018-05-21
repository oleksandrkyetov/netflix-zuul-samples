package olky.sample;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.config.DynamicIntProperty;
import com.netflix.discovery.EurekaClient;
import com.netflix.netty.common.accesslog.AccessLogPublisher;
import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.netty.common.channel.config.CommonChannelConfigKeys;
import com.netflix.netty.common.metrics.EventLoopGroupMetrics;
import com.netflix.netty.common.proxyprotocol.StripUntrustedProxyHeadersHandler;
import com.netflix.netty.common.status.ServerStatusManager;
import com.netflix.spectator.api.Registry;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.FilterUsageNotifier;
import com.netflix.zuul.RequestCompleteHandler;
import com.netflix.zuul.context.SessionContextDecorator;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.DirectMemoryMonitor;
import com.netflix.zuul.netty.server.ZuulServerChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ZuulHystrixServerStartup extends BaseServerStartup {

    private static final String PROPERTY_SERVER_PORT_MAIN = "zuul.server.port.main";
    private static final int DEFAULT_SERVER_PORT_MAIN = 8081;

    @Inject
    public ZuulHystrixServerStartup(
        final ServerStatusManager serverStatusManager,
        final FilterLoader filterLoader,
        final SessionContextDecorator sessionCtxDecorator,
        final FilterUsageNotifier usageNotifier,
        final RequestCompleteHandler reqCompleteHandler,
        final Registry registry,
        final DirectMemoryMonitor directMemoryMonitor,
        final EventLoopGroupMetrics eventLoopGroupMetrics,
        final EurekaClient discoveryClient,
        final ApplicationInfoManager applicationInfoManager,
        final AccessLogPublisher accessLogPublisher
    ) {
        super(serverStatusManager, filterLoader, sessionCtxDecorator, usageNotifier,
            reqCompleteHandler, registry, directMemoryMonitor, eventLoopGroupMetrics,
            discoveryClient, applicationInfoManager, accessLogPublisher);
    }

    @Override
    protected Map<Integer, ChannelInitializer> choosePortsAndChannels(
        final ChannelGroup clientChannels,
        final ChannelConfig channelDependencies
    ) {
        final Map<Integer, ChannelInitializer> portsToChannels = new HashMap<>();

        int port = new DynamicIntProperty(PROPERTY_SERVER_PORT_MAIN, DEFAULT_SERVER_PORT_MAIN).get();

        final ChannelConfig channelConfig = BaseServerStartup.defaultChannelConfig();
        channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen, StripUntrustedProxyHeadersHandler.AllowWhen.ALWAYS);
        channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, false);
        channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
        channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, false);

        portsToChannels.put(port, new ZuulServerChannelInitializer(port, channelConfig, channelDependencies, clientChannels));

        logPortConfigured(port, null);

        return portsToChannels;
    }

}
