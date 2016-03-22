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
  smsData.push req.body, (err, balance) ->
    if (err)
      err = new Error('POST request error')
      err.status = 400
      return next(err)
    res.write(JSON.stringify(balance));
    res.statusCode = 200
    return res.send()
  return

router.get '/list', (req, res, next) ->
  smsData.getListStringify (req) ->
    res.render('index', {title: 'Express', body: req})
  return

#FIXME !!
router.get '/balance', (req, res, next) ->
  db.getBalance (err, balance) ->
    if (err)
      err = new Error('GET request error')
      err.status = 400
      return next(err)
    balance1 = [
      name: "ТКС"
      balance: 5558
    ,
      name: "Кошелек МАКС"
      balance: 2456
    ]
    res.write(JSON.stringify(balance));
    res.statusCode = 200
    return res.send()
  return

module.exports = router