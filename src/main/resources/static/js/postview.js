// Controller for the view search.html
app.controller('postview', function ($scope, $http, $routeParams, $location, $route, categ) {
    var self = this;

    self.editingPost = false; // True if you're editing the text


    // Load categories using service categ
    categ.load();

    var url = '/rest/post/' + $routeParams.id;
    // Get the post by id
    $http.get(url).then(function (response) {
        self.post = response.data;
    });

    //--------------------------------------
    // Misc functions
    // print Java 8 LocalDateTime object as string
    self.timeToString = function (time) {

        // Add a leading zero if number is < 10
        function add0(n) {
            return n < 10 ? "0" + n : n;
        }

        return time.dayOfMonth + " " + time.month.toLowerCase() + " " + time.year + "   " +
            add0(time.hour) + ":" + add0(time.minute) + ":" + add0(time.second);
    }

    // Get a category by id
    self.catById = function (id) {
        return categ.byId(id);
    }

    // Get categories of a post as an array of names
    self.catAsList = function (post) {
        return categ.asNameList(post);
    }

    //-----------------------------------------------
    // Post editing functions

    // Edit a post
    self.edit = function () {
        self.editingPost = true;
        self.updatedText = self.post.text;
        self.updatedCats = self.post.categories.slice();
        self.allCats = categ.allIdList();

        // Selected categories from 2 lists
        self.selectedCat1 = self.updatedCats.length > 0 ? self.updatedCats[0] : 0;


        self.selectedCat2 = self.allCats.length > 0 ? self.allCats[0] : 0;
    }

    // Add a category to updatedCats
    self.addCat = function () {
        // Add only if not present already
        if (self.updatedCats.indexOf(self.selectedCat2) == -1) {
            self.updatedCats.push(self.selectedCat2);
            if (self.selectedCat1 == 0) self.selectedCat1 = self.selectedCat2;
        }
    }

    // Delete a category from updatedCats
    self.deleteCat = function () {
        var index = self.updatedCats.indexOf(self.selectedCat1);
        if (index > -1) {
            // Delete index from array
            self.updatedCats.splice(index, index + 1);
            // Put the position to something valid
            self.selectedCat1 = self.updatedCats.length > 0 ? self.updatedCats[0] : 0;
        }
    }

    // Cancel editing
    self.cancel = function () {
        self.editingPost = false;
        self.updatedText = "";
        self.updatedCats = [];
    }

    // Confirm editing: update
    self.update = function () {
        self.editingPost = false;

        var post = self.post; // Create the updator

        post.text = self.updatedText;
        post.categories = self.updatedCats;
        post.timeStamp = null; // Funny, the JSON back-conversion of LocalDateTime does not work

        $http.put('/rest/post/' + $routeParams.id, post).finally(
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