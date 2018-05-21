package olky.sample.filters.endpoint

import com.netflix.zuul.filters.http.HttpAsyncEndpoint
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.message.http.HttpResponseMessage
import olky.sample.ZuulHttpClient5ServiceCaller
import rx.Observable

import javax.inject.Inject

class ServiceCallerEndpoint extends HttpAsyncEndpoint {

    private final ZuulHttpClient5ServiceCaller zuulHttpClient5ServiceCaller;

    @Inject
    ServiceCallerEndpoint(ZuulHttpClient5ServiceCaller zuulHttpClient5ServiceCaller) {
        this.zuulHttpClient5ServiceCaller = zuulHttpClient5ServiceCaller
    }

    @Override
    Observable<HttpResponseMessage> applyAsync(final HttpRequestMessage httpRequestMessage) {
        return zuulHttpClient5ServiceCaller.call(httpRequestMessage)
    }

}
