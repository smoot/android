class dbAdapter
  Firebird = require('node-firebird')
  _options = {}

  _dictionaries = {
    getLocationId: (locationName) ->
#      ToDo Find ID from locationName
      return 33
    getUserId: (userName) ->
      (if item.NAME.toUpperCase() == userName.toUpperCase()
        return item.ID
      ) for item in _dictionaries.user
      console.log "Set default USER:" + _dictionaries.user[0].NAME + " " +_dictionaries.user[0].ID
      return _dictionaries.user[0].ID
  }

  constructor: () ->
    _options.host = '127.0.0.1'
    _options.port = 3050
    _options.database = "d:\\svn\\github\\MoneyTracker\\MT.FDB"
    #  options.database = "d:\\svn\\github\\MoneyTracker\\test\\MT.FDB"
    _options.user = 'SYSDBA'
    _options.password = 'masterkey'
    console.log "Create object dbAdapter"
    @getDictionaries (err, data) ->
      if (err)
        throw err
      _dictionaries.user = data.user
      _dictionaries.location = data.location
      return

  getDictionaries: (callback) ->
    Firebird.attach _options, (err, db) ->
      data = {}
      if (err)
        console.log err
        return callback(err)
      db.query "SELECT ID, NAME, REMARKS FROM ORGANIZATION", (err, result) ->
        if (err)
          console.log err
          return callback(err)
        data.location = result
        db.query "SELECT ID, NAME FROM USERMT", (err, result) ->
          if (err)
            console.log err
            return callback(err)
          data.user = result
          db.detach()
          return callback(null, data)
        return
      return
    return

  _getChangingValues = (callback) ->
    Firebird.attach _options, (err, db) ->
      if (err)
        console.log err
        db.detach()
        return callback({err: err})
      db.query "SELECT MAX(ID) FROM EXPENSE", (err, result) ->
        if (err)
          console.log err
          db.detach()
          return callback({err: err})
        _dictionaries.expense = result[0].MAX + 1
        db.query "SELECT MAX(ID) FROM EXPENSEITEM", (err, result) ->
          if (err)
            console.log err
            db.detach()
            return callback({err: err})
          _dictionaries.item = result[0].MAX + 1
#          ToDo Mocking SELECT ONLY TKC
          db.query "SELECT BALANCE FROM ACCOUNT WHERE ID=2", (err, result) ->
            if (err)
              console.log err
              db.detach()
              return callback({err: err})
            _dictionaries.balance = result[0].BALANCE
            db.detach()
            return callback()
          return
        return
      return
    return

  addExpense: (sms, callback) ->
    _getChangingValues (err) ->
      if (err)
        console.log err
        db.detach()
        return callback({err: err})
      Firebird.attach _options, (err, db) ->
        if (err)
          db.detach()
          return callback({err: err})
        db.transaction Firebird.ISOLATION_READ_COMMITED, (err, transaction) ->
          if (err)
            console.log err
            db.detach()
            return callback({err: err})

          st = "INSERT INTO EXPENSE
          (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS) VALUES
          (?,     ?,            ?,      ?,         1,          2,      1,      0,      0,            1,        0,          NULL,           ?,  'autoinsert')"
          transaction.query st, [_dictionaries.expense, sms.date, _dictionaries.getUserId(sms.user),
            sms.coast, sms.coast], (err, result) ->
            if (err)
              console.log err
              db.detach()
              return callback({err: err})

            st = "INSERT INTO EXPENSEITEM
                          (ID, QTY, PRICE, REMARKS, EXPENSE, COMM, TOTAL, TRADEPLACE, DISC, TRANSFERDATE, IDX) VALUES
                          (?, 1, ?,          NULL,      ?,    4474,  ?,        ?,    0,  ?, ?)"
            transaction.query st, [_dictionaries.item, sms.coast, _dictionaries.expense, sms.coast,
              _dictionaries.getLocationId(sms.location), sms.date, 1], (err, result) ->
              if (err)
                console.log err
                db.detach()
                return callback({err: err})

              st = "UPDATE ACCOUNT SET BALANCE=? WHERE ID=2"
              transaction.query st, [_dictionaries.balance - parseFloat(sms.coast)], (err, result) ->
                if (err)
                  console.log err
                  db.detach()
                  return callback({err: err})
                transaction.commit (err)->
                  if (err)
                    transaction.rollback()
                    console.log err
                    db.detach()
                    return callback({err: err})
                  db.detach()
                  console.log 'Send ' + JSON.stringify(sms) + ' to DB'
                  console.log 'Balance is ' + (_dictionaries.balance - parseFloat(sms.coast))
                  return callback()
                return
              return
            return
          return
        return
      return
    return


module.exports = dbAdapter