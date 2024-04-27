/**
 *
 */
package com.webkokteyli.plugins.mocklocation;

import java.util.List;
import android.os.Build;
import android.content.Context;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

/**
 *
 */
public class MockLocation extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{

        if(action.equals("check")){

            check(callbackContext);
            return true;
        }

        return false;
    }

    private void check(CallbackContext callbackContext) throws JSONException{

        JSONObject resultJSON = new JSONObject();

        LocationManager locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){

            Location location = null;
            String usedProvider = "";
            List<String> providers = locationManager.getProviders(true);

            for(String provider : providers){

                @SuppressLint("MissingPermission")
                Location locationFromProvider = locationManager.getLastKnownLocation(provider);

                if(locationFromProvider == null) {

                    continue;
                }

                if(location == null || locationFromProvider.getAccuracy() < location.getAccuracy()){

                    location = locationFromProvider;
                    usedProvider = provider;
                }
            }

            Log.d("chromium", "[INFO:CONSOLE] Used location provider is: " + usedProvider);

            if(location != null){

                boolean isMock = (Build.VERSION.SDK_INT < 31) ? location.isFromMockProvider() : location.isMock();

                JSONObject data = new JSONObject();
                    data.put("isMock", isMock);

                resultJSON.put("data", data);
            }
            else {

                JSONObject error = new JSONObject();
                    error.put("code", "LOCATION_OBJ_NOT_FOUND");
                    error.put("message", "\"location\" object not found. (lastKnownLocation may be null)");

                resultJSON.put("error", error);
            }
        }
        else{

            JSONObject error = new JSONObject();
                error.put("code", "LOCATION_MANAGER_OBJ_NOT_FOUND");
                error.put("message", "\"locationManager\" not found.");

            resultJSON.put("error", error);
        }

        //
        PluginResult result = new PluginResult(PluginResult.Status.OK, resultJSON);
        callbackContext.sendPluginResult(result);
    }
}