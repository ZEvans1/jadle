import com.google.gson.Gson;
import dao.Sql2oFoodtypeDao;
import dao.Sql2oRestaurantDao;
import dao.Sql2oReviewDao;
import exceptions.ApiException;
import models.Foodtype;
import models.Restaurant;
import models.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        Sql2oFoodtypeDao foodtypeDao;
        Sql2oRestaurantDao restaurantDao;
        Sql2oReviewDao reviewDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:h2:~/jadle.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        conn = sql2o.open();

        //process a new restaurant
        post("/restaurants/new", "application/json", (req, res) -> {
            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurantDao.add(restaurant);
            res.status(201);
            return "The following was added to the API\n\r" + gson.toJson(restaurant);//send it back to be displayed
        });

        //show all restaurants
        get("/restaurants", "application/json", (req, res) -> {
            if(restaurantDao.getAll().size() > 0) {
                return gson.toJson(restaurantDao.getAll());
            } else {
                return "{\"message\":\"No restaurants in database.\"}";
            }
        });

        //all food types
        get("/foodtypes", "application/json", (req, res) -> {
            return gson.toJson(foodtypeDao.getAll());
        });


        //new food type
        post("/foodtypes/new", "application/json", (req,res) -> {
            Foodtype foodtype = gson.fromJson(req.body(), Foodtype.class);
            foodtypeDao.add(foodtype);
            res.status(201);
            return gson.toJson(foodtype);
        });

        post("/restaurants/:restaurantId/reviews/new", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("restaurantId"));
            Review review = gson.fromJson(req.body(), Review.class);
            review.setRestaurantId(restaurantId);
            reviewDao.add(review);
            res.status(201);
            return gson.toJson(review);
        });

        //see reviews for a restaurant
        get("/restaurants/:id/reviews", "application/json", (req, res)-> {
           int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            List<Review> reviews;
            reviews = reviewDao.getAllReviewsByRestaurant(restaurantId);
           return gson.toJson(reviews);
        });



        get("/restaurants/:id", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            if (restaurantToFind == null) {
                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            return gson.toJson(restaurantToFind);
        });

        exception(ApiException.class, (exc, req, res) -> {
            ApiException err = (ApiException) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatusCode());
            res.body(gson.toJson(jsonMap));
        });

        //Filters
        after((req, res) -> {
            res.type("application/json");
        });
    }
}