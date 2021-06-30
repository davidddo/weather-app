package de.hdm.weatherapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import de.hdm.weatherapp.database.AppDatabase;
import de.hdm.weatherapp.database.dao.CityDao;
import de.hdm.weatherapp.database.entity.CityEntity;
import de.hdm.weatherapp.interfaces.common.Coord;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private AppDatabase database;
    private CityDao cityDao;

    @Before
    public void before() {
        final Context context = ApplicationProvider.getApplicationContext();
        database = AppDatabase.getInstance(context, "weather_app_test");
        cityDao = database.cityDao();
    }

    @After
    public void after() {
        database.clearAllTables();
        database.close();
    }

    @Test
    public void writeAndReadCity() {
        final Coord coord = new Coord();
        coord.lat = 37.666668;
        coord.lon = 55.683334;

        final CityEntity city = new CityEntity(519188, "Novinki", "RU", coord);
        cityDao.insertMany(Collections.singletonList(city));

        final List<CityEntity> cities = cityDao.getAll();
        assertNotNull(cities);
        assertNotEquals(0, cities.size());

        final CityEntity queriedCity = cityDao.getById(519188).getValue();
        assertNotNull(queriedCity);
        assertEquals(queriedCity.name, "Novinki");
        assertEquals(queriedCity.country, "RU");
        assertFalse(queriedCity.saved);
    }

    @Test
    public void saveAndReadFavourites() {
        cityDao.update(519188, true);

        final List<CityEntity> cities = cityDao.getAllFavourites().getValue();
        assertNotNull(cities);
        assertEquals(cities.size(), 1);
        assertTrue(cities.get(0).saved);
    }
}
