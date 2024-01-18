/**
 * 
 * 
 */
var mocklocation = {

    isFromMockProvider: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'MockLocation', 'isFromMockProvider', []);
    }
}

/**
 * 
 * 
 */
cordova.addConstructor(function () {

    if (!window.plugins){
        window.plugins = {};
    }

    window.plugins.mocklocation = mocklocation;
    return window.plugins.mocklocation;
});