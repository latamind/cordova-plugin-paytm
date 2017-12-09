#import "PaytmCordova.h"
#import <Cordova/CDV.h>

@implementation PaytmCordova{
    NSString* callbackId;
    PGTransactionViewController* txnController;
}

- (void)startPayment:(CDVInvokedUrlCommand *)command {
    
    callbackId = command.callbackId;

    NSString *merchant_id  = [command.arguments objectAtIndex:0];
    NSString *cust_id = [command.arguments objectAtIndex:1];
    NSString *channel_id = [command.arguments objectAtIndex:2];
    NSString *industry_type_id = [command.arguments objectAtIndex:3];
    NSString *website = [command.arguments objectAtIndex:4];
    NSString *order_id  = [command.arguments objectAtIndex:5];
    NSString *email = [command.arguments objectAtIndex:6];
    NSString *phone = [command.arguments objectAtIndex:7];
    NSString *txn_amt = [command.arguments objectAtIndex:8];
    NSString *callback_url = [command.arguments objectAtIndex:9];
    NSString *checksum = [command.arguments objectAtIndex:10];
    NSString *is_prod = [command.arguments objectAtIndex:11];

    PGMerchantConfiguration* merchant = [PGMerchantConfiguration defaultConfiguration];
    merchant.merchantID = merchant_id;
    merchant.industryID = industry_type_id;
    merchant.website = website;
    merchant.channelID = channel_id;
    
    //Step 2: Create the order with whatever params you want to add. But make sure that you include the merchant mandatory params
    NSMutableDictionary *orderDict = [NSMutableDictionary new];
    //Merchant configuration in the order object
    orderDict[@"MID"] = merchant_id;
    orderDict[@"ORDER_ID"] = order_id;
    orderDict[@"CUST_ID"] = cust_id;
    orderDict[@"INDUSTRY_TYPE_ID"] = industry_type_id;
    orderDict[@"CHANNEL_ID"] = channel_id;
    orderDict[@"TXN_AMOUNT"] = txn_amt;
    orderDict[@"WEBSITE"] = website;
    orderDict[@"CALLBACK_URL"] = callback_url;
    orderDict[@"CHECKSUMHASH"] = checksum;
    
    PGOrder *order = [PGOrder orderWithParams:orderDict];
    
    txnController = [[PGTransactionViewController alloc] initTransactionForOrder:order];

    if ([is_prod isEqualToString:@"true"])
    {
        txnController.serverType = eServerTypeProduction;
    }
    else
    {
        txnController.serverType = eServerTypeStaging;
    }
    txnController.merchant = merchant;
    txnController.delegate = self;
    txnController.loggingEnabled = true;
    UIViewController *rootVC = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    [rootVC presentViewController:txnController animated:YES completion:nil];
}

//Called when a transaction has completed. response dictionary will be having details about Transaction.
- (void)didSucceedTransaction:(PGTransactionViewController *)controller
                     response:(NSDictionary *)response{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    [txnController dismissViewControllerAnimated:YES completion:nil];
}

//Called when a transaction is failed with any reason. response dictionary will be having details about failed Transaction.
- (void)didFailTransaction:(PGTransactionViewController *)controller
                     error:(NSError *)error
                  response:(NSDictionary *)response{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    [txnController dismissViewControllerAnimated:YES completion:nil];
    
}

//Called when a transaction is Canceled by User. response dictionary will be having details about Canceled Transaction.
- (void)didCancelTransaction:(PGTransactionViewController *)controller
                       error:(NSError *)error
                    response:(NSDictionary *)response{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:response];
    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
    [txnController dismissViewControllerAnimated:YES completion:nil];
}

@end