// Controller for the view home.html
app.controller('home', function ($scope, $http) {
    var self = this;

    $http.get('/rest/resource').then(function (response) {
        self.myData = response.data;
    });
});