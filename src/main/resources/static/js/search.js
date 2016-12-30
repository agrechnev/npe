// Controller for the view search.html
app.controller('search', function ($scope, $http) {
    var self = this;

    // Get all posts: for now
    $http.get('/rest/post').then(function (response) {
        self.allPosts = response.data;
    });
});