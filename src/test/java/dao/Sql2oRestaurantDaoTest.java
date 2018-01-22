package dao;

import models.Foodtype;
import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oRestaurantDaoTest {
    private Connection conn;
//    private Sql2oReviewDao reviewDao;
    private Sql2oRestaurantDao restaurantDao;
    private Sql2oFoodtypeDao foodtypeDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }

    public Foodtype setupFoodtype() {
        return new Foodtype("Thai");
    }

    public Foodtype setupAnotherFoodtype() {
        return new Foodtype("BBQ");
    }

    @Test
    public void addRestaurantSetsId(){
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        assertEquals(1, testRestaurant.getId());
    }

    @Test
    public void addedRestaurantsAreReturnedFromGetAll() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void noRestaurantsReturnsEmptyList() throws Exception {
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void deleteByIdDeletesCorrectRestaurant() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.deleteById(testRestaurant.getId());
        assertEquals(0, restaurantDao.getAll().size());
    }

    @Test
    public void getAllFoodtypesForArestaurantReturnsFoodtypesCorrectly() throws Exception {
        Foodtype testFoodtype = new Foodtype("Seafood");
        foodtypeDao.add(testFoodtype);

        Foodtype testFoodtypeTwo = new Foodtype("Food");
        foodtypeDao.add(testFoodtypeTwo);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.addRestaurantToFoodtype(testRestaurant, testFoodtype);
        restaurantDao.addRestaurantToFoodtype(testRestaurant, testFoodtypeTwo);

        Foodtype[] foodtypes = {testFoodtype, testFoodtypeTwo};

        assertEquals(restaurantDao.getAllFoodtypesForARestaurant(testRestaurant.getId()), Arrays.asList(foodtypes));
    }

    @Test
    public void deleteingFoodtypeAlsoUpdatesJoinTable() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Foodtype testFoodtype = setupFoodtype();
        Foodtype testFoodtypeTwo = setupAnotherFoodtype();
        foodtypeDao.add(testFoodtype);
        foodtypeDao.add(testFoodtypeTwo);

        foodtypeDao.addFoodtypeToRestaurant(testFoodtype, testRestaurant);
        foodtypeDao.addFoodtypeToRestaurant(testFoodtypeTwo, testRestaurant);

        foodtypeDao.deleteById(testFoodtype.getId());
        assertEquals(0, foodtypeDao.getAllRestaurantsForAFoodtype(testFoodtype.getId()).size());
    }

}