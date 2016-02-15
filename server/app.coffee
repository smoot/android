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
dbAdapter = require('./model/dbAdapter')

###
Create global objects
###

global.smsData = new smsDataAdapter()
global.db = new dbAdapter()

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

###smsData.push {"data":"Tuesday Feb 02 2016 at 12:00 AM","balance":"9400.11","location":"PYATEROCHKA 3156, IVANOVO","user":"Alina","procedure":"Purchase","coast":"453.0"}, (err) ->
smsData.push {"data":"Sunday Jan 31 2016 at 12:00 AM","balance":"5323.11","location":"APTEKA MAKSAVIT, IVANOVO","user":"Alina","procedure":"Purchase","coast":"2383.0"}, (err) ->
smsData.push {"data":"Friday Jan 27 2017 at 12:00 AM","balance":"45879.06","location":"Тинькофф","user":"Maks","procedure":"Percent","coast":"304.98"}, (err) ->
smsData.push {"data":"Thursday Jan 28 2016 at 12:00 AM","balance":"10116.51","location":"OKEY, IVANOVO","user":"Alina","procedure":"Purchase","coast":"360.5"}, (err) ->
smsData.print()
smsData.getList (list) ->
  db.send list, (err) ->
  return###

#db.fbSelect()
#db.fbExpense()

module.exports = app
