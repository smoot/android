class db
  Firebird = require('node-firebird')
  options = {}
  options.host = '127.0.0.1'
  options.port = 3050
  options.database = "d:\\svn\\github\\MoneyTracker\\MT.FDB"
  #  options.database = "d:\\svn\\github\\MoneyTracker\\test\\MT.FDB"
  options.user = 'SYSDBA'
  options.password = 'masterkey'

  constructor: () ->
    console.log "Create object dbAdapter"

  send: (list, callback) ->
    commit = @commit
    (@prepareStatement obj, (st) ->
      console.log st.statement
      if (st.err)
        error = "Can't send object to db"
        console.log error
        return callback({err: error})
      else
        commit obj, (err) ->
          if (err)
            error = "Commit to db failed"
            console.log error
            return callback({err: error})
          else
            return callback()
        return) for obj in list
    return

  prepareStatement: (obj, callback) ->
    if (true)
      return callback({statement: 'INSERT AAA'})
    else
      error = 'Prepare statement error'
      console.log error
      return callback({err: error})

  commit: (obj, callback) ->
    if true
      return callback()
    else
      error = "Commit error"
      console.log error
      return callback({err: error})

  fbExec: () ->
    Firebird.attach options, (err, db) ->
      if (err)
        throw err
      db.query 'SELECT * FROM ACCOUNT', (err, result) ->
        if (err)
          throw err
        console.log result
        db.detach()

  fbInsert: ->
    Firebird.attach options, (err, db) ->
      if (err)
        throw  err
      st = [
        "INSERT INTO EXPENSE
        (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS)
        VALUES (3, '15-FEB-2016', 258, 1410, 1, 257, 257, 0, 0, 1, 0, NULL, 1410, NULL)",
        "INSERT INTO EXPENSE
        (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS)
        VALUES (1, '15-FEB-2016', 258, 1410, 1, 257, 257, 0, 0, 1, 0, NULL, 1410, NULL)"]
      db.query st[0], (err, result) ->
        if (err)
          throw err
        console.log result
        db.detach()

  fbExpense: (sms) ->
#    ToDo Mocking
    ###sms = {
      "date": "Feb 02 2016",
      "balance": "9400.11",
      "location": "PYATEROCHKA 3156, IVANOVO",
      "user": "Alina",
      "procedure": "Purchase",
      "coast": "453.0"
    }###
    Firebird.attach options, (err, db) ->
      id =
        Expense: null
        Item: null
        Balance: null
      if (err)
        throw  err
      db.query "SELECT MAX(ID) FROM EXPENSE", (err, result) ->
        if (err)
          throw err
        id.Expense = result[0].MAX
        console.log id.Expense

        db.query "SELECT MAX(ID) FROM EXPENSEITEM", (err, result) ->
          if (err)
            throw err
          id.Item = result[0].MAX
          console.log id.Item

          db.query "SELECT BALANCE FROM ACCOUNT WHERE ID=2", (err, result) ->
            if (err)
              throw err
            id.Balance = parseFloat(result[0].BALANCE)
            console.log id.Balance

            st = "INSERT INTO EXPENSE
                    (ID, TRANSFERDATE, USERMT, TOTALITEMS, OBJVERSION, ACCOUNT, MONEYTYPE, DISC, DISCPERCENT, DISCTYPE, DISCINPRICE, COMMONTRADEPLACE, TOTAL, REMARKS) VALUES
                    (?,     ?,            1,      ?,         1,          2,      1,      0,      0,            1,        0,          NULL,           ?,  'autoinsert')"
            db.query st, [id.Expense + 1, sms.date, sms.coast, sms.coast], (err, result) ->
              if (err)
                throw err
              console.log result

              st = "INSERT INTO EXPENSEITEM
                    (ID, QTY, PRICE, REMARKS, EXPENSE, COMM, TOTAL, TRADEPLACE, DISC, TRANSFERDATE, IDX) VALUES
                    (?, ?, ?,          NULL,      ?,    4474,  ?,        12538,    0,  ?, ?)"
              db.query st, [id.Item + 1, 1, sms.coast, id.Expense + 1, sms.coast * 1, sms.date, 1], (err, result) ->
                if (err)
                  throw err
                console.log result

                st = "UPDATE ACCOUNT SET BALANCE=? WHERE ID=2"
                db.query st, [id.Balance - sms.coast], (err, result) ->
                  if (err)
                    throw err
                  console.log result
              ###st = "INSERT INTO EXPENSEITEM
                  (ID, QTY, PRICE, REMARKS, EXPENSE, COMM, TOTAL, TRADEPLACE, DISC, TRANSFERDATE, IDX) VALUES
                  (?, ?, ?,          NULL,      ?,    285,  ?,        258,    0,  ?, ?)"
              db.query st, [id.Item + 2, 2, sms.coast, id.Expense, sms.coast * 2, sms.data, 2], (err, result) ->
                if (err)
                  throw err
                console.log result###
              db.detach()


module.exports = db