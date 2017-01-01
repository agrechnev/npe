// Controller for the view search.html
app.controller('postview', function ($scope, $http, $routeParams, $location, $route) {
    var self = this;

    self.editingPost = false; // True if you're editing the text

    var url = '/rest/post/' + $routeParams.id;
    // Get all posts: for now
    $http.get(url).then(function (response) {
        self.post = response.data;
    });

    // print Java 8 LocalDateTime object as string
    self.timeToString = function (time) {

        // Add a leading zero if number is < 10
        function add0(n) {
            return n < 10 ? "0" + n : n;
        }

        return time.dayOfMonth + " " + time.month.toLowerCase() + " " + time.year + "   " +
            add0(time.hour) + ":" + add0(time.minute) + ":" + add0(time.second);
    }

    // Edit a post
    self.edit = function () {
        self.editingPost = true;
        self.updatedText = self.post.text;
    }

    // Cancel editing
    self.cancel = function () {
        self.editingPost = false;
        self.updatedText = "";
    }

    // Confirm editing: update
    self.update = function () {
        self.editingPost = false;

        var post = self.post; // Create the updator

        post.text = self.updatedText;
        post.timeStamp = null; // Funny, the JSON back-conversion of LocalDateTime does not work

        $http.put('/rest/post/' + $routeParams.id, self.post).finally(
            function final() {
                $route.reload();
            }
        );

    }

    // Delete a post
    self.delete = function () {
        if (window.confirm("Delete post: Are you sure?")) {
            $http.delete('/rest/post/' + $routeParams.id).finally(
                function final() {
                    $location.path("/search"); // Go to the search page
                    $route.reload();
                }
            );
        }
    }


});