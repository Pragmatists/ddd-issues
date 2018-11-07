angular.module('ddd-issues', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/issues/', {
                templateUrl: '/app/list.html',
                controller: 'IssuesCtrl'
            })
            .when('/issues/:issueNumber', {
                templateUrl: '/app/show.html',
                controller: 'EditIssueCtrl'
            })
            .otherwise({
                redirectTo: '/issues/'
            });
    })
    .constant('versions', [
        { product: 'ddd-issues', version: '0.5.0'},
        { product: 'ddd-issues', version: '0.4.0'},
        { product: 'ddd-issues', version: '0.3.0'},
        { product: 'buggy-app', version: '1.0.0'},
        { product: 'buggy-app', version: '1.2.0'},
        { product: 'buggy-app', version: '1.2.1'},
        { product: 'buggy-app', version: '1.2.4'}])
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
    .controller("NewIssueCtrl", function ($scope, $http, $location, versions) {

        $scope.newIssue = {};
        $scope.versions = versions;

        $scope.createIssue = function () {
        	
        	console.log('Creating issue: ', $scope.newIssue);
        	
            $http.post('/app/issues', $scope.newIssue)
                .success(function (data, status, headers) {
                    $location.path('/issues/' + headers['Location']);
                });
        };

    })
    .controller("EditIssueCtrl", function ($scope, $http, $routeParams) {

        $http.get('/app/issues/' + $routeParams.issueNumber)
            .success(function (data) {
                $scope.issue = data;
            });

    });
