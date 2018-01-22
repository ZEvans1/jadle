package dao;


import models.Foodtype;
import models.Restaurant;

import java.util.List;

public interface FoodtypeDao {

    void add(Foodtype foodtype);
    void addFoodtypeToRestaurant(Foodtype foodtype, Restaurant restaurant);

    List<Foodtype> getAll();
    List<Restaurant> getAllRestaurantsForAFoodtype(int id);


    void deleteById(int id);
}
