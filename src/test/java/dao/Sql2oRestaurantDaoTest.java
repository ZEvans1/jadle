package dao;

import models.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oRestaurantDaoTest {
    private Connection conn;
//    private Sql2oReviewDao reviewDao;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
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

}