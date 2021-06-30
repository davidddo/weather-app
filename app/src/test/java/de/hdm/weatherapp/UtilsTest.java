package de.hdm.weatherapp;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hdm.weatherapp.utils.Utils;

public class UtilsTest {

    @Test
    public void testConvertWeatherCodeToIconId() {
        final int actual = Utils.getWeatherIcon(211);
        final int expected = 2131165295;

        assertEquals("Converts the weather code to the corresponding icon id", expected, actual);
    }

    @Test
    public void testFormatTimestampToDate() {
        final String actual = Utils.formatDateTime(1625030292, "HH:mm");
        final String expected = "07:18";

        assertEquals("Converts a timestamp to the given format", expected, actual);
    }
}
