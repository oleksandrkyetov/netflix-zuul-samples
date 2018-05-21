package olky.sample.filters.endpoint

import com.netflix.zuul.filters.http.HttpAsyncEndpoint
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.message.http.HttpResponseMessage
import olky.sample.ZuulSpringWebfluxServiceCaller
import rx.Observable

import javax.inject.Inject

class ServiceCallerEndpoint extends HttpAsyncEndpoint {

    private final ZuulSpringWebfluxServiceCaller zuulSpringWebfluxServiceCaller;

    @Inject
    ServiceCallerEndpoint(ZuulSpringWebfluxServiceCaller zuulSpringWebfluxServiceCaller) {
        this.zuulSpringWebfluxServiceCaller = zuulSpringWebfluxServiceCaller
    }

    @Override
    Observable<HttpResponseMessage> applyAsync(final HttpRequestMessage httpRequestMessage) {
        return zuulSpringWebfluxServiceCaller.call(httpRequestMessage)
    }

}
