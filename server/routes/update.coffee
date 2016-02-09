express = require('express')
router = express.Router()
SMSDataList = require('../model/SMSDataList')

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
  SMSDataArray.getListStringify (req) ->
    res.render('index', {title: 'Express', body: req})
  return



module.exports = router