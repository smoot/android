class dbAdapter
  Firebird = require('node-firebird')
  options
    host = '127.0.0.1'
    port = 3050
    database = 'database.fdb'
    user = 'SYSDBA'
    password = 'masterkey'

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

module.exports = dbAdapter