package de.struktuhr.gateway.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        try {
            Map<String, String> map = toMap(response.body().asReader());
            String message = map.get("message");
            return new ResponseStatusException(HttpStatus.resolve(response.status()), message);
        } catch (Exception e) {
            return new RuntimeException(e.getMessage());
        }
    }


    private Map<String, String> toMap(Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(reader, Map.class);
        return map;
    }
}
