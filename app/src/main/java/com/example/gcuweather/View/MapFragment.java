package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import android.app.Activity;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gcuweather.R;
import com.example.gcuweather.View.SearchActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private EditText searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            requireContext(), R.raw.map_style_json));

            if (!success) {
                Log.e("MapFragment", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapFragment", "Can't find style. Error: ", e);
        }
        // Add default marker
        LatLng defaultLocation = new LatLng(0, 0); // Default location
        googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Marker"));

        // Move camera to default location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        // Access the search bar from the parent activity
        Activity parentActivity = getActivity();
        if (parentActivity != null && parentActivity instanceof SearchActivity) {
            searchBar = parentActivity.findViewById(R.id.searchBar);
            if (searchBar != null) {
                searchBar.addTextChangedListener(new SearchTextWatcher());
            }
        }
    }

    private class GeocodeTask extends AsyncTask<String, Void, LatLng> {
        @Override
        protected LatLng doInBackground(String... params) {
            String query = params[0];
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(query, 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    return new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LatLng location) {
            if (location != null) {
                // Clear existing markers
                googleMap.clear();
                // Add marker for the searched location
                googleMap.addMarker(new MarkerOptions().position(location).title(searchBar.getText().toString()));
                // Move camera to the searched location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Perform search operation based on the query
            performSearch(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not used
        }
    }

    private void performSearch(String query) {
        new GeocodeTask().execute(query);
    }

    // Lifecycle methods for MapView
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
