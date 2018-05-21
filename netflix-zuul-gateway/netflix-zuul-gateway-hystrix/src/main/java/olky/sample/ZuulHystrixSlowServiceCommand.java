package olky.sample;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import java.io.InputStreamReader;
import olky.sample.dto.Input;
import olky.sample.dto.Output;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ZuulHystrixSlowServiceCommand extends HystrixObservableCommand<HttpResponseMessage> {

    private static final Gson GSON = new Gson();

    private final HttpRequestMessage httpRequestMessage;

    public ZuulHystrixSlowServiceCommand(final HttpRequestMessage httpRequestMessage) {
        super(HystrixCommandGroupKey.Factory.asKey("ZuulHystrixSlowServiceGroup"));
        this.httpRequestMessage = httpRequestMessage;
    }

    @Override
    protected Observable<HttpResponseMessage> construct() {
        return Observable.create((Subscriber<? super HttpResponseMessage> observer) -> {
            try {
                if (!observer.isUnsubscribed()) {
                    final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
                    final HttpPost httpPost = new HttpPost("http://localhost:8080/slow/service/call");

                    final Input input = new Input("Zuul 2 + Hystrix Test");
                    final StringEntity stringEntity = new StringEntity(GSON.toJson(input));
                    stringEntity.setContentType("application/json");

                    httpPost.setEntity(stringEntity);

                    final HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
                    final Output output = GSON.fromJson(new InputStreamReader(httpResponse.getEntity().getContent()), Output.class);

                    final HttpResponseMessage httpResponseMessage = new HttpResponseMessageImpl(httpRequestMessage.getContext(),
                        httpRequestMessage, 200);
                    httpResponseMessage.setBodyAsText(output.getString());

                    observer.onNext(httpResponseMessage);
                    observer.onCompleted();
                }
            } catch (Exception e) {
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

}
