// Generated by CoffeeScript 1.10.0
(function() {
  var dbAdapter;

  dbAdapter = (function() {
    var Firebird, _defaultCommodity, _defaultLocation, _dictionaries, _getChangingValues, _options;

    Firebird = require('node-firebird');

    _defaultCommodity = "Непредвиденные расходы";

    _defaultLocation = "Прочее";

    _options = {};

    _dictionaries = {
      getLocationId: function(locationName) {
        var commodity, i, item, j, len, len1, ref, ref1;
        console.log(locationName);
        ref = _dictionaries.location;
        for (i = 0, len = ref.length; i < len; i++) {
          item = ref[i];
          if (item.FULLNAME !== null) {
            if (locationName.toUpperCase().indexOf(item.FULLNAME.toUpperCase()) > -1) {
              ref1 = _dictionaries.commodity;
              for (j = 0, len1 = ref1.length; j < len1; j++) {
                commodity = ref1[j];
                if (commodity.NAME !== null && item.REMARKS !== null) {
                  if ((commodity.NAME.toUpperCase().indexOf(item.REMARKS.toUpperCase())) > -1) {
                    console.log("item.FULLNAME is" + item.FULLNAME);
                    console.log("item.REMARKS is" + item.REMARKS);
                    console.log("commodity.NAME is" + commodity.NAME);
                    return {
                      location: item.ID,
                      commodity: commodity.ID
                    };
                  }
                }
              }
              return {
                location: item.ID,
                commodity: _dictionaries.defaultCommodityId
              };
            }
          }
        }
        console.log("Set default LOCATION: " + _dictionaries.defaultLocationId + " and COMMODITY: " + _dictionaries.defaultCommodityId);
        return {
          location: _dictionaries.defaultLocationId,
          commodity: _dictionaries.defaultCommodityId
        };
      },
      getUserId: function(userName) {
        var i, item, len, ref;
        ref = _dictionaries.user;
        for (i = 0, len = ref.length; i < len; i++) {
          item = ref[i];
          if (item.NAME !== null && (userName.toUpperCase().indexOf(item.NAME.toUpperCase()) > -1)) {
            return item.ID;
          }
        }
        console.log("Set default USER: " + _dictionaries.user[0].NAME + " " + _dictionaries.user[0].ID);
        return _dictionaries.user[0].ID;
      }
    };

    function dbAdapter() {
      _options.host = '127.0.0.1';
      _options.port = 3050;
      _options.database = "/home/user/temp/MT.FDB";
      _options.user = 'SYSDBA';
      _options.password = 'masterkey';
      console.log("Create object dbAdapter");
      this.getDictionaries(function(err, data) {
        var i, item, j, len, len1, ref, ref1;
        if (err) {
          throw err;
        }
        _dictionaries.user = data.user;
        _dictionaries.location = data.location;
        _dictionaries.commodity = data.commodity;
        ref = _dictionaries.location;
        for (i = 0, len = ref.length; i < len; i++) {
          item = ref[i];
          if (item.NAME !== null && (item.NAME.toUpperCase() === _defaultLocation.toUpperCase())) {
            _dictionaries.defaultLocationId = item.ID;
          }
        }
        ref1 = _dictionaries.commodity;
        for (j = 0, len1 = ref1.length; j < len1; j++) {
          item = ref1[j];
          if (item.NAME !== null && (item.NAME.toUpperCase() === _defaultCommodity.toUpperCase())) {
            _dictionaries.defaultCommodityId = item.ID;
          }
        }
      });
    }

    dbAdapter.prototype.getDictionaries = function(callback) {
      Firebird.attach(_options, function(err, db) {
        var data;
        data = {};
        if (err) {
          console.log(err);
          return callback(err);
        }
        db.query("SELECT ID, NAME, FULLNAME, REMARKS FROM ORGANIZATION", function(err, result) {
          if (err) {
            console.log(err);
            db.detach();
            return callback(err);
          }
          data.location = result;
          db.query("SELECT ID, NAME FROM USERMT", function(err, result) {
            if (err) {
              console.log(err);
              db.detach();
              return callback(err);
            }
            data.user = result;
            return db.query("SELECT ID, NAME FROM COMMODITY", function(err, result) {
              if (err) {
                console.log(err);
                db.detach();
                return callback(err);
              }
              data.commodity = result;
              db.detach();
              return callback(null, data);
            });
          });
        });
      });
    };

    _getChangingValues = function(callback) {
      Firebird.attach(_options, function(err, db) {
        if (err) {
          console.log(err);
          return callback({
            err: err
          });
        }
        db.query("SELECT NEXT VALUE FOR IDGEN FROM RDB$DATABASE;", function(err, result) {
          if (err) {
            console.log(err);
            db.detach();
            return callback({
              err: err
            });
          }
          _dictionaries.expense = result[0].GEN_ID;
          console.log("EXPENSE ID is " + result[0].GEN_ID);
          db.query("SELECT MAX(ID) FROM EXPENSEITEM", function(err, result) {
            if (err) {
              console.log(err);
              db.detach();
              return callback({
                err: err
              });
            }
            _dictionaries.item = result[0].MAX + 1;
            db.query("SELECT BALANCE FROM ACCOUNT WHERE ID=2", function(err, result) {
              if (err) {
                console.log(err);
                db.detach();
                return callback({
                  err: err
                });
              }
              _dictionaries.balance = result[0].BALANCE;
              db.detach();
              return callback();
            });
          });
        });
      });
    };

    dbAdapter.prototype.addExpense = function(sms, callback) {
      _getChangingValues(function(err) {
        if (err) {
          console.log(err);
          db.detach();
          return callback({
            err: err
          });
        }
        Firebird.attach(_options, function(err, db) {
          if (err) {
            db.detach();
            return callback({
              err: err
            });
          }
          db.transaction(Firebird.ISOLATION_READ_COMMITED, function(err, transaction) {
            var st;
            if (err) {
              console.log(err);
              db.detach();
              return callback({
                err: err
              });
            }
            st = "INSERT INTO EXPENSE (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS) VALUES (?,     ?,            ?,      ?,         1,          2,      1,      0,      0,            1,        0,          NULL,           ?,  ?)";
            transaction.query(st, [_dictionaries.expense, sms.date, _dictionaries.getUserId(sms.user), sms.coast, sms.coast, 'autoinsert ' + sms.location], function(err, result) {
              if (err) {
                console.log(err);
                db.detach();
                return callback({
                  err: err
                });
              }
              st = "INSERT INTO EXPENSEITEM (ID, QTY, PRICE, REMARKS, EXPENSE, COMM, TOTAL, TRADEPLACE, DISC, TRANSFERDATE, IDX) VALUES (?, 1, ?,          NULL,      ?,    ?,  ?,        ?,    0,  ?, ?)";
              transaction.query(st, [_dictionaries.item, sms.coast, _dictionaries.expense, _dictionaries.getLocationId(sms.location).commodity, sms.coast, _dictionaries.getLocationId(sms.location).location, sms.date, 1], function(err, result) {
                var balance;
                if (err) {
                  console.log(err);
                  db.detach();
                  return callback({
                    err: err
                  });
                }
                balance = {
                  old: _dictionaries.balance,
                  coast: parseFloat(sms.coast),
                  "new": null
                };
                balance["new"] = balance.old - balance.coast;
                st = "UPDATE ACCOUNT SET BALANCE=? WHERE ID=2";
                transaction.query(st, [balance["new"]], function(err, result) {
                  if (err) {
                    console.log(err);
                    db.detach();
                    return callback({
                      err: err
                    });
                  }
                  transaction.commit(function(err) {
                    if (err) {
                      transaction.rollback();
                      console.log(err);
                      db.detach();
                      return callback({
                        err: err
                      });
                    }
                    db.detach();
                    console.log('Send ' + JSON.stringify(sms) + ' to DB');
                    console.log('Balance is ' + (_dictionaries.balance - parseFloat(sms.coast)));
                    return callback(null, {
                      balance: balance
                    });
                  });
                });
              });
            });
          });
        });
      });
    };

    dbAdapter.prototype.getBalance = function(callback) {
      Firebird.attach(_options, function(err, db) {
        var balance;
        balance = {};
        if (err) {
          console.log(err);
          return callback(err);
        }
        db.query("SELECT NAME, BALANCE FROM ACCOUNT", function(err, result) {
          if (err) {
            console.log(err);
            db.detach();
            return callback(err);
          }
          balance = result;
          db.detach();
          return callback(null, balance);
        });
      });
    };

    return dbAdapter;

  })();

  module.exports = dbAdapter;

}).call(this);

//# sourceMappingURL=dbAdapter.js.map
