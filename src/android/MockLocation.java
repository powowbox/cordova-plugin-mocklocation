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
import android.os.Build;

//
import androidx.core.app.ActivityCompat;

//
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 
 * 
 */
public class MockLocation extends CordovaPlugin {

    /**
     * 
     * 
     */
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private CallbackContext callbackContext;



    /**
     * 
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
        
        if (ActivityCompat.checkSelfPermission(cordova.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            JSONObject result = new JSONObject();
            if(location != null) {

                try {

                    result.put("isMock", isMockLocation(location));
                    result.put("message", "");

                }
                catch (JSONException e) {

                    e.printStackTrace();
                }

                callbackContext.success(result);
            }
            else {

                try {
                    result.put("message", "No last known location available");
                    callbackContext.error(result);
                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }
        else {

            ActivityCompat.requestPermissions(cordova.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
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
}