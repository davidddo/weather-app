package de.hdm.weatherapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.interfaces.common.Coord;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DatabaseTest {

    private static AppDatabase database;
    private static CityDao cityDao;

    @BeforeClass
    public static void before() {
        final Context context = ApplicationProvider.getApplicationContext();
        database = AppDatabase.getInstance(context, "weather_app_test");
        cityDao = database.cityDao();
    }

    @AfterClass
    public static void after() throws InterruptedException {
        database.clearAllTables();
        Thread.sleep(3000);
        database.close();
    }

    @Test
    public void writeAndReadCity() {
        final CityEntity city = new CityEntity(519188, "Novinki", "RU", new Coord(37.666668, 55.683334));
        cityDao.insertMany(Collections.singletonList(city));

        final List<CityEntity> cities = cityDao.getAll();
        assertNotNull(cities);
        assertNotEquals(0, cities.size());

        final CityEntity queriedCity = cityDao.getByName("Novinki");
        assertNotNull(queriedCity);
        assertEquals(queriedCity.name, "Novinki");
        assertEquals(queriedCity.country, "RU");
        assertFalse(queriedCity.saved);
    }

    @Test
    public void saveAndReadFavourites() throws InterruptedException {
        cityDao.update(519188, true);

        Thread.sleep(1000);

        final List<CityEntity> cities = cityDao.getAllSaved();
        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertTrue(cities.get(0).saved);
    }
}
