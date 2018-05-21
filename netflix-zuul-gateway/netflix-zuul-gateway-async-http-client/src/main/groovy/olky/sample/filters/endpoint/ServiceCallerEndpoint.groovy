package olky.sample.filters.endpoint

import com.netflix.zuul.filters.http.HttpAsyncEndpoint
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.message.http.HttpResponseMessage
import olky.sample.ZuulAsyncHttpClientServiceCaller
import rx.Observable

import javax.inject.Inject

class ServiceCallerEndpoint extends HttpAsyncEndpoint {

    private final ZuulAsyncHttpClientServiceCaller zuulAsyncHttpClientServiceCaller;

    @Inject
    ServiceCallerEndpoint(ZuulAsyncHttpClientServiceCaller zuulAsyncHttpClientServiceCaller) {
        this.zuulAsyncHttpClientServiceCaller = zuulAsyncHttpClientServiceCaller
    }

    @Override
    Observable<HttpResponseMessage> applyAsync(final HttpRequestMessage httpRequestMessage) {
        return zuulAsyncHttpClientServiceCaller.call(httpRequestMessage)
    }

}
