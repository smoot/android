express = require('express')
router = express.Router()

router.post '/*', (req, res, next) ->
  if (req == null || req == "" || req == undefined)
    error = "Request is undefined"
    console.log error
    return next()

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