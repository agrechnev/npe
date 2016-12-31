// Controller for the view search.html
app.controller('postview', function ($scope, $http, $routeParams) {
    var self = this;

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
});