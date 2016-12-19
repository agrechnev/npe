// Controller for the view users.html
app.controller('users', function ($scope, $http) {
    var self = this;

    $http.get('/rest/user').then(function (response) {
        self.allUsers = response.data;
    });
});