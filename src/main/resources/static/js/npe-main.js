/**
 * Created by Oleksiy Grechnyev on 12/12/2016.
 * Main code: router + navigation controller
 */
var app = angular.module("npeApp", ["ngRoute", "ngCookies"]);

app.config(function ($routeProvider, $httpProvider) {
    // Set Up Routing
    $routeProvider
    // Normal pages
        .when("/", {
            templateUrl: "home.html",
            controller: 'home',
            controllerAs: 'controller'
        })
        .when("/search", {
            templateUrl: "search.html",
            controller: 'search',
            controllerAs: 'controller'
        })
        .when("/categories", {
            templateUrl: "categories.html",
            controller: 'categories',
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
        })
        // Admin pages
        .when("/admsample", {
            templateUrl: "admin/admsample.html",
            controller: 'admsample',
            controllerAs: 'controller'
        })
        .when("/admusers", {
            templateUrl: "admin/admusers.html",
            controller: 'admusers',
            controllerAs: 'controller'
        })
        // Special pages, not in the menu
        .when("/postview/:id", {
            templateUrl: "postview.html",
            controller: 'postview',
            controllerAs: 'controller'
        });

    // This setting is important for spring security
    // for some reason it disables the default Spring Security login window
    $httpProvider.defaults.headers.common["X-Requested-with"] = 'XMLHttpRequest';
});

// Navigation controller.
app.controller('navigation', function ($scope, $http, $location, $rootScope, $route, $cookies, auth) {
    var self = this;

    // Check if I am already authenticated
    // Run authenticate w/o credentials
    auth.authenticate();

    // Check the current location (to show the active entry in nav-pills menu)
    self.isActive = function (viewLocation) {
        return viewLocation === $location.path();
    };

    self.logout = function () {
        $http.post('/logout', {}).finally(function () {
            $rootScope.isAuthenticated = false;
            $rootScope.isAdmin = false;
            $rootScope.isExpert = false;
            $cookies.remove("JSESSIONID"); // Just in case

            // This is very important to set XSRF-TOKEN cookie after logout
            // Without this I wouldn't be able to Sign Up after Logout
            // Don't know exactly why it is so
            // Apparently you need at leas one get before post to get the token cookie
            auth.authenticate();

            $location.path("/"); // Go to the main page
            $route.reload();
        });
    }

});