package com.example.android.sunshine.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Simple utility for parsing a JSON response from the open weather API
 */
public class WeatherDataParser {

    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
    throws JSONException {

        JSONObject jsonObject = new JSONObject(weatherJsonStr);
        JSONArray dayArray = jsonObject.getJSONArray("list");
        JSONObject dayEntry = (JSONObject) dayArray.get(dayIndex);
        JSONObject temp = (JSONObject) dayEntry.get("temp");
        return (Double) temp.get("max");
    }
}
