express = require('express')
router = express.Router()

class SMSDataList
  #{@date, @coast, @location, @balance, @procedure, @user, @currency}
  expectedFields = ['coast', 'location', 'procedure']
  list = new Array()

  constructor: (options) ->
    console.log "Create class SMSDataList"

  push: (obj, callback) ->
    console.log "Push start"
    @validate obj, (err) ->
      if (err)
        console.log "Can't push object to array. Field is missing"
        callback({err: "Can't push object to array. Field is missing"})
      else
        list.push(obj)
        console.log "Add object to array. Total items is " + list.length
        callback()
    return

  validate: (obj, callback) ->
    (f = obj[field]
    if (f == null || f == "" || f == undefined)
      console.log "Field: " + field + " is undefined"
      console.log "Please Fill All Required Field"
      return callback({err: "Please Fill All Required Field"})) for field in expectedFields
    return callback()

  getLength: (callback) -> callback(list.length)

  getList: (callback) -> callback(list)

  print: (callback) ->
    (console.log JSON.stringify(SMSData)) for SMSData in list
    callback()


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
  objBad3 = {}
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

###addSMSDataTest.getList (list) ->
  (console.log JSON.stringify(obj)) for obj in list###

SMSDataArray = new SMSDataList()
router.post '/*', (req, res, next) ->
  console.log req.body
  SMSDataArray.push req.body, (err) ->
    if (err)
      throw new Error "Add to array error"
    else
      res.statusCode = 200

      console.log "FOR"
      SMSDataArray.print (err) ->
        if err
          throw new Error "print error"
#       when SMSData isnt null
  res.render

  return

router.get '/list',  (req, res, next) ->
  res.render('index', {title: 'Express', body: SMSDataArray})
  return

module.exports = router