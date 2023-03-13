package com.trendingmoviesservice.resources;

import com.trendingmoviesservice.models.CatalogItem;
import com.trendingmoviesservice.models.Rating;
import com.trendingmoviesservice.models.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.trendingmoviesservice.proto.Movie;
import com.trendingmoviesservice.proto.Reply;
import com.trendingmoviesservice.proto.Request;
import com.trendingmoviesservice.proto.Movie.*;
import com.trendingmoviesservice.proto.TopMoviesGrpc.TopMoviesImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.ArrayList;

@GrpcService
public class TrendingMoviesResource extends TopMoviesImplBase{
    
    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void getTrending(Request request, StreamObserver<Reply> responseObserver) {
        String sql = "select distinct id, name, descp, rating from movie JOIN rating on rating.movie_id=movie.id ORDER BY rating DESC;";
        jdbcTemplate.setDataSource(dataSource);
        List<Map<String, Object>> movies = jdbcTemplate.queryForList(sql);
        System.out.println("movies");
        System.out.println(movies);
        System.out.println("server");
        List<Movie> moviesToRespond = new ArrayList<>();
        for (Map<String, Object> movie : movies) {
            Movie movieTemp = Movie.newBuilder()
                    .setMovieId(movie.get("id").toString())
                    .setOrder(moviesToRespond.size())
                    .setName(movie.get("name").toString())
                    .setDescription(movie.get("descp").toString())
                    .build();
            System.out.println(movieTemp.getName());

            moviesToRespond.add(movieTemp);
        }
        System.out.println(moviesToRespond.get(1).getName());
        Reply reply = Reply.newBuilder()
                .addAllMovies(moviesToRespond)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
