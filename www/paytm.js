module.exports = {
  startPayment: function (merchantId, customerId, channelId, industryTypeId, website, orderId, amount, callbackUrl, checksum, isProd, successCallback, failureCallback) {
    cordova.exec(successCallback,
      failureCallback,
      "Paytm",
      "startPayment", [merchantId, customerId, channelId, industryTypeId, website, orderId, amount, callbackUrl, checksum, isProd]);
  }
};