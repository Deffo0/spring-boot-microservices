package com.example.ratingsservice.Repositories;

import com.example.ratingsservice.models.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating, String> {
    List<Rating> findByUserId(String userId);
}