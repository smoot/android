express = require('express')
router = express.Router()

#GET home page.
router.get '/',  (req, res, next) ->
  res.render('index', {title: 'Express', body: req.balance})
  return

router.post '/*', (req, res, next) ->
  console.log(req.body)
  reqData = req.body
  console.log(reqData)
  res.statusCode = 200
  res.send()
  return

module.exports = router
