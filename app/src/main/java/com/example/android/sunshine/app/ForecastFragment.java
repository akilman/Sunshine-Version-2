package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public final String LOG_TAG = ForecastFragment.class.getSimpleName();

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // obtain the root view via the 'main' fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // create some 'entries' to display
        final int nEntries = 15;
        List<String> objects = new ArrayList<>();
        for (int i = 0; i < nEntries; i++) {
            objects.add(UUID.randomUUID().toString());
        }

        // create the array adapter for the ListView
        FragmentActivity activity = getActivity();
        int resource = R.layout.list_item_forecast;
        int textViewResourceId = R.id.list_item_forecast_textview;
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(activity, resource, textViewResourceId, objects);

        // associate adapter to ListView
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(arrayAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            Void params = null;
            AsyncTask<Void, Void, String> asyncTask = new FetchWeatherTask().execute(params);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Instance of an {@link AsyncTask} for fetching the weather
     */
    class FetchWeatherTask extends AsyncTask<Void, Void, String> {

        public final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String doInBackground(Void... params) {
            return fetchForecast();
        }

        /**
         * Helper method to return the forecast of a given zipcode in JSON format
         */
        public String fetchForecast() {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                int zipCode = 94089;
                String mode = "json";
                String units = "metric";
                int nDays = 7;
                String urlFormat = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%d&mode=%s&units=%s&cnt=%d";
                String urlString = String.format(urlFormat, zipCode, mode, units, nDays);
                URL url = new URL(urlString);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.i(LOG_TAG, "Connected!");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }

            return forecastJsonStr;
        }
    }

}