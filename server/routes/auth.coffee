express = require('express')
router = express.Router()

router.use '/', (req, res, next) ->
  if (req.header('token') != '10A8D41FCA841705D8EFF4669578E512502')
    err = new Error('Not authorized')
    err.status = 401
    next(err)
  else
    next()

module.exports = router