// By Oleksiy Grechnyev
// Controller for the view login.html: Log In and Sign Up
app.controller('login', function ($scope, $http, $rootScope, $location, auth) {
    var self = this;

    // No credentials before you actually log in
    self.credentials = {};

    // New user object for sign up
    self.newUser = {};
    self.passw2 = "";

    // The login() function
    // A non-empty credentias object is created by the form in login.html or by the function signup()
    self.login = function () {
        auth.authenticate(self.credentials, function () {
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
        var userDto = self.newUser;

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