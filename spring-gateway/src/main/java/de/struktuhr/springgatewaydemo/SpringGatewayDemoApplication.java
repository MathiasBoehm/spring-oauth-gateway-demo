package de.struktuhr.springgatewaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringGatewayDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringGatewayDemoApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder, ApiKeyGatewayFilterFactory apiKeyGatewayFilterFactory) {
		return builder.routes()
			.route(r-> r
				.path("/personservice/**")
				.filters(f -> f.rewritePath("/personservice(?<segment>/?.*)", "$\\{segment}"))
				.uri("http://localhost:9091"))
			.route(r-> r
					.path("/orderservice/**")
					.filters(f -> f
							.filter(apiKeyGatewayFilterFactory.apply(new ApiKeyGatewayFilterFactory.Config("123~456")))
							.rewritePath("/orderservice(?<segment>/?.*)", "$\\{segment}"))
					.uri("http://localhost:9092"))
			.build();
	}




}
