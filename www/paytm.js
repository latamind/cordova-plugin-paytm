  module.exports = {
    startPayment: function (merchantId, customerId, channelId, industryTypeId, website, orderId, email, phone, amount, callbackUrl, checksum, isProd, successCallback, failureCallback) {
      cordova.exec(successCallback,
        failureCallback,
        "Paytm",
        "startPayment", [merchantId, customerId, channelId, industryTypeId, website, orderId, email, phone, amount, callbackUrl, checksum, isProd]);
    }
  };