package olky.sample;

import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.message.http.HttpResponseMessage;
import com.netflix.zuul.message.http.HttpResponseMessageImpl;
import javax.inject.Singleton;
import olky.sample.dto.Input;
import olky.sample.dto.Output;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import rx.Observable;

@Singleton
public class ZuulSpringWebfluxServiceCaller {

    public Observable<HttpResponseMessage> call(final HttpRequestMessage httpRequestMessage) {
        return Observable.just(WebClient.create().method(HttpMethod.POST)
            .uri("http://localhost:8080/slow/service/call")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(Mono.just(new Input("Zuul + Spring Webflux")), Input.class))
            .retrieve().bodyToMono(Output.class)
            .map(output -> {
                final HttpResponseMessage httpResponseMessage = new HttpResponseMessageImpl(
                    httpRequestMessage.getContext(), httpRequestMessage, 200);
                httpResponseMessage.setBodyAsText(output.getString());
                return httpResponseMessage;
            }).block());
    }

}
