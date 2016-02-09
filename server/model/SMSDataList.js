// Generated by CoffeeScript 1.10.0
(function() {
  var SMSDataList, addSMSDataTest;

  SMSDataList = (function() {
    var expectedFields, list;

    expectedFields = ['coast', 'location', 'procedure'];

    list = new Array();

    function SMSDataList() {
      console.log("Create object SMSDataList");
    }

    SMSDataList.prototype.push = function(obj, callback) {
      this.validate(obj, function(err) {
        var error;
        if (err) {
          error = "Can't push object to array";
          console.log(error);
          return callback({
            err: error
          });
        } else {
          list.push(obj);
          console.log("Add object to array. Total items is " + list.length);
          return callback();
        }
      });
    };

    SMSDataList.prototype.validate = function(obj, callback) {
      var error, f, field, i, len;
      if (Object.keys(obj).length < Object.keys(expectedFields).length) {
        error = "Fill in required fields: @" + expectedFields.toString();
        console.log(error);
        return callback({
          err: error
        });
      }
      for (i = 0, len = expectedFields.length; i < len; i++) {
        field = expectedFields[i];
        f = obj[field];
        if (f === null || f === "" || f === void 0) {
          error = "Field: @" + field + " is undefined";
          console.log(error);
          return callback({
            err: error
          });
        }
      }
      return callback();
    };

    SMSDataList.prototype.getLength = function(callback) {
      return callback(list.length);
    };

    SMSDataList.prototype.getList = function(callback) {
      return callback(list);
    };

    SMSDataList.prototype.print = function(callback) {
      var SMSData, i, len;
      for (i = 0, len = list.length; i < len; i++) {
        SMSData = list[i];
        console.log(JSON.stringify(SMSData));
      }
      return callback();
    };

    SMSDataList.prototype.getListStringify = function(callback) {
      var i, item, len, ls;
      ls = new Array();
      for (i = 0, len = list.length; i < len; i++) {
        item = list[i];
        ls.push(JSON.stringify(item));
      }
      return callback(ls);
    };

    return SMSDataList;

  })();

  addSMSDataTest = function() {
    var objBad1, objBad2, objBad3, objGood1, objGood2, test;
    test = new SMSDataList();
    objGood1 = {
      date: "date",
      coast: "coast",
      location: "loc",
      procedure: "proc"
    };
    objBad1 = {
      date: "date",
      coast: "fdfd",
      location: "loc",
      procedure: ""
    };
    objBad2 = {
      date: "date",
      location: "loc",
      procedure: "proc"
    };
    objGood2 = {
      coast: "coast",
      location: "loc",
      procedure: "proc"
    };
    objBad3 = {
      coast: "coast"
    };
    console.log("objGood1");
    test.push(objGood1, function(err) {
      if (err) {
        return console.log(err.err);
      }
    });
    console.log("objBad1");
    test.push(objBad1, function(err) {
      if (err) {
        return console.log(err.err);
      }
    });
    console.log("objBad2");
    test.push(objBad2, function(err) {
      if (err) {
        return console.log(err.err);
      }
    });
    console.log("objGood2");
    test.push(objGood2, function(err) {
      if (err) {
        return console.log(err.err);
      }
    });
    console.log("objBad3");
    test.push(objBad3, function(err) {
      if (err) {
        return console.log(err.err);
      }
    });
    test.getLength(function(res) {
      console.log("Total items is " + res);
      if (res !== 2) {
        throw new Error("Test addSMSDataTest FAILED");
      }
    });
    return test;
  };

  module.exports = SMSDataList;

}).call(this);

//# sourceMappingURL=SMSDataList.js.map
