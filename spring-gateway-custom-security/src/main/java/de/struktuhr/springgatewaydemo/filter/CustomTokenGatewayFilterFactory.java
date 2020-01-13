package de.struktuhr.springgatewaydemo.filter;

import com.nimbusds.jwt.SignedJWT;
import de.struktuhr.springgatewaydemo.token.CustomTokenHelper;
import de.struktuhr.springgatewaydemo.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class CustomTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomTokenGatewayFilterFactory.Config> {

    private final static String TOKEN_HEADER_NAME = "X-CUSTOM-TOKEN";
    private final static String USER_INFO_HEADER_NAME = "X-USER-INFO";

    private final Logger logger = LoggerFactory.getLogger(CustomTokenGatewayFilterFactory.class);

    @Autowired
    private TokenService tokenService;

    public CustomTokenGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(CustomTokenGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            // leave bad guys out
            firewall(exchange);

            final String tokenString = findTokenString(exchange);
            if (tokenString != null) {
                // Parse and verify
                final SignedJWT signedJWT = tokenService.parseJWT(tokenString);
                if (tokenService.verify(signedJWT)) {
                    // TODO check expiration in real implementation

                    // Enhance Request
                    final String userInfo = CustomTokenHelper.buildUserInfo(signedJWT);
                    final ServerWebExchange mutatedExchange = withUserInfo(exchange, userInfo);
                    return chain.filter(mutatedExchange);
                }
                else {
                    logger.warn("Token has wrong signature");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            else {
                logger.warn("No Token provided");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config {
        // No Fields
    }

    private ServerWebExchange withUserInfo(ServerWebExchange exchange, String userInfo) {
        return exchange.mutate()
                .request(r -> r.header(USER_INFO_HEADER_NAME, userInfo))
                .build();
    }

    private String findTokenString(ServerWebExchange exchange) {
        final List<String> tokens = exchange.getRequest().getHeaders().get(TOKEN_HEADER_NAME);
        return (tokens != null && !tokens.isEmpty())
                ? tokens.get(0)
                : null;
    }

    private void firewall(ServerWebExchange exchange) {
        if (exchange.getRequest().getHeaders().containsKey(USER_INFO_HEADER_NAME)) {
            throw new IllegalStateException("Malicious Client is trying to submit X-USER-INFO header");
        }
    }
}
