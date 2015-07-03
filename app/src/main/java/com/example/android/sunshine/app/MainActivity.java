package com.example.android.sunshine.app;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Main activity for the app. Creates a
 * {@link com.example.android.sunshine.app.MainActivity.PlaceholderFragment} containing a view
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
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
    }
}
