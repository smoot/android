var express = require('express');
var router = express.Router();
var reqData;

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express', body: reqData.balance});
});

router.post('/*', function (req, res, next) {
    console.log(req.body);
    reqData = req.body;
    console.log(reqData);
    res.statusCode = 200;
    res.send();
});

module.exports = router;
