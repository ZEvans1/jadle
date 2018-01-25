package dao;


import models.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oReviewDao implements ReviewDao {

    private final Sql2o sql2o;

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) {

        String sql = "INSERT INTO reviews (writtenBy, content, rating, restaurantId, createdat) VALUES (:writtenBy, :content, :rating, :restaurantId, :createdat)";

        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(review)
                    .executeUpdate()
                    .getKey();
            review.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from reviews WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Review> getAllReviewsByRestaurant(int restaurantId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reviews WHERE restaurantId = :restaurantId")
                    .addParameter("restaurantId", restaurantId)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> getAll(){
        try (Connection con =sql2o.open()){
            return con.createQuery("SELECT * FROM reviews")
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId) {
        List<Review> unsortedReviews = getAllReviewsByRestaurant(restaurantId);
        //loop through all unsorted reviews
        for (int i = 0; i <= unsortedReviews.size(); i++ ) {

        }
        //use the compareTo method defined in the Review class to see if older or newer
        //switch review order if necessary, older reviews move behind newer reviews
        List<Review> sortedReviews = unsortedReviews;
        return sortedReviews;
    }


}