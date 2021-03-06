var express = require('express');
var app = express();
var router = express.Router();
var firebase = require('firebase');
var config = {
    apiKey: "AIzaSyAJhcwWoTxW2czDSkPH-zC8YxUc9y2UdwU",
    authDomain: "orderit-3e188.firebaseapp.com",
    databaseURL: "https://orderit-3e188.firebaseio.com",
    storageBucket: "orderit-3e188.appspot.com",
    messagingSenderId: "784106592734"
};
firebase.initializeApp(config);

router.use(function (req, res, next) {
    var user = firebase.auth().currentUser;

    if (user) {
        console.log("Usage is signed in!");
    } else {
        console.log("Usage is NOT signed in!");
    }
    next();
});

/* GET login page. */
router.get('/', function (req, res) {
    res.render('index', {title: 'Order-It Kitchen'});
});

router.post('/login', function (req, res) {
    console.log("Logging in");
    var email = req.body.email;
    var password = req.body.password;
    firebase.auth().signInWithEmailAndPassword(email, password).then(function (result) {
        res.json({success: true});
        console.log("User signed in successfully!");
    }).catch(function (error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        res.status(400);
        res.json({error: {status: 400, stack: errorCode}, message: errorMessage});
    });
});

router.post('/register', function (req, res) {
    console.log("Register a new user");
    var email = req.body.email;
    var password = req.body.password;
    firebase.auth().createUserWithEmailAndPassword(email, password)
        .then(function (result) {
            res.json({success: true});
            console.log("User registered successfully!");
        })
        .catch(function (error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            res.status(400);
            res.json({error: {status: 400, stack: errorCode}, message: errorMessage});
        });
});


app.use('/', router)
module.exports = app;
