var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var index = require('./routes/index');
var users = require('./routes/users');
var chat = require('./routes/chat');

var app = express();
var http = require('http');
var server = http.createServer(app);
var io = require('socket.io')(server);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

// uncomment after placing your favicon in /public
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/chat', chat);

io.on('connection', function(socket){
    console.log('a user connected');

    socket.on('chat message', function(msg){
        socket.broadcast.emit('chat message', msg);
    });

    socket.on('stoppedTyping', function(user){
        socket.broadcast.emit('stoppedTyping', user);
    });

    socket.on('isTyping', function(user){
        socket.broadcast.emit('isTyping', user);
    });

    socket.on('userConnected', function(user){
        io.emit('chat message', user + " connected");
    });

    socket.on('disconnect', function(){
        console.log('user disconnected');
        io.emit('chat message', 'user disconnected');
    });
});

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

server.listen(3000);

module.exports = app;
