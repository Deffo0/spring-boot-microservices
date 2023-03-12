package com.moviecatalogservice.services;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.springframework.stereotype.Service;

import com.trendingmoviesservice.proto.*;
import com.trendingmoviesservice.proto.TopMoviesGrpc.TopMoviesBlockingStub;

import java.util.List;

@Service
public class TrendingMoviesService {

    private final TopMoviesBlockingStub myServiceStub;

    public TrendingMoviesService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8884)
                .usePlaintext()
                .build();
        myServiceStub = TopMoviesGrpc.newBlockingStub(channel);
    }

    public List<Movie> receiveTrending(int ntop) {
        System.out.println("client");
        return myServiceStub.getTrending(Request.newBuilder().setNtop(ntop).build()).getMoviesList();
    }
}

