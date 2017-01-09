// By Oleksiy Grechnyev
// Controller for the view users.html
app.controller('admusers', function ($scope, $http, $route) {
    var self = this;

    self.editingUserId = 0; // Dummy: not editing
    self.editingUser = null;

    $http.get('/rest/user').then(function (response) {
        self.userList = response.data;
        self.selectedUser = self.userList.length > 0 ? self.userList[0] : null;
    });

    // Delete user
    self.deleteUser = function (id) {
        $http.delete("/rest/user/" + id).then(function () {
            $route.reload();
        });
    }

    // Edit a user
    self.editUser = function (id) {
        self.editingUserId = id;

        self.editingUser = self.selectedUser;
    }

    // Cancel editing
    self.cancel = function () {
        self.editingUserId = 0; // Dummy: not editing
        self.editingUser = null;
        $route.reload();
    }

    // Update (finish editing)
    self.update = function () {
        var url = "/rest/user/" + self.editingUserId;

        self.editingUserId = 0; // Finish editing

        $http.put(url, self.editingUser).then(function () {
            self.editingUser = null;
            $route.reload();
        });
    }
});