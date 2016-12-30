/**
 * The Authentication service
 * Created by Oleksiy Grechnyev on 12/30/2016.
 */
app.service('auth', function ($rootScope, $http) {
    var self = this;

    // Try to authenticate credentials with the server
    self.authenticate = function (credentials, callback) {
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
});