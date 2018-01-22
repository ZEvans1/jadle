package dao;

import models.Foodtype;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;


public class Sql2oFoodtypeDaoTest {
    private Connection conn;
    private Sql2oFoodtypeDao foodtypeDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        foodtypeDao = new Sql2oFoodtypeDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Foodtype setupFoodtype() {
        return new Foodtype("Thai");
    }

    @Test
    public void addFoodtypeId() {
        Foodtype testFoodtype = setupFoodtype();
        foodtypeDao.add(testFoodtype);
        assertEquals(1, testFoodtype.getId());
    }

    @Test
    public void getsAllFoodTypes() {
        Foodtype testFoodtype = setupFoodtype();
        Foodtype testFoodtype2 = setupFoodtype();
        foodtypeDao.add(testFoodtype);
        foodtypeDao.add(testFoodtype2);
        assertEquals(2, foodtypeDao.getAll().size());

    }

    @Test
    public void deletesAFoodtype() {
        Foodtype testFoodtype = setupFoodtype();
        foodtypeDao.add(testFoodtype);
        foodtypeDao.deleteById(testFoodtype.getId());
        assertEquals(0, foodtypeDao.getAll().size());
    }

}