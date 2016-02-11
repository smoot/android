class dbAdapter
  list = new Array()
  listSend = new Array()
  listUnSend = new Array()
  block = false

  _addToUnsend = (obj, callback) ->
    listUnSend.push(obj)
    callback()

  _addToSend = (obj, callback) ->
    listSend.push(obj)
    callback()

  _removeFromMainList = (index, callback) ->
    list.splice(index, 1)
    callback()


  constructor: () ->
    console.log "Create object dbAdapter"

  push: (obj, callback) ->
    if !(block)
      list.push(obj)
      console.log "Add object to dbAdapter. Total items is " + list.length
      return callback()
    else
      error = "Push blocking, try again"
      console.log error
      return callback({err: error})

  send: (obj, callback) ->
    block = true
    (@normalize obj, (err) ->
      if (err)
        error = "Can't send object to db"
        console.log error
        _addToUnsend obj, (err) ->
          if (err)
            console.log "Error adding to unsend"
#          TodO Error handler
        _removeFromMainList i, (err) -> #ToDo I use
          if (err)
            console.log "Error remove from main list"
#          TodO Error handler
        return callback({err: error})
      else
        @commit obj, (err) ->
          if (err)
            error = "Commit to db failed"
            console.log error
            return callback({err: error})
          else
            callback()) for obj in list
    return

  normalize: (obj, callback) ->
    return

  commit: (obj, callback) ->
    if true
      return callback()
    else
      error = "Commit error"
      console.log error
      return callback({err: error})

