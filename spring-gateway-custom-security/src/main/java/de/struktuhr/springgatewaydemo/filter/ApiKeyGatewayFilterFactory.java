package de.struktuhr.springgatewaydemo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiKeyGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiKeyGatewayFilterFactory.Config> {

    private final Logger logger = LoggerFactory.getLogger(ApiKeyGatewayFilterFactory.class);

    public ApiKeyGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Find API Key in Request
            final String apiKey = findApiKey(exchange);
            if (apiKey == null) {
                logger.info("No API Key provided");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            else {
                // Is API key valid?
                if (getConfiguredApiKeys(config).contains(apiKey)) {
                    return chain.filter(exchange);
                }
                else {
                    logger.warn("Invalid API Key " + apiKey);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("apiKeys");
    }

    private String findApiKey(ServerWebExchange exchange) {
        final List<String> apiKeys = exchange.getRequest().getHeaders().get("X-API-KEY");
        return (apiKeys != null && !apiKeys.isEmpty())
                ? apiKeys.get(0)
                : null;
    }


    private List<String> getConfiguredApiKeys(Config config) {
        final String[] arr = config.getApiKeys().split("~");
        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public static class Config {
        // ~ delimited keys
        String apiKeys;

        public Config(String apiKeys) {
            this.apiKeys = apiKeys;
        }

        public Config() {
        }

        public String getApiKeys() {
            return apiKeys;
        }

        public void setApiKeys(String apiKeys) {
            this.apiKeys = apiKeys;
        }
    }
}
