package com.systa.reactive.router;

import com.systa.reactive.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(final ReviewHandler reviewHandler){
        return route()
                .nest(path("/v1/review"), builder -> {
                    builder.POST("", reviewHandler :: addReview)
                           .GET("", reviewHandler :: getReviews)
                           .PUT("/{id}", reviewHandler :: updateReview)
                           .DELETE("/{id}", reviewHandler :: deleteReview);
                })
                .GET("/v1/helloWorld", (request -> ServerResponse.ok().bodyValue("Hello World")))
//                .POST("/v1/review", reviewHandler :: addReview)
//                .GET("/v1/review", reviewHandler :: getReviews)
                .build();
    }

}
