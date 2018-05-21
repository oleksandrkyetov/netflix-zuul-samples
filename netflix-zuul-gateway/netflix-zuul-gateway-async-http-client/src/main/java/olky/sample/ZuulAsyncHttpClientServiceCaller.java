package olky.sample;

import com.google.gson.Gson;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import javax.inject.Singleton;
import olky.sample.dto.Input;
import olky.sample.dto.Output;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.extras.rxjava.AsyncHttpObservable;
import rx.Observable;
import rx.functions.Func0;

@Singleton
public class ZuulAsyncHttpClientServiceCaller {

    private static final Gson GSON = new Gson();

    public Observable<HttpResponseMessage> call(final HttpRequestMessage httpRequestMessage) {
        final Input input = new Input("Zuul + AsyncHttpClient");
        final Func0<BoundRequestBuilder> func0 = () -> new DefaultAsyncHttpClient()
            .preparePost("http://localhost:8080/slow/service/call").setBody(GSON.toJson(input));

        return AsyncHttpObservable.observe(func0)
            .map(response -> {
                final HttpResponseMessage httpResponseMessage = new HttpResponseMessageImpl(
                    httpRequestMessage.getContext(), httpRequestMessage, response.getStatusCode());
                if (response.getStatusCode() == 200) {
                    httpResponseMessage.setBodyAsText(GSON.fromJson(response.getResponseBody(), Output.class).getString());
                }

                return httpResponseMessage;
            });
    }
}
