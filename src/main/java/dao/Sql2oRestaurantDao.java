package dao;

import models.Foodtype;
import models.Restaurant;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oRestaurantDao implements RestaurantDao {

    private final Sql2o sql2o;

    public Sql2oRestaurantDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Restaurant rest){
        String sql = "INSERT INTO restaurants (name, address, zipcode, phone, website, email) VALUES (:name , :address , :zipcode , :phone , :website , :email)";

        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(rest)
                    .executeUpdate()
                    .getKey();
            rest.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Restaurant> getAll(){
        String sql = "SELECT * FROM restaurants";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Restaurant.class);
        }
    }

    @Override
    public Restaurant findById(int id){
        String sql = "SELECT * FROM restaurants WHERE id = :id";
        try (Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Restaurant.class);
        }
    }

    @Override
    public void update(int id, String name, String address, String zipcode, String phone, String website, String email){
        String sql = "UPDATE restaurants SET (name, address, zipcode, phone, website, email) = (:name, :address, :zipcode, :phone, :email)";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("address",  address)
                    .addParameter("zipcode", zipcode)
                    .addParameter("phone", phone)
                    .addParameter("website", website)
                    .addParameter("email", email)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteById(int id){
        String sql = "DELETE from restaurants WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addRestaurantToFoodtype(Restaurant restaurant, Foodtype foodtype) {
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
    public List<Foodtype> getAllFoodtypesForARestaurant(int restaurantId) {
        ArrayList<Foodtype> foodtypes = new ArrayList<>();

        String joinQuery = "SELECT foodtypeid FROM restaurants_foodtypes WHERE restaurantid = :restaurantid";

        try (Connection con = sql2o.open()) {
            List<Integer> allFoodtypeIds = con.createQuery(joinQuery)
                    .addParameter("restaurantid",restaurantId)
                    .executeAndFetch(Integer.class);
            for(Integer foodtypeId : allFoodtypeIds) {
                String foodtypeQuery = "SELECT * FROM foodtypes WHERE id = :foodtypeid";
                foodtypes.add(
                        con.createQuery(foodtypeQuery)
                                .addParameter("foodtypeid", foodtypeId)
                                .executeAndFetchFirst(Foodtype.class));
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        return foodtypes;
    }
}

