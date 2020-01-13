package de.struktuhr.gateway.filter;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.text.ParseException;
import java.util.List;

@Component
public class PrincipalGatewayFilterFactory extends AbstractGatewayFilterFactory<PrincipalGatewayFilterFactory.Config> {

    private final static String USER_INFO_HEADER_NAME = "X-USER-INFO";
    private final static String USER_ID_CLAIM_NAME = "user_id";

    public PrincipalGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // leave bad guys out
            firewall(exchange);

            final String userInfo = findUserInfo(exchange);
            if(userInfo != null) {
                final ServerWebExchange mutatedExchange = withUserInfo(exchange, userInfo);
                return chain.filter(mutatedExchange);
            }
            else {
                return chain.filter(exchange);
            }
        };
    }

    private String findUserInfo(ServerWebExchange exchange) {
        final String authorizationHeader = findAuthorizationHeader(exchange);
        if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("bearer")) {
            final String tokenString = authorizationHeader.substring(6);
            try {
                final SignedJWT signedJWT = SignedJWT.parse(tokenString);
                return signedJWT.getJWTClaimsSet().getSubject();
            }
            catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String findAuthorizationHeader(ServerWebExchange exchange) {
        final List<String> tokens = exchange.getRequest().getHeaders().get("Authorization");
        return (tokens != null && !tokens.isEmpty())
                ? tokens.get(0)
                : null;
    }

    /* principal is null (check why)
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // leave bad guys out
            firewall(exchange);
            return exchange.getPrincipal()
                    .cast(JwtAuthenticationToken.class)
                    .map(JwtAuthenticationToken::getToken)
                    .map(Jwt::getClaims)
                    .filter(claims -> claims.containsKey(USER_ID_CLAIM_NAME))
                    .map(claims -> claims.get(USER_ID_CLAIM_NAME))
                    .cast(String.class)
                    .map(userId -> withUserId(exchange, userId))
                    .defaultIfEmpty(exchange)
                    .flatMap(chain::filter);
        };
    }
     */

    private void firewall(ServerWebExchange exchange) {
        if (exchange.getRequest().getHeaders().containsKey(USER_INFO_HEADER_NAME)) {
            throw new IllegalStateException("Malicious Client is trying to submit X-USER-INFO header");
        }
    }

    private ServerWebExchange withUserInfo(ServerWebExchange exchange, String userId) {
        return exchange.mutate()
                .request(r -> r.header(USER_INFO_HEADER_NAME, userId))
                .build();
    }

    public static class Config {

    }
}
