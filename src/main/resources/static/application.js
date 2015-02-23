angular.module('ddd-issues', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider.
            when('/list', {
                templateUrl: '/app/list.html',
                controller: 'IssuesCtrl'
            }).
            when('/new', {
                templateUrl: '/app/new.html',
                controller: 'NewIssueCtrl'
            }).
            otherwise({
                redirectTo: '/list'
            });
    })
    .controller("IssuesCtrl", function ($scope) {

    })
    .controller("NewIssueCtrl", function($scope) {

    });
