/**
 * The Categories AngularJS service
 * Created by Oleksiy Grechnyev on 12/30/2016.
 */
app.service('categ', function ($rootScope, $http) {
    var self = this;

    // Load all categories
    self.load = function (callback) {
        // Get categories
        $http.get('/rest/category').then(function (response) {
            self.categories = response.data;

            // Run callback() if not empty
            callback && callback();
        });
    }

    self.load(); // Load on init

    // Get category name by id
    self.byId = function (id) {
        var result = null;

        self.categories.forEach(function (c) {
            if (c.id == id) result = c.categoryName;
        });

        return result;
    }

    // All categories of a post object as list of their names
    self.asNameList = function (post) {
        var result = [];

        post.categories.forEach(function (id) {
            result.push(self.byId(id));
        });

        return result;
    }

    //List of category ids
    self.allIdList = function () {
        var result = [];

        self.categories.forEach(function (cat) {
            result.push(cat.id);
        });

        return result;
    }

});