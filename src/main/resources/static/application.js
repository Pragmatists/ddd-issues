angular.module('ddd-issues', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/issues/', {
                templateUrl: '/app/list.html',
                controller: 'IssuesCtrl'
            })
            .when('/new', {
                templateUrl: '/app/new.html',
                controller: 'NewIssueCtrl'
            })
            .when('/issues/:issueNumber', {
                templateUrl: '/app/show.html',
                controller: 'EditIssueCtrl'
            })
            .otherwise({
                redirectTo: '/issues/'
            });
    })
    .constant('products', ['acme', 'buggy-app'])
    .constant('versions', ['1.0.0', '1.2.3'])
    .controller("IssuesCtrl", function ($scope, $http) {

        $http.get('/app/issues')
            .success(function (data) {
                $scope.issues = data;
            });

    })
    .controller("IssueTitleCtrl", function ($scope, $http, $route) {
    	
    	$scope.edited = false;
    	$scope.edit = function(){
    		$scope.newTitle = $scope.issue.title;
    		$scope.edited = true;
    	};
    	$scope.confirm = function(){
            $http.post('/app/issues/' + $scope.issue.number + '/rename', {newTitle: $scope.newTitle})
            .success(function () {
            	$route.reload();
            });
    	};
    	$scope.cancel = function(){
    		$scope.edited = false;
    	};

    })
    .controller("NewIssueCtrl", function ($scope, $http, $location, versions, products) {

        $scope.newIssue = {};

        $scope.versions = versions;
        $scope.products = products;

        $scope.createIssue = function () {
            $http.post('/app/issues', $scope.newIssue)
                .success(function () {
                    $location.path('/list');
                });
        };

    })
    .controller("EditIssueCtrl", function ($scope, $http, $routeParams) {

        $http.get('/app/issues/' + $routeParams.issueNumber)
            .success(function (data) {
                $scope.issue = data;
            });

    });
