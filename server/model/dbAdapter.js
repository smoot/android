// Generated by CoffeeScript 1.10.0
(function() {
  var dbAdapter;

  dbAdapter = (function() {
    var Firebird, _dictionaries, _getChangingValues, _options;

    Firebird = require('node-firebird');

    _options = {};

    _dictionaries = {
      getLocationId: function(locationName) {
        return 33;
      },
      getUserId: function(userName) {
        var i, item, len, ref;
        ref = _dictionaries.user;
        for (i = 0, len = ref.length; i < len; i++) {
          item = ref[i];
          if (item.NAME.toUpperCase() === userName.toUpperCase()) {
            return item.ID;
          }
        }
        console.log("Set default USER:" + _dictionaries.user[0].NAME + " " + _dictionaries.user[0].ID);
        return _dictionaries.user[0].ID;
      }
    };

    function dbAdapter() {
      _options.host = '127.0.0.1';
      _options.port = 3050;
      _options.database = "d:\\svn\\github\\MoneyTracker\\MT.FDB";
      _options.user = 'SYSDBA';
      _options.password = 'masterkey';
      console.log("Create object dbAdapter");
      this.getDictionaries(function(err, data) {
        if (err) {
          throw err;
        }
        _dictionaries.user = data.user;
        _dictionaries.location = data.location;
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
        db.query("SELECT ID, NAME, REMARKS FROM ORGANIZATION", function(err, result) {
          if (err) {
            console.log(err);
            return callback(err);
          }
          data.location = result;
          db.query("SELECT ID, NAME FROM USERMT", function(err, result) {
            if (err) {
              console.log(err);
              return callback(err);
            }
            data.user = result;
            db.detach();
            return callback(null, data);
          });
        });
      });
    };

    _getChangingValues = function(callback) {
      Firebird.attach(_options, function(err, db) {
        if (err) {
          console.log(err);
          db.detach();
          return callback({
            err: err
          });
        }
        db.query("SELECT MAX(ID) FROM EXPENSE", function(err, result) {
          if (err) {
            console.log(err);
            db.detach();
            return callback({
              err: err
            });
          }
          _dictionaries.expense = result[0].MAX + 1;
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
            st = "INSERT INTO EXPENSE (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS) VALUES (?,     ?,            ?,      ?,         1,          2,      1,      0,      0,            1,        0,          NULL,           ?,  'autoinsert')";
            transaction.query(st, [_dictionaries.expense, sms.date, _dictionaries.getUserId(sms.user), sms.coast, sms.coast], function(err, result) {
              if (err) {
                console.log(err);
                db.detach();
                return callback({
                  err: err
                });
              }
              st = "INSERT INTO EXPENSEITEM (ID, QTY, PRICE, REMARKS, EXPENSE, COMM, TOTAL, TRADEPLACE, DISC, TRANSFERDATE, IDX) VALUES (?, 1, ?,          NULL,      ?,    4474,  ?,        ?,    0,  ?, ?)";
              transaction.query(st, [_dictionaries.item, sms.coast, _dictionaries.expense, sms.coast, _dictionaries.getLocationId(sms.location), sms.date, 1], function(err, result) {
                if (err) {
                  console.log(err);
                  db.detach();
                  return callback({
                    err: err
                  });
                }
                st = "UPDATE ACCOUNT SET BALANCE=? WHERE ID=2";
                transaction.query(st, [_dictionaries.balance - parseFloat(sms.coast)], function(err, result) {
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
                    return callback();
                  });
                });
              });
            });
          });
        });
      });
    };

    return dbAdapter;

  })();

  module.exports = dbAdapter;

}).call(this);

//# sourceMappingURL=dbAdapter.js.map
