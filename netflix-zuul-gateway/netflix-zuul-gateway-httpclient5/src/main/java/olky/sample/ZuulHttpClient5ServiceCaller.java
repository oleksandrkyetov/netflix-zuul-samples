package olky.sample;

import com.google.gson.Gson;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import javax.inject.Singleton;
import olky.sample.dto.Input;
import olky.sample.dto.Output;
import org.apache.hc.client5.http.fluent.Async;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import rx.Observable;

@Singleton
public class ZuulHttpClient5ServiceCaller {

    private static final Gson GSON = new Gson();

    public Observable<HttpResponseMessage> call(final HttpRequestMessage httpRequestMessage) {
        final Input input = new Input("Zuul + HttpClient5");

        final Request request = Request.create("POST", "http://localhost:8080/slow/service/call")
            .body(new StringEntity(GSON.toJson(input), ContentType.APPLICATION_JSON));

        return Observable.just(Async.newInstance().execute(request, new BasicHttpClientResponseHandler()))
            .map(future -> {
                try {
                    return future.get();
                } catch (Exception e) {
                    return "Exception";
                }
            })
            .map(response -> {
                final HttpResponseMessage httpResponseMessage = new HttpResponseMessageImpl(
                    httpRequestMessage.getContext(), httpRequestMessage, 200);
                httpResponseMessage.setBodyAsText(GSON.fromJson(response, Output.class).getString());

                return httpResponseMessage;
            });
    }

}
