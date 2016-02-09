class SMSDataList
  #{@date, @coast, @location, @balance, @procedure, @user, @currency}
  expectedFields = ['coast', 'location', 'procedure']
  list = new Array()

  constructor: () ->
    console.log "Create object SMSDataList"

  push: (obj, callback) ->
    @validate obj, (err) ->
      if (err)
        error = "Can't push object to array"
        console.log error
        return callback({err: error})
      else
        console.log "else"
        list.push(obj)
        console.log "Add object to array. Total items is " + list.length
        callback()
    return

  validate: (obj, callback) ->
    if (Object.keys(obj).length < Object.keys(expectedFields).length)
      error = "Fill in required fields: @" + expectedFields.toString()
      console.log error
      return callback({err: error})
    (f = obj[field]
    if (f == null || f == "" || f == undefined)
      error = "Field: @" + field + " is undefined"
      console.log error
      return callback({err: error})) for field in expectedFields
    return callback()

  getLength: (callback) -> callback(list.length)

  getList: (callback) -> callback(list)

  print: (callback) ->
    (console.log JSON.stringify(SMSData)) for SMSData in list
    callback()

  getListStringify: (callback) ->
    ls = new Array()
    (ls.push(JSON.stringify(item))) for item in list
    callback (ls)


addSMSDataTest = ->
  test = new SMSDataList()
  objGood1 = {
    date: "date"
    coast: "coast"
    location: "loc"
    procedure: "proc"
  }
  objBad1 = {
    date: "date"
    coast: "fdfd"
    location: "loc"
    procedure: ""
  }
  objBad2 = {
    date: "date"
    location: "loc"
    procedure: "proc"
  }
  objGood2 = {
    coast: "coast"
    location: "loc"
    procedure: "proc"
  }
  objBad3 = {
    coast: "coast"
  }
  console.log "objGood1"
  test.push objGood1, (err) ->
    if (err)
      console.log err.err
  console.log "objBad1"
  test.push objBad1, (err) ->
    if (err)
      console.log err.err
  console.log "objBad2"
  test.push objBad2, (err) ->
    if (err)
      console.log err.err
  console.log "objGood2"
  test.push objGood2, (err) ->
    if (err)
      console.log err.err
  console.log "objBad3"
  test.push objBad3, (err) ->
    if (err)
      console.log err.err

  test.getLength (res) ->
    console.log "Total items is " + res
    if res != 2 then throw new Error("Test addSMSDataTest FAILED")

  return test
#addSMSDataTest()
module.exports = SMSDataList