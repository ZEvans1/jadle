package dao;

import models.Restaurant;
import models.Review;
import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;


public class Sql2oReviewDaoTest {

    private Connection conn;
    private Sql2oReviewDao reviewDao;
    private Sql2oRestaurantDao restaurantDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        reviewDao = new Sql2oReviewDao(sql2o);
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingReviewSetsId() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Captain Kirk", "food coma!", 3, testRestaurant.getId());
        reviewDao.add(testReview);
        assertEquals(1, testReview.getId());
    }

    @Test
    public void getAllReviewsByRestaurant() throws Exception {

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant newRestaurant = setupRestaurant(); //add in some extra data to see if it interferes
        restaurantDao.add(newRestaurant);

        Review testReview = new Review("Captain Kirk", "food coma!", 3, testRestaurant.getId());
        reviewDao.add(testReview);

        Review otherReview = new Review("Mr. Spock", "passable", 1, testRestaurant.getId());
        reviewDao.add(otherReview);


        assertEquals(2, reviewDao.getAllReviewsByRestaurant(testRestaurant.getId()).size());
        assertEquals(0, reviewDao.getAllReviewsByRestaurant(newRestaurant.getId()).size()); //why is this a good idea as a safety check?
    }

    @Test
    public void timeStampIsReturnedCorrectly() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Cap", "food", 3, testRestaurant.getId());
        reviewDao.add(testReview);

        long creationTime = testReview.getCreatedat();
        long savedTime = reviewDao.getAll().get(0).getCreatedat();

        String formattedCreationTime = testReview.getFormattedCreatedAt();
        String formattedSavedTime = reviewDao.getAll().get(0).getFormattedCreatedAt();
        assertEquals(formattedCreationTime, formattedSavedTime);
        assertEquals(creationTime, reviewDao.getAll().get(0).getCreatedat());
    }

    @Test
    public void reviewsAreReturnedInCorrectOrder() throws Exception {
        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        Review testReview = new Review("Kirk", "ok", 5, testRestaurant.getId());
        reviewDao.add(testReview);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Review testSecondReview = new Review("Spock", "passable", 9, testRestaurant.getId());
        reviewDao.add(testSecondReview);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        Review testThirdReview = new Review("Scotty", "bloody good grub!", 4, testRestaurant.getId());
        reviewDao.add(testThirdReview);

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }

        Review testFourthReview = new Review("Mr. Sulu", "I prefer home cooking", 2, testRestaurant.getId());
        reviewDao.add(testFourthReview);

        assertEquals(4, reviewDao.getAllReviewsByRestaurant(testRestaurant.getId()).size()); //it is important we verify that the list is the same size.
        assertEquals("I prefer home cooking", reviewDao.getAllReviewsByRestaurantSortedNewestToOldest(testRestaurant.getId()).get(0).getContent());
    }

    public Restaurant setupRestaurant() {
        return new Restaurant("Fish Witch", "214 NE Broadway", "97232", "503-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
    }

}