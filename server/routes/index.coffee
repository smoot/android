express = require('express')
router = express.Router()
#reqData= new SMSData();

#GET home page.
router.get '/',  (req, res, next) ->
  res.render('index', {title: 'Express', body: JSON.stringify(smsData)})
  return



module.exports = router
