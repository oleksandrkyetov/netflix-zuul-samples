package olky.sample;

import lombok.extern.slf4j.Slf4j;
import olky.sample.dto.Input;
import olky.sample.dto.Output;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/slow/service", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SlowServiceController {

    @RequestMapping(method = RequestMethod.POST, path = "/call")
    public ResponseEntity<Output> call(@RequestBody final Input input) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            LOGGER.debug("Sleeping thread was interrupted");
        }

        return ResponseEntity.ok(new Output("Response " + input.getString()));
    }

}
