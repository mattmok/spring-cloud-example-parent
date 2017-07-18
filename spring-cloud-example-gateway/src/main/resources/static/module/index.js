(function(angular){
	'use strict';
	define(['app','echart','BMap'], function (app, echarts, BMap) {
		app.register.controller('IndexController', ['$rootScope', '$scope', '$resource', '$filter', '$timeout', '$window', '$location', '$interval', '$sce', '$http', function($rootScope, $scope, $resource, $filter, $timeout, $window, $location, $interval, $sce, $http){
			console.log('index init...');
		}]);
	});
})(window.angular);