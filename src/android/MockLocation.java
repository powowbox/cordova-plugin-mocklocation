/**
 *
 *
 */
package com.webkokteyli.plugins.mocklocation;

//
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

//
import android.location.Location;
import android.location.LocationManager;

//
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;


/**
 *
 *
 */
public class MockLocation extends CordovaPlugin {

    /**
     *
     *
     */
    private CallbackContext callbackContext;

    //
    int highAccuracyPermissionRequestCode = 1;
    String [] highAccuracyPermissions = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };


    /**
     *
     *
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{

        this.callbackContext = callbackContext;
        
        //
        if (action.equals("isFromMockProvider")) {

            //
            if(hasPermission(highAccuracyPermissions)){

                isFromMockProvider(callbackContext);
                return true;
            }
            else {

                PermissionHelper.requestPermissions(this, highAccuracyPermissionRequestCode, highAccuracyPermissions);
            }

            //
            return true;
        }

        //
        return false;
    }



    /**
     *
     *
     */
    private void isFromMockProvider(CallbackContext callbackContext) throws JSONException{

        //
        JSONObject resultJSON = new JSONObject();

        // Get the location manager
        LocationManager locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){

            //
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){

                //
                boolean isFromMockProvider = (Build.VERSION.SDK_INT < 31) ? location.isFromMockProvider() : location.isMock();

                //
                resultJSON.put("success", true);
                resultJSON.put("message", "");
                resultJSON.put("isFromMockProvider", isFromMockProvider);
            }
            else{

                //
                resultJSON.put("success", false);
                resultJSON.put("message", "\"location\" object not found. (lastKnownLocation may be null)");
                resultJSON.put("errorCode", "LOCATION_OBJ_NOT_FOUND");
            }
        }
        else{

            //
            resultJSON.put("success", false);
            resultJSON.put("message", "\"locationManager\" not found.");
            resultJSON.put("errorCode", "LOCATION_MANAGER_OBJ_NOT_FOUND");
        }

        //
        PluginResult result = new PluginResult(PluginResult.Status.OK, resultJSON);
        callbackContext.sendPluginResult(result);
    }


    /**
     * 
     * 
     */
    public boolean hasPermission(String[] permissions) {

        for(String p : permissions){
            
            if(!PermissionHelper.hasPermission(this, p)){

                return false;
            }
        }

        return true;
    }


    /**
     * 
     * 
     */
    public void requestPermissions(int requestCode){

        PermissionHelper.requestPermissions(this, requestCode, highAccuracyPermissions);
    }


    /**
     *
     *
     */
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)  throws JSONException{

        //
        JSONObject resultJSON = new JSONObject();

        //
        if(requestCode == highAccuracyPermissionRequestCode){

            for (int i = 0; i < permissions.length; i++) {

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    //
                    resultJSON.put("success", false);
                    resultJSON.put("message", "\"Fine Location Access\" not granted.");
                    resultJSON.put("errorCode", "FINE_LOCATION_ACCESS_NOT_GRANTED");

                    //
                    callbackContext.error(resultJSON);
                    return;
                }
            }

            //
            resultJSON.put("success", true);
            resultJSON.put("message", "");

            //
            callbackContext.success(resultJSON);
        }
    }
}