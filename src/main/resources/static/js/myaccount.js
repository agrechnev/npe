// By Oleksiy Grechnyev
// Controller for the view myaccount.html: Edit or delete my account
app.controller('myaccount', function ($scope, $http, $rootScope, $location, auth) {
    var self = this;

    self.init = function () {
        self.updateMessage = "";
        self.deleteMessage = "";
        self.changePasswMessage = "";

        self.passwChanger = {};
        self.passw = "";
        self.passw2 = "";

        user = {};
        // Read data from the server using current id
        $http.get('/rest/user/' + $rootScope.authUserId).then(function (response) {
                self.user = response.data;
            },
            function () {
                self.updateMessage = "Cannot read account: server error.";
            });

    };
    // Run it at startup
    self.init();

    self.update = function () {
        // Try to update the user
        $http.put("/rest/user/" + $rootScope.authUserId, self.user).then(function (response) {
            self.updateMessage = "Account updated successfully."
        }, function () {
            self.updateMessage = "Cannot update account."
        })

    }


    self.delete = function () {
        // Delete my account, password to verify
        $http.post("/rest/user/" + $rootScope.authUserId + "/delete_account",
            self.passw).then(function (response) {

            self.deleteMessage = "";
            // Deleted successfully, logging out
            $http.post('/logout', {}).finally(function () {
                $rootScope.isAuthenticated = false;
                $location.path("/"); // Go to the main page
                $route.reload();
            });
        }, function (response) {
            if (response.status == 403) {
                self.deleteMessage = "Wrong password.";
            } else {
                self.deleteMessage = "Server error.";
            }
        })

    }

    // Change password
    self.changePassw = function () {
        // Check if new passwords match
        if (self.passwChanger.newPassw != self.passw2) {
            self.changePasswMessage = "Passwords do not match. Try again."
            return;
        }

        $http.post("/rest/user/" + $rootScope.authUserId + "/change_password",
            self.passwChanger).then(function success(response) {

            self.changePasswMessage = "";

            // Set credentials for the re-login
            var credentials = {};
            credentials.username = $rootScope.authUserName;
            credentials.password = self.passwChanger.newPassw;

            // Changed successfully, logging out, then in again
            $http.post('/logout', {}).finally(function () {
                $rootScope.isAuthenticated = false;

                // Logging in again
                auth.authenticate(credentials, function () {
                    if ($rootScope.isAuthenticated) {
                        // Authentication success
                        self.init(); // Restart controller properly
                        self.changePasswMessage = "Password updated successfully.";
                        $location.path("/myaccount"); // Go back to the /myaccount page
                        $route.reload();

                    } else {
                        // Authentication failure: this shouldn't happen !!!
                        $location.path("/");
                    }
                });
            });
        }, function failure(response) {
            if (response.status == 403) {
                self.changePasswMessage = "Wrong old password.";
            } else {
                self.changePasswMessage = "Server error.";
            }
        })
    }
});