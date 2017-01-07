// By Oleksiy Grechnyev
// Controller for the view newpost.html: Create a new post
app.controller('newpost', function ($scope, $http, $location, categ) {
    var self = this;

    self.newPost = {};
    self.newPostMessage = null;

    // Categories of the new post
    self.updatedCats = [];
    self.selectedCat1 = 0;

    // All categories
    categ.load(function () {
        // This runs after successful load
        self.allCats = categ.allIdList();
        self.selectedCat2 = self.allCats.length > 0 ? self.allCats[0] : 0;
    });

    // Get a category by id
    self.catById = function (id) {
        return categ.byId(id);
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

    // Create a new post
    self.create = function () {
        var postDto = self.newPost;

        // Set categories
        self.newPost.categories = self.updatedCats;

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