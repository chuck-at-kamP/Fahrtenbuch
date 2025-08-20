package com.example.fahrtenbuch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class FBLocation {
    Activity context;
    LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private FBLocationListener listener;

    FBLocation(Activity ctx) {
        context = ctx;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
    public void setListener(FBLocationListener listener) {
        this.listener = listener;
    }
    public double[] getCurrentLocation2() {
        double[] result = new double[2];
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            Log.d("Location","Location Permissions denied!");
            return result;
        }
        Log.d("Location","Get Location!");
        // Fetch the last known location
        Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location1 != null) {
            // Get latitude and longitude
            double lat = location1.getLatitude();
            double lon = location1.getLongitude();
            Log.d("Location","Location! lat=" + lat + ", long=" + lon);
            result[0] = lon;
            result[1] = lat;
        } else {
            Log.d("Location","Unable to get location!");
        }
        Location location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location2 != null) {
            // Get latitude and longitude
            double lat = location2.getLatitude();
            double lon = location2.getLongitude();
            Log.d("Location","Location! lat=" + lat + ", long=" + lon);
            result[0] = lon;
            result[1] = lat;
        } else {
            Log.d("Location","Unable to get location!");
        }
        return result;
    }

    public interface FBLocationListener {
        public void locationChangedStart(double lat, double lon, float acc, double alt);
        public void locationChangedStop(double lat, double lon, float acc, double alt);
        public void locationChangedTimer(double lat, double lon, float acc, double alt);
    }

    public void getCurrentLocation(String order) {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            Log.d("Location","Location Permissions denied!");
        }
        Log.d("FBLocation getCurrentLocation()","Get Location!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(
                        LocationManager.GPS_PROVIDER,
                        null,
                        context.getMainExecutor(),
                        new Consumer<Location>() {
                            @Override
                            public void accept(Location location) {
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();
                                float acc = location.getAccuracy();
                                double alt = location.getAltitude();
                                Log.d("FBLocation getCurrentLocation()","Location! lat=" + lat + ", long=" + lon);
                                if (listener != null) {
                                    if(order.equals("start")) {
                                        listener.locationChangedStart(lat,lon,acc,alt); // <---- fire listener here
                                    }
                                    else if(order.equals("stop")) {
                                        listener.locationChangedStop(lat,lon,acc,alt); // <---- fire listener here
                                    }
                                    else if(order.equals("timer")) {
                                        listener.locationChangedTimer(lat,lon,acc,alt); // <---- fire listener here
                                    }
                                }
                            }
                        });
            }
        }
    }

    public String[] getAddress(double lon, double lat) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            String mess = e.getMessage();
            Log.d("Address", "Cant get address! mess" + mess);
        }
        String addressString = "Unknown";
        String address = "Unknown";
        String city = "Unknown";
        if(addresses.size() > 0) {
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
            String street = addresses.get(0).getSubLocality();
            Log.d("FBLocation getAddress()", "Adress city=" + city + ", zip=" + zip + ", country=" + country + ", street=" + street);
            addressString = address.replace(",", "\n");
        }
        String[] result = { city, address, addressString };
        return result;
    }
}
