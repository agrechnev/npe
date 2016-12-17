// Controller for the view home.html
app.controller('login', function ($scope, $http, $rootScope, $location) {
    var self = this;

    // No credentials before you actually log in
    self.credentials = {};

    // Try to authenticate credentials with the server
    var authenticate = function (credentials, callback) {
        // If nonempty credentials, perform base-64 encoding
        // This is supposed to "log in" Spring Security
        var headers = credentials ? {
            authorization: "Basic " +
            btoa(credentials.username + ":" + credentials.password)
        } : {};

        // Authenticate on server using headers
        $http.get('userauth', {headers: headers}).then(function (response) {
            // Check is successfull
            if (response.data.name) {
                $rootScope.isAuthenticated = true;
            } else {
                $rootScope.isAuthenticated = false;
            }
            ;

            // Run function callback if not empty
            callback && callback();

        }, function () {
            // get operation had an error
            $rootScope.isAuthenticated = false;
            // Run callback anyway
            callback && callback();
        });
    };

    // The login() function
    // A non-empty credentias object is created by the form in login.html
    self.login = function () {
        authenticate(self.credentials, function () {
            if ($rootScope.isAuthenticated) {
                // Authentication success
                $location.path("/"); // Go to the main page
                self.error = false;
            } else {
                // Authentication failure
                // login.html will display the error message
                $location.path("/login"); // Go to the login page (with error)
                self.error = true;
            }
        });
    };
});