package olky.sample.filters.endpoint

import com.netflix.zuul.filters.http.HttpAsyncEndpoint
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.message.http.HttpResponseMessage
import olky.sample.ZuulHystrixSlowServiceCommand
import rx.Observable

class ServiceCallerEndpoint extends HttpAsyncEndpoint {

    @Override
    Observable<HttpResponseMessage> applyAsync(final HttpRequestMessage httpRequestMessage) {
        return new ZuulHystrixSlowServiceCommand(http).observe();
    }

}
