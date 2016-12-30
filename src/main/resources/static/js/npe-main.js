/**
 * Created by Oleksiy Grechnyev on 12/12/2016.
 * Main code: router + navigation controller
 */
var app = angular.module("npeApp", ["ngRoute"]);

app.config(function ($routeProvider, $httpProvider) {
    // Set Up Routing
    $routeProvider
        .when("/", {
            templateUrl: "home.html",
            controller: 'home',
            controllerAs: 'controller'
        })
        .when("/users", {
            templateUrl: "users.html",
            controller: 'users',
            controllerAs: 'controller'
        })
        .when("/search", {
            templateUrl: "search.html",
            controller: 'search',
            controllerAs: 'controller'
        })
        .when("/newpost", {
            templateUrl: "newpost.html",
            controller: 'newpost',
            controllerAs: 'controller'
        })
        .when("/myaccount", {
            templateUrl: "myaccount.html",
            controller: 'myaccount',
            controllerAs: 'controller'
        })
        .when("/login", {
            templateUrl: "login.html",
            controller: 'login',
            controllerAs: 'controller'
        });

    // This setting is important for spring security
    // for some reason it disables the default Spring Security login window
    $httpProvider.defaults.headers.common["X-Requested-with"] = 'XMLHttpRequest';
});

// Navigation controller.
app.controller('navigation', function ($scope, $http, $location, $rootScope, $route) {
    var self = this;

    // Not authenticated in the beginning
    $rootScope.isAuthenticated = false;

    // Check the current location (to show the active entry in nav-pills menu)
    self.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };

    self.logout = function () {
        $http.post('/logout', {}).finally(function () {
            $rootScope.isAuthenticated = false;
            $location.path("/"); // Go to the main page
            $route.reload();
        });
    }

});