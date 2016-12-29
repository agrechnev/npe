// By Oleksiy Grechnyev
// Controller for the view myaccount.html: Edit or delete my account
app.controller('myaccount', function ($scope, $http, $rootScope, $location) {
    var self = this;
    self.updateMessage = "";
    self.deleteMessage = "";

    user = {};
    // Read data from the server using current id
    $http.get('/rest/user/' + $rootScope.authUserId).then(function (response) {
            self.user = response.data;
        },
        function () {
            self.updateMessage = "Cannot read account: server error.";
        });

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
            // Deleted successfully, logging out
            $http.post('/logout', {}).finally(function () {
                $rootScope.isAuthenticated = false;
                $location.path("/"); // Go to the main page
                $route.reload();
            });
        }, function (response) {
            if (response.status == 401) {
                self.deleteMessage = "Wrong password."
            } else {
                self.deleteMessage = "Server error."
            }
        })

    }
});