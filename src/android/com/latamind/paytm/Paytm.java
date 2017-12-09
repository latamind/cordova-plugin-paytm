package com.latamind.paytm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.paytm.pgsdk.*;

public class Paytm extends CordovaPlugin {

    private PaytmPGService paytm_service;

    protected void pluginInitialize() {
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        if (action.equals("startPayment")) {
            //merchant_id, cust_id, channel_id, industry_type_id, website, order_id, email, phone, txn_amt, callback_url, checksum, is_prod
            startPayment(args.getString(0), args.getString(1), args.getString(2), args.getString(3), args.getString(4), args.getString(5), args.getString(6), args.getString(7), args.getString(8), args.getString(9), args.getString(10), args.getString(11), callbackContext);
            return true;
        }
        return false;
    }

    private void startPayment(
                            final String merchant_id,
                            final String cust_id,
                            final String channel_id,
                            final String industry_type_id,
                            final String website,
                            final String order_id,
                            final String email,
                            final String phone,
                            final String txn_amt,
                            final String callback_url,
                            final String checksum,
                            final String is_prod,
                            final CallbackContext callbackContext){

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", merchant_id);
        paramMap.put("CUST_ID", cust_id);
        paramMap.put("CHANNEL_ID", channel_id);
        paramMap.put("INDUSTRY_TYPE_ID", industry_type_id);
        paramMap.put("WEBSITE", website);
        paramMap.put("ORDER_ID", order_id);
        paramMap.put("EMAIL", email);
        paramMap.put("MOBILE_NO", phone);
        paramMap.put("TXN_AMOUNT", txn_amt);
		paramMap.put("CALLBACK_URL" , callback_url);
        paramMap.put("CHECKSUMHASH" , checksum);

        /*paytm_service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", order_id);
        paramMap.put("MID", PAYTM_MERCHANT_ID);
        paramMap.put("CUST_ID", cust_id);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE_ID);
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("TXN_AMOUNT", txn_amt);
        //paramMap.put("EMAIL", email);
        //paramMap.put("MOBILE_NO", phone);
        paramMap.put("THEME", "merchant");

        paramMap.put("StudentId", "152");
        paramMap.put("SchoolId", "36");
        paramMap.put("SessionId", "17");
        
        PaytmOrder order = new PaytmOrder(paramMap);
        PaytmMerchant merchant = new PaytmMerchant(this.PAYTM_GENERATE_URL, this.PAYTM_VALIDATE_URL);

        this.paytm_service.initialize(order, merchant, null);
        */

        

		/*//Kindly create complete Map and checksum on your server side and then put it here in paramMap.

		Map<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", "101010");
        paramMap.put("MID", "DIY12386817555501617");
        paramMap.put("CUST_ID", "152");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "DIYtestingwap");
        //paramMap.put("EMAIL", "sanjaycedti@gmail.com");
        paramMap.put("TXN_AMOUNT", "2");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
		paramMap.put("CHECKSUMHASH" , "KYxoCeSALyiW+N/2UQDT6U0Qa2tgknNSVhM+PVZoTomfZG6ZjFBIpUj9Jn27gpfYNZEBctCdlBEXY+gzc0EOKnXWnD9QSwlMot6JNkYXL18=");
        */

        PaytmOrder order = new PaytmOrder(paramMap);
        
        if(is_prod.equals("1")){
            this.paytm_service = PaytmPGService.getProductionService();
        }else{
            this.paytm_service = PaytmPGService.getStagingService();
        }

        this.paytm_service.initialize(order, null);
        
        this.paytm_service.startPaymentTransaction(cordova.getActivity(), false, false, new PaytmPaymentTransactionCallback()
        {

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                // Some UI Error Occurred in Payment Gateway Activity.
                // // This may be due to initialization of views in
                // Payment Gateway Activity or may be due to //
                // initialization of webview. // Error Message details
                // the error occurred.
                Log.i("Paytm::Error","someUIErrorOccurred :"+inErrorMessage);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
            }

            @Override
            public void onTransactionResponse(Bundle bundle) {
                Log.d("Paytm::LOG", "Payment Transaction : " + bundle);
                //Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                //Bundle[{STATUS=TXN_SUCCESS, CHECKSUMHASH=qx79XWS5M5eC3KevGapM5FBNEuaYkgrLdKD+dL+vJf4OVOnP37c7FHe1p1yMEtEb2JRjfpRvP/R2Vq5C+y/VazFXglJCrGzrSzoMP4EbS/Q=, BANKNAME=, ORDERID=101010, TXNAMOUNT=2.00, TXNDATE=2017-09-09 20:23:44.0, MID=DIY12386817555501617, TXNID=70000224957, RESPCODE=01, PAYMENTMODE=PPI, BANKTXNID=890887, CURRENCY=INR, GATEWAYNAME=WALLET, RESPMSG=Txn Successful.}]


                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, Paytm.this.getJson(bundle)));
            }

            @Override
            public void networkNotAvailable() {
                // If network is not
                // available, then this
                // method gets called.
                Log.i("Paytm::Error","networkNotAvailable");
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                // This method gets called if client authentication
                // failed. // Failure may be due to following reasons //
                // 1. Server error or downtime. // 2. Server unable to
                // generate checksum or checksum response is not in
                // proper format. // 3. Server failed to authenticate
                // that client. That is value of payt_STATUS is 2. //
                // Error Message describes the reason for failure.
                Log.i("Paytm::Error","clientAuthenticationFailed :"+inErrorMessage);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));

            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode,
                    String inErrorMessage, String inFailingUrl) {

                Log.i("Paytm::Error","onErrorLoadingWebPage iniErrorCode  :"+iniErrorCode);
                Log.i("Paytm::Error","onErrorLoadingWebPage inErrorMessage  :"+inErrorMessage);
                Log.i("Paytm::Error","onErrorLoadingWebPage inFailingUrl  :"+inFailingUrl);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
            }

            // had to be added: NOTE
            @Override
            public void onBackPressedCancelTransaction() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Log.d("Paytm::LOG", "Payment Transaction Failed " + inErrorMessage);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, 0));
            }

        });
    }

    private JSONObject getJson(final Bundle bundle) {
        if (bundle == null) return null;
        JSONObject jsonObject = new JSONObject();

        for (String key : bundle.keySet()) {
            Object obj = bundle.get(key);
            try {
                jsonObject.put(key, wrap(bundle.get(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;//.toString();
    }

    public static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return toJSONArray(o);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static JSONArray toJSONArray(Object array) throws JSONException {
        JSONArray result = new JSONArray();
        if (!array.getClass().isArray()) {
            throw new JSONException("Not a primitive array: " + array.getClass());
        }
        final int length = Array.getLength(array);
        for (int i = 0; i < length; ++i) {
            result.put(wrap(Array.get(array, i)));
        }
        return result;
    }
}