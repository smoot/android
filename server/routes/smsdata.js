// Generated by CoffeeScript 1.10.0
(function() {
  var express, router;

  express = require('express');

  router = express.Router();

  router.post('/*', function(req, res, next) {
    var err;
    if (req === null || req === "" || req === void 0) {
      err = new Error('Bad request');
      err.status = 400;
      return next(err);
    }
    return next();
  });

  router.post('/', function(req, res, next) {
    smsData.push(req.body, function(err, balance) {
      if (err) {
        err = new Error('POST request error');
        err.status = 400;
        return next(err);
      }
      res.write(JSON.stringify(balance));
      res.statusCode = 200;
      return res.send();
    });
  });

  router.get('/list', function(req, res, next) {
    smsData.getListStringify(function(req) {
      return res.render('index', {
        title: 'Express',
        body: req
      });
    });
  });

  router.get('/balance', function(req, res, next) {
    db.getBalance(function(err, balance) {
      var balance1;
      if (err) {
        err = new Error('GET request error');
        err.status = 400;
        return next(err);
      }
      balance1 = [
        {
          name: "ТКС",
          balance: 5558
        }, {
          name: "Кошелек МАКС",
          balance: 2456
        }
      ];
      res.write(JSON.stringify(balance));
      res.statusCode = 200;
      return res.send();
    });
  });

  module.exports = router;

}).call(this);

//# sourceMappingURL=smsdata.js.map
