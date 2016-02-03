//(function () {
    // Private variable
    //var date, coast, location, balance, procedure, user, currency; //ToDO MOCKING

    //ToDO MOCKING
    var date = "2015-01-01",
        coast = "2013.45",
        location = "IVANOVO",
        balance = "43948.32",
        procedure = "Purchase",
        user = "Maks",
        currency = "RUB";
    // Constructor
    function SMSDataClass() {
        // always initialize all instance properties
    }

    // class methods

    SMSDataClass.prototype.getDate = function () {
        return this.date;
    };

    SMSDataClass.prototype.setDate = function (date) {
        this.date = date;
    };

    SMSDataClass.prototype.getLocation = function () {
        return this.location;
    };

    SMSDataClass.prototype.setLocation = function (location) {
        this.location = location;
    };

    SMSDataClass.prototype.getBalance = function () {
        return this.balance;
    };

    SMSDataClass.prototype.setBalance = function (balance) {
        this.balance = balance;
    };

    SMSDataClass.prototype.getProcedure = function () {
        return this.procedure;
    };

    SMSDataClass.prototype.setProcedure = function (procedure) {
        this.procedure = procedure;
    };

    SMSDataClass.prototype.getCoast = function () {
        return this.coast;
    };

    SMSDataClass.prototype.setCoast = function (coast) {
        this.coast = coast;
    };

    SMSDataClass.prototype.getUser = function () {
        return this.user;
    };

    SMSDataClass.prototype.setUser = function (user) {
        this.user = user;
    };

    SMSDataClass.prototype.getCurrency = function () {
        return this.currency;
    };

    SMSDataClass.prototype.setCurrency = function (currency) {
        this.currency = currency;
    };


    // export the class
    module.exports = SMSDataClass;

//}).call(this);