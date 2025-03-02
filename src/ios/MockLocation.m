#import <CoreLocation/CoreLocation.h>

- (BOOL)check:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;

    if (!@available(iOS 15.0, *)) {
      CLLocationManager *locationManager = [[CLLocationManager alloc] init];
      CLLocation *location = locationManager.location;
      
      BOOL isSimulatedBySoftware = location.sourceInformation.isSimulatedBySoftware;
      BOOL isProducedByAccessory = location.sourceInformation.isProducedByAccessory;
      BOOL isSimulated = isSimulatedBySoftware || isProducedByAccessory;
      pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBoolean:isSimulated];
    } else {
      [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
       pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBoolean:NO];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
