# cordova-plugin-mocklocation

A Cordova plugin to check if locations are mocked. Only works for Android platform.

## How does it work

Plugin gets "lastKnownLocation()" and uses "isMocked()" for  >= API 31, "isFromMockProvider()" for < API 30.

This plugin uses "lastKnownLocation()" data, so, you MAY WANT to get location first via another plugin like "cordova-plugin-geolocation".

Should work > API 24. (Android 7).

## Usage

```js
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {

  // get geolocation data first
  var watchID = navigator.geolocation.getCurrentPosition(onSuccess, onError);

  // geolocation success (system has recent location data)
  function onSuccess(){

    // check if mocked
    window.plugins.mocklocation.check(successCallback, errorCallback);

    // check success
    function successCallback(result) {

      console.log(result);

      // result = {
      //   isMock: true / false,
      //   message: "",
      // }
    }

    // error (permission errors etc.)
    function errorCallback(error) {

      console.log(error);

      // error = {
      //   message: "Lorem ipsum",
      // }
    }
  }

}
```