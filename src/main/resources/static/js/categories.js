// Controller for the view categories.html
app.controller('categories', function ($scope, $http) {
    var self = this;

    $http.get('/rest/category').then(function (response) {
        self.allCategories = response.data;
    });
});