# cordova-plugin-mocklocation

A Cordova plugin to check if current location is mocked with mock/fake location programs. **Only works on Android platform** and returns `isMocked()` result.

Plugin gets `lastKnownLocation()` from one of the avaliable location providers (passive, network etc.) and uses `isMocked()` for  >= API 31, `isFromMockProvider()` for < API 30.

For IOS 15+, use CLLocationManager see: https://stackoverflow.com/a/78190700/9459302

Should work > API 24. (Android 7) and iOS 15+.

### Changes

Add ios 15+ support


## Example

```js
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  window.plugins.mocklocation.check(
    isMockedBoolean => console.log(`isMocked: ${isMockedBoolean}`),  
    errorString => console.error(errorString));
}
```
