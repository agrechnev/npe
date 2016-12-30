// By Oleksiy Grechnyev
// Controller for the view newpost.html: Create a new post
app.controller('newpost', function ($scope, $http, $location) {
    var self = this;

    self.newPost = {};
    self.newPostMessage = null;

    // Create a new post
    self.create = function () {
        var postDto = self.newPost;

        // Set extra fields
        postDto.rating = 0;
        postDto.userId = null;
        postDto.timeStamp = null;

        // Try to POST the new object
        $http.post("/rest/post", postDto).then(
            function success(response) {
                $location.path("/"); // Go to the main page
            },
            function failure(response) {
                self.newPostMessage = "Error. Try again."
            }
        );
    };
});