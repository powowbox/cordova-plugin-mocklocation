# cordova-plugin-mocklocation

A Cordova plugin to check if current location is mocked with mock/fake location programs. **Only works on Android platform** and returns `isMocked()` result.

Plugin gets `lastKnownLocation()` from one of the avaliable location providers (passive, network etc.) and uses `isMocked()` for  >= API 31, `isFromMockProvider()` for < API 30.

For IOS 15+, use CLLocationManager see: https://stackoverflow.com/a/78190700/9459302

Should work > API 24. (Android 7) and iOS 15+.

### Changes

Add ios 15+ support


### Return

- Returns `success: true` and `isMock: true/false` if location fetch is successful.
- Returns `success: false` and `error.code: xx, error.message: xx` if location fetch is unsuccessful.

## Example

```js
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {

  async function run(){

  window.plugins.mocklocation.check(
    isMocked => console.log(`isMocked: ${$isMocked}`),  
    error => {
      console.error(error.code);
      console.error(error.message);
    });
  }

  //
  run();
}
```
