package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    private RestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    public MovieResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        // Get the movie info from TMDB
        Query query = new Query(Criteria.where("movieId").is(movieId));
        Optional<Movie> cachedMovie = Optional.ofNullable(mongoTemplate.findOne(query, Movie.class));
        if (cachedMovie.isPresent()) {
            // return the cached movie
            return new Movie(
                    cachedMovie.get().getMovieId(),
                    cachedMovie.get().getName(),
                    cachedMovie.get().getDescription()
            );
        } else {
            final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
            Optional<MovieSummary> movieSummary = Optional.ofNullable(restTemplate.getForObject(url, MovieSummary.class));
            if(movieSummary.isPresent()){
                Movie movieCache = new Movie(
                        movieSummary.get().getId(),
                        movieSummary.get().getTitle(),
                        movieSummary.get().getOverview()
                );
                System.out.println(movieCache.getName());
                mongoTemplate.save(movieCache);
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                return new Movie(movieId, movieSummary.get().getTitle(), movieSummary.get().getOverview());
            }


        }
        return null;
    }
}
