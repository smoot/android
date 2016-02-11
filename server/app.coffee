express = require('express')
path = require('path')
favicon = require('serve-favicon')
logger = require('morgan')
cookieParser = require('cookie-parser')
bodyParser = require('body-parser')


#Project references
routes = require('./routes/index')
users = require('./routes/users')
smsdata = require('./routes/smsdata')
auth = require('./routes/auth')
smsDataAdapter = require('./model/smsDataAdapter')

###
Create global objects
###

global.smsData = new smsDataAdapter()

###
Main
###
app = express()

#view engine setup
app.set('views', path.join(__dirname, 'views'))
app.set('view engine', 'jade')

#uncomment after placing your favicon in /public
#//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: false }))
app.use(cookieParser())
app.use(require('stylus').middleware(path.join(__dirname, 'public')))
app.use(express.static(path.join(__dirname, 'public')))

###
Routes
###

app.use('/', routes)
app.use('/', auth)
app.use('/users', users)
app.use('/smsdata', smsdata)

###
  Catch 404 and forward to error handler
###

app.use (req, res, next) ->
  err = new Error('Not Found')
  err.status = 404
  next(err)
  return

#error handlers

#development error handler
#will print stacktrace
if (app.get('env') =='development')
 app.use (err, req, res, next) ->
  res.status(err.status || 500)
  res.render 'error',
    message: err.message
    error: err
  return


#production error handler
#no stacktraces leaked to user
app.use (err, req, res, next) ->
  res.status(err.status || 500);
  res.render 'error',
    message: err.message
    error: {}
  return

module.exports = app
