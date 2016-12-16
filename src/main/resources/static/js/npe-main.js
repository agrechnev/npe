/**
 * Created by Oleksiy Grechnyev on 12/12/2016.
 * Main code, including router, is here
 */
var app = angular.module("npeApp", ["ngRoute"]);

app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "home.html"
        })
        .when("/red", {
            templateUrl: "red.html"
        })
        .when("/green", {
            templateUrl: "green.html"
        })
        .when("/blue", {
            templateUrl: "blue.html"
        });
});