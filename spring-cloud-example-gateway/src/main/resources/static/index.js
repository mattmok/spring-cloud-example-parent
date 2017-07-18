(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.controller('InitController',[
			'$rootScope',
			'$scope',
			'$timeout',
			'$resource',
			'$filter',
			'$document',
			'$interval',
			'localStorageService',
		    function($rootScope, $scope, $timeout, $resource, $filter, $document, $interval, localStorageService) {
			$rootScope.start = {
				"time" : new Date(),
				"open": function($event) {
					$rootScope.start.opened = true;
				}
			};
			$rootScope.end = {
				"open": function($event) {
				    $rootScope.end.opened = true;
				},
				"maxDate":new Date(),
				"time": new Date()
			};
			
			$rootScope.width = angular.element(document).width();
			$rootScope.height = angular.element(document).height() - ($rootScope.width > 768?74:50);
			$rootScope.activeItem = function(event){
				$(event.currentTarget).siblings(":not(this)").removeClass("active");
				$(event.currentTarget).addClass("active");
			};
			var selected = {"name": '全部'};
			$rootScope.stompClient;
			$rootScope.alerts = [];
			$rootScope.menus = [];
			$rootScope.operator = {};
			$rootScope.provinces = [selected];
			$rootScope.cities = [selected];
			$rootScope.districts = [selected];
			$rootScope.stations = [selected];

			
			$scope.$on('$destroy', function() {
				if ($rootScope.stompClient) {
					$rootScope.stompClient.disconnect();
	            }
				localStorageService.clearAll();
	        });
			$scope.$on('$viewContentLoaded',function(){
	        	if($rootScope.$confrim){
					$rootScope.$confrim.close();
	            }
	        	if($rootScope.width > 768){
	        		$(".nano").nanoScroller({ scroll: 'top' });
	        	}
	        });
		}]);
	});
})(window.angular);