// Controller for the view categories.html
app.controller('categories', function ($scope, $http, $route) {
    var self = this;

    self.newCatName = "";
    self.editCatName = "";
    self.editCatId = 0; // Id of the category being edited now

    // Get all categories
    $http.get('/rest/category').then(function (response) {
        self.categories = response.data;
    });

    // Add a new category
    self.addCat = function () {
        var catDto = {};
        catDto.categoryName = self.newCatName;

        $http.post('/rest/category', catDto).then(function (response) {
            self.newCatName = "";
            $route.reload();
        });
    }

    // Delete a category
    self.deleteCat = function (id) {
        $http.delete('/rest/category/' + id).then(function (response) {
            $route.reload();
        });
    }

    // Edit a category
    self.editCat = function (id) {
        self.editCatId = id;

        // Find category by id
        self.categories.forEach(function (c) {
            if (c.id == id) self.editCatName = c.categoryName;
        });
    }

    // Update a category (finish editing)
    self.update = function () {
        var url = '/rest/category/' + self.editCatId; // Create URL

        // Create the updator
        var updator = {};
        updator.categoryName = self.editCatName;

        $http.put(url, updator).then(function (response) {
            $route.reload();
        });
    }

    // Cancel edit
    self.cancel = function () {
        self.editCatName = "";
        self.editCatId = 0;
        $route.reload();
    }
});