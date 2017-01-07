// Controller for the view search.html
app.controller('search', function ($scope, $http, categ) {
    var self = this;

    categ.load();

    // Get all posts: for now
    $http.get('/rest/post').then(function (response) {
        self.allPosts = response.data;
    });

    // Get categories of a post as an array of names
    self.catAsList = function (post) {
        return categ.asNameList(post);
    }
});