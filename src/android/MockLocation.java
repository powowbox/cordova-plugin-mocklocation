/**
 *
 *
 */
package com.webkokteyli.plugins.mocklocation;

//
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

//
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

//
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 *
 *
 */
public class MockLocation extends CordovaPlugin implements LocationListener {

    /**
     *
     *
     */
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private CallbackContext callbackContext;


    /**
     *
     *
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        this.callbackContext = callbackContext;

        if (action.equals("check")) {

            getLastKnownLocation();
            return true;
        }

        return false;
    }



    /**
     *
     *
     */
    private void getLastKnownLocation() {

        // Request location permission
        if (ActivityCompat.checkSelfPermission(cordova.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(cordova.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // Get the location manager
        LocationManager locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);

        //
        JSONObject result = new JSONObject();

        // Get the last known location
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        // Register the location listener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Check if the last known location is null
        if(lastLocation != null) {

            // Remove location updates from the location listener
            locationManager.removeUpdates(this);

            try {

                result.put("isMock", isMockLocation(lastLocation));
                result.put("message", "");

            }
            catch (JSONException e) {

                e.printStackTrace();
            }

            callbackContext.success(result);
        }
        else {

            // Request a single update
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        }
    }



    /**
     *
     *
     */
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastKnownLocation();
            }
            else {

                JSONObject result = new JSONObject();
                try {
                    result.put("message", "Location permission denied");
                    callbackContext.error(result);
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
    }



    /**
     *
     *
     */
    private boolean isMockLocation(Location location) {

        boolean isMock;

        if (Build.VERSION.SDK_INT >= 31) {

            isMock = location.isMock();
        }
        else {

            isMock = location.isFromMockProvider();
        }

        return isMock;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        getLastKnownLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}