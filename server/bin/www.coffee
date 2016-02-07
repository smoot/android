#!/usr/bin/env node

#Module dependencies

app = require('../app')
debug = require('debug')('server:server')
http = require('http')

#Get port from environment and store in Express.

port = process.env.PORT || '3000'
#port = '3000'
app.set('port', port)


#Create HTTP server.

server = http.createServer(app)

#Listen on provided port, on all network interfaces.

server.listen(port)

server.on 'error', (error) ->
  if (error.syscall != 'listen')
    throw error

  bind = if typeof port == 'string' then 'Pipe ' + port else 'Port ' + port

  #handle specific listen errors with friendly messages
  switch error.code
    when 'EACCES'
      console.error(bind + ' requires elevated privileges')
      process.exit(1)
    when 'EADDRINUSE'
      console.error(bind + ' is already in use')
      process.exit(1)
    else throw error
  return

server.on 'listening', ->
  addr = server.address()
  bind = if typeof port == 'string' then 'Pipe ' + port else 'Port ' + port
  debug('Listening on ' + bind)
  return