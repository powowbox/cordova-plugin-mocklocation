#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVInvokedUrlCommand.h>

@interface MockLocation : CDVPlugin

- (void)check:(CDVInvokedUrlCommand*)command;

@end
