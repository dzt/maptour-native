package petersoboyejo.com.maptour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinationsActivity extends AppCompatActivity {

        private ArrayList<Location> destinations = new ArrayList();
        private RecyclerView recyclerView;
        private LocationAdapter locationAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_destinations);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Destinations");
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            locationAdapter = new LocationAdapter(destinations);

            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(locationAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    Location location = destinations.get(position);
                    Toast.makeText(getApplicationContext(), location.getName(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("LOCATION_CHOICE", location);
                    startActivity(intent);

                }
                @Override public void onLongClick(View view, int position) {}
            }));

            this.init();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish();
            }
            return super.onOptionsItemSelected(item);
        }

        private void init() {


            SharedPreferences destinationsPreferences = getSharedPreferences("destinationPrefs", MODE_PRIVATE);

            if (destinationsPreferences.contains("destinations")) {

                String destinationsArr = destinationsPreferences.getString("destinations", "");
                JSONArray arr = null;

                try {
                    arr = new JSONArray(destinationsArr);
                    for (int i = 0; i < arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);

                        String name = obj.getString("name");
                        double lat = obj.getDouble("lat");
                        double lon = obj.getDouble("lon");

                        Geocoder geocoder = new Geocoder(DestinationsActivity.this, Locale.getDefault());
                        String url = "";

                        try {
                            List<Address> addresses  = geocoder.getFromLocation(lat,lon, 1);
                            url = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                            Log.d("DEBUG", addresses.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        destinations.add(new Location(name, "https://en.wikipedia.org/wiki/" + url, lat, lon));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            destinations.add(new Location("Alcatraz Island", "https://en.wikipedia.org/wiki/Alcatraz_Island", 37.826667, -122.422778));
            destinations.add(new Location("Golden Gate Bridge", "https://en.wikipedia.org/wiki/Golden_Gate_Bridge", 37.819722, -122.478611));
            destinations.add(new Location("Lincoln Memorial", "https://en.wikipedia.org/wiki/Lincoln_Memorial", 38.889306, -77.050111));
            destinations.add(new Location("Grand Central Terminal", "https://en.wikipedia.org/wiki/Grand_Central_Terminal", 40.752800, -73.976522));

        }
}