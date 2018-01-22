package dao;

import models.Review;

import java.util.List;

public interface ReviewDao {

    void add(Review review);

    List<Review> getAllReviewsByRestaurant(int restaurantId);

    List<Review> getAll();

    //

    void deleteById(int id);

}
