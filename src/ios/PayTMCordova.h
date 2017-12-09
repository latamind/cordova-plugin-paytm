
#import <UIKit/UIKit.h>
#import "PaymentsSDK.h"
#import <Cordova/CDV.h>

@interface PaytmCordova : CDVPlugin <PGTransactionDelegate>

- (void)startPayment:(CDVInvokedUrlCommand*)command;

@end
