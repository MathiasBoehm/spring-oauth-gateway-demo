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
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
			.route(r-> r
				.path("/service1/**")
				.filters(f -> f.rewritePath("/service1(?<segment>/?.*)", "$\\{segment}"))
				.uri("http://localhost:9091"))
				.build();
	}
}
