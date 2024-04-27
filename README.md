# cordova-plugin-mocklocation

A Cordova plugin to check if current location is mocked with mock/fake location programs. **Only works on Android platform** and returns `isMocked()` result.

Plugin gets `lastKnownLocation()` from one of the avaliable location providers (passive, network etc.) and uses `isMocked()` for  >= API 31, `isFromMockProvider()` for < API 30.

Should work > API 24. (Android 7).

### Return

- Returns `success: true` and `isMock: true/false` if location fetch is successful.
- Returns `success: false` and `error.code: xx, error.message: xx` if location fetch is unsuccessful.

## Example

```js
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {

  async function run(){

    let mockResult = await checkMockLocation();
    if(typeof mockResult.error === 'undefined'){
      if(!mockResult.data.isMock){
        // not mocked
      }
      else{
        // mocked
      }
    }
    else{
      console.log(mockResult.error.code);
      console.log(mockResult.error.message);
    }

    async function checkMockLocation(){
      if(device.platform != 'Android'){
        return false;
      }
      else{
        return new Promise((onSuccess, onError) => {
            if(typeof window.plugins.mocklocation.check !== 'undefined'){
              window.plugins.mocklocation.check(onSuccess, onError);
            }
            else{
              console.log('Plugin not found: plugins.mocklocation.check()');
            }
        });
      }
    }
  }

  //
  run();
}
```