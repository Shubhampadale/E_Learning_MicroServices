package com.gateway.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${category.service.url}")
    private String categoryServiceUrl;

    @Value("${course.service.url}")
    private String courseServiceUrl;

    @Value("${video.service.url}")
    private String videoServiceUrl;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){

        return routeLocatorBuilder.routes()
                .route(r->
                        r.path("/category/**")
                                .filters(
                                        f->f.rewritePath("/category/?(?<segment>.*)","/${segment}"))
                                .uri(categoryServiceUrl)
                )
                .route(r->
                        r.path("/course/**")
                                .filters(
                                        f->f.rewritePath("/course/?(?<segment>.*)","/${segment}"))
                                .uri(courseServiceUrl)
                )
                .route(r->
                        r.path("/video/**")
                                .filters(
                                        f->f.rewritePath("/video/?(?<segment>.*)","/${segment}"))
                                .uri(videoServiceUrl)
                )
                .build();
    }
}
