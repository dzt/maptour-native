package petersoboyejo.com.maptour;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private Button mapType_button, destinations_button, myLocation_button;
    private WebView wv;

    private MapView mv;
    private GoogleMap gmap;
    private Location currentDestination;

    private FusedLocationProviderClient client;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        this.init();

        mv.onCreate(mapViewBundle);
        mv.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentDestination = (Location) getIntent().getSerializableExtra("LOCATION_CHOICE");
        }

    }

    private void init() {

        this.requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);

        mapType_button = (Button) findViewById(R.id.mapType_button);
        destinations_button = (Button) findViewById(R.id.destinations_button);
        myLocation_button = (Button) findViewById(R.id.myLocation_button);

        mapType_button.setOnClickListener(this);
        destinations_button.setOnClickListener(this);
        myLocation_button.setOnClickListener(this);

        wv = (WebView) findViewById(R.id.webView);
        wv.setWebViewClient(new MyBrowser());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /* Init Map View */
        mv = (MapView) findViewById(R.id.mapView);

    }

    @Override
    public void onClick(View v) {

        Button _b = (Button) findViewById(v.getId());

        switch (v.getId()) {
            case R.id.mapType_button: this.mapTypeAction();
                Log.d("Button Pressed", _b.getText() + "");
                break;
            case R.id.destinations_button: this.destinationsAction();
                Log.d("Button Pressed", _b.getText() + "");
                break;
            case R.id.myLocation_button: this.myLocationAction();
                Log.d("Button Pressed", _b.getText() + "");
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mv.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mv.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mv.onStop();
    }
    @Override
    protected void onPause() {
        mv.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mv.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        gmap.setIndoorEnabled(true);
        if (currentDestination != null) {
            this.changeDestination(currentDestination.getLatitude(), currentDestination.getLongitude(), currentDestination.getWikiLink());
        }
    }

    private void changeDestination(double latitude, double longitude, String wikiLink) {
        LatLng position = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        gmap.addMarker(new MarkerOptions().position(position)
                .title(currentDestination.getName()));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(position));
        wv.loadUrl(wikiLink);

    }

    private void mapTypeAction() {

    }

    private void destinationsAction() {
        Intent intent = new Intent(getBaseContext(), DestinationsActivity.class);
        this.startActivity(intent);
    }

    private void myLocationAction() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<android.location.Location>() {

            double lat, lon;
            @Override
            public void onSuccess(android.location.Location location) {
                if(location!= null){
                    Log.d("LOCATION", location.toString());
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                    LatLng position = new LatLng(lat, lon);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(position);
                    gmap.addMarker(new MarkerOptions().position(position)
                            .title("Your current location"));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(position));

                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    String url = "";

                    try {
                        List<Address> addresses  = geocoder.getFromLocation(lat,lon, 1);
                        url = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
                        Log.d("DEBUG", addresses.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    wv.loadUrl("https://en.wikipedia.org/wiki/" + url);
                }
            }

        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
