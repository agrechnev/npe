// By Oleksiy Grechnyev
// Controller for the view users.html
app.controller('admsample', function ($scope, $http, $route, $rootScope, $location, auth) {
        var self = this;

        self.errorMessage = "";

        self.submit = function (url) {
            if (!self.passw) {
                self.errorMessage = "Password required.";
                return;
            }


            $http.post(url, self.passw).then(function success(response) {

                    self.errorMessage = "";

                    // Set credentials for the re-login as the new admin
                    var credentials = {};
                    credentials.username = "admin";
                    credentials.password = "admin";

                    // Deleted successfully, logging out
                    $http.post('/logout', {}).finally(function () {
                        $rootScope.isAuthenticated = false;
                        $rootScope.isAdmin = false;
                        self.passw = "";

                        // Logging in again as admin:admin
                        auth.authenticate(credentials, function () {
                            if ($rootScope.isAuthenticated) {
                                // Authentication success
                                self.errorMessage = "Operation successful. " +
                                    "You are now admin:admin. " +
                                    "Don't forget to change your password!";
                                //$location.path("/admsample"); // Go back
                                //$route.reload();

                            } else {
                                // Authentication failure: this shouldn't happen !!!
                                $location.path("/");
                                $route.reload();
                            }
                        });
                    });

                }, function failure(response) {
                    if (response.status == 403) {
                        self.errorMessage = "Wrong password.";
                    } else {
                        self.errorMessage = "Server error.";
                    }
                }
            );
        }
    }
);