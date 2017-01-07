// Controller for the view users.html
app.controller('admusers', function ($scope, $http) {
    var self = this;

    $http.get('/rest/user').then(function (response) {
        self.allUsers = response.data;
    });
});