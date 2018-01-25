package dao;


import models.Foodtype;
import models.Restaurant;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Sql2oFoodtypeDao implements FoodtypeDao {

    private final Sql2o sql2o;
    public Sql2oFoodtypeDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Foodtype foodtype) {
        String sql = "INSERT INTO foodtypes (name) VALUES (:name)";

        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(foodtype)
                    .executeUpdate()
                    .getKey();
            foodtype.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Foodtype> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM foodtypes")
                    .executeAndFetch(Foodtype.class);
        }
    }

    @Override
    public Foodtype findById(int id) {
        String sql = "SELECT * FROM foodtypes WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Foodtype.class);
        }
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE from foodtypes WHERE id = :id";
        String deleteJoin = "DELETE from restaurants_foodtypes WHERE foodtypeid = :foodtypeid";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("foodtypeid",id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addFoodtypeToRestaurant (Foodtype foodtype, Restaurant restaurant) {
        String sql = "INSERT INTO restaurants_foodtypes (restaurantid, foodtypeid) VALUES (:restaurantid, :foodtypeid)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("restaurantid", restaurant.getId())
                    .addParameter("foodtypeid", foodtype.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public List<Restaurant> getAllRestaurantsForAFoodtype(int foodtypeId) {

        ArrayList<Restaurant> restaurants = new ArrayList<>();

        String joinQuery = "SELECT restaurantid FROM restaurants_foodtypes WHERE foodtypeid = :foodtypeid";

        try (Connection con = sql2o.open()) {
            List<Integer> allRestaurantIds = con.createQuery(joinQuery)
                    .addParameter("foodtypeid",foodtypeId)
                    .executeAndFetch(Integer.class);
            for(Integer restaurantid : allRestaurantIds) {
                String restaurantQuery = "SELECT * FROM restaurants WHERE id = :restaurantid";
                restaurants.add(
                        con.createQuery(restaurantQuery)
                        .addParameter("restaurantid", restaurantid)
                        .executeAndFetchFirst(Restaurant.class));
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    return restaurants;
    }


}
