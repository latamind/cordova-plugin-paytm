Installation
============

```
cordova plugin add https://github.com/latamind/cordova-plugin-paytm.git
```

Usage
=====

```
window.cordova.paytm.startPayment(merchant_id, customer_id, channel_id, industry_type_id, website, order_id, email, mobile_no, amount, callback_url, checksum, isProd, successCallback, failureCallback);
```