express = require('express')
router = express.Router()

#Validate request isn't undefined
router.post '/*', (req, res, next) ->
  if (req == null || req == "" || req == undefined)
    err = new Error('Bad request')
    err.status = 400
    return next(err)
  return next()

router.post '/', (req, res, next) ->
  console.log req.body
  smsData.push req.body, (err) ->
    if (err)
      err = new Error('POST request error')
      err.status = 400
      return next(err)
    else
      smsData.print (err) ->
        if (err)
          throw new Error "print error"
      res.statusCode = 200
      return res.send()

router.get '/list',  (req, res, next) ->
  smsData.getListStringify (req) ->
    res.render('index', {title: 'Express', body: req})
  return



module.exports = router