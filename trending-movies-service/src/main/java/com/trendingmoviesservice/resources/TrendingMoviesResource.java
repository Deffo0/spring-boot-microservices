package com.trendingmoviesservice.resources;

import com.trendingmoviesservice.models.CatalogItem;
import com.trendingmoviesservice.models.Rating;
import com.trendingmoviesservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.trendingmoviesservice.proto.Movie;
import com.trendingmoviesservice.proto.Reply;
import com.trendingmoviesservice.proto.Request;
import com.trendingmoviesservice.proto.Movie.*;
import com.trendingmoviesservice.proto.TopMoviesGrpc.TopMoviesImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class TrendingMoviesResource extends TopMoviesImplBase{
    
    @Override
    public void getTrending(Request request, StreamObserver<Reply> responseObserver) {
        System.out.println("server");
        Movie movie = Movie.newBuilder().setMovieId("mov1").setName("sdsd").setOrder(2).setDescription("xxxss").build(); 
        Reply reply = Reply.newBuilder()
                .addMovies(movie)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
