package dao;


import models.Foodtype;

import java.util.List;

public interface FoodtypeDao {

    void add(Foodtype foodtype);

    List<Foodtype> getAll();
    // List<Restaurant> getAllRestaurantsForAFoodtype(int id); //E we will implement this soon.

    //

    void deleteById(int id);
}
