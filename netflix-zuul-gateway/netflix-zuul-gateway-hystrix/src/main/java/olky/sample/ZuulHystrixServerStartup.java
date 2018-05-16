package olky.sample;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.netty.common.accesslog.AccessLogPublisher;
import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.netty.common.metrics.EventLoopGroupMetrics;
import com.netflix.netty.common.status.ServerStatusManager;
import com.netflix.spectator.api.Registry;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.FilterUsageNotifier;
import com.netflix.zuul.RequestCompleteHandler;
import com.netflix.zuul.context.SessionContextDecorator;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.DirectMemoryMonitor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ZuulHystrixServerStartup extends BaseServerStartup {

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
    protected Map<Integer, ChannelInitializer> choosePortsAndChannels(ChannelGroup clientChannels, ChannelConfig channelDependencies) {
        return null;
    }

}
