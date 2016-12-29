// By Oleksiy Grechnyev
// Controller for the view login.html: Log In and Sign Up
app.controller('login', function ($scope, $http, $rootScope, $location) {
    var self = this;

    // No credentials before you actually log in
    self.credentials = {};

    // New user object for sign up
    self.newUser = {};
    self.passw2 = "";

    // Try to authenticate credentials with the server
    var authenticate = function (credentials, callback) {
        // If nonempty credentials, perform base-64 encoding
        // This is supposed to "log in" Spring Security
        var headers = credentials ? {
            authorization: "Basic " +
            btoa(credentials.username + ":" + credentials.password)
        } : {};

        // Authenticate on server using headers
        $http.get('/userauth', {headers: headers}).then(function (response) {
            // Check is successfull
            if (response.data.name) {
                // Set up global variables
                $rootScope.isAuthenticated = true;
                $rootScope.authUserName = response.data.name;
                $rootScope.isAdmin = (response.data.authorities[0].authority === "ROLE_ADMIN");

                // Try to get user ID from the server
                $http.get('/userid').then(function (response) {
                        $rootScope.authUserId = response.data;
                    },
                    function () {
                        $rootScope.authUserId = undefined;
                    }
                );
            } else {
                $rootScope.isAuthenticated = false;
            }

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
    // A non-empty credentias object is created by the form in login.html or by the function signup()
    self.login = function () {
        authenticate(self.credentials, function () {
            if ($rootScope.isAuthenticated) {
                // Authentication success
                $location.path("/"); // Go to the main page
                self.errorLogin = false;
            } else {
                // Authentication failure
                // login.html will display the error message
                $location.path("/login"); // Go to the login page (with error)
                self.errorLogin = true;
            }
        });
    };

    // The signup() function
    // Nonempty newUser object is created by the sign up form in login.html
    self.signup = function () {
        userDto = self.newUser;

        // Check that passwords match
        if (userDto.passw != self.passw2) {
            self.errorSignup = true;
            self.errorMessage = "Error! Passwords do not match. Please try again.";
            return;
        }

        // Create a complete new UserDto object
        userDto.points = 0;
        userDto.role = "USER";
        userDto.id = 0;

        // Try to POST the new object
        $http.post("/rest/user", userDto).then(
            function success(response) {
                self.errorSignup = false;

                // Log In the new user after successful POST only

                self.credentials.username = userDto.login;
                self.credentials.password = userDto.passw;

                self.login();

            },
            function failure(response) {
                self.errorSignup = true;

                if (response.status == 409) {
                    self.errorMessage = "User creation failed: login name already in use.";
                } else {
                    self.errorMessage = "User creation failed: server error.";
                }
            }
        );

    }
});