angular.module('ddd-issues', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/list', {
                templateUrl: '/app/list.html',
                controller: 'IssuesCtrl'
            })
            .when('/new', {
                templateUrl: '/app/new.html',
                controller: 'NewIssueCtrl'
            })
            .when('/edit/:issueNumber', {
                templateUrl: '/app/edit.html',
                controller: 'EditIssueCtrl'
            })
            .otherwise({
                redirectTo: '/list'
            });
    })
    .constant('products', ['acme', 'buggy-app'])
    .constant('versions', ['1.0.0', '1.2.3'])
    .controller("IssuesCtrl", function ($scope, $http) {

        $http.get('/app/issues')
            .success(function (data) {
                $scope.issues = data;
            })

    })
    .controller("NewIssueCtrl", function ($scope, $http, $location, versions, products) {

        $scope.newIssue = {};

        $scope.versions = versions;
        $scope.products = products;

        $scope.createIssue = function () {
            $http.post('/app/issues', $scope.newIssue)
                .success(function () {
                    $location.path('/list');
                })
        };

    })
    .controller("EditIssueCtrl", function ($scope, $http, $routeParams) {

        $http.get('/app/issues/' + $routeParams.issueNumber)
            .success(function (data) {
                $scope.issue = data;
            });

    });
