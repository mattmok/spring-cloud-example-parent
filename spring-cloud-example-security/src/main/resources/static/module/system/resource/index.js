(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('ResourceController',['$scope', '$filter', '$http','$resource', '$timeout',function($scope, $filter, $http,$resource, $timeout){
			var Resources = $resource("authority/resource/page", {},{
				getList : {method : 'POST',isArray:true}
			});
			
			Resources.getList({}, function(response){
				$scope.list = response;
            });
			
			$scope.dialogShow = function(event, target, index){
				event.preventDefault();
				var $button = angular.element(event.currentTarget);
				$scope.dialog.editabled = $button.data("editabled");
				$scope.dialog.title = $button.data("title");
				$scope.dialog.action = $button.attr("href")||$button.data("action");
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.modified = index;
				$scope.dialog.show();
			};
			
			
			$scope.deleteObj = function(event, target, index){
				event.preventDefault();
				var $button = angular.element(event.currentTarget);
				var action = $button.attr('href')||$button.data("href");
				if(action){
					$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该资源信息?', callback : function(){					//弹出确认对话框
						$http({'method' : 'DELETE', 'url' : action}).success(function(response){
							$scope.list.splice($scope.list.indexOf(target), 1);
						});
					}}).show();
				}
			};
			
			
			
			$scope.$on('dialog-completed', function(e, dialog, element){
				dialog.submit = function(event){
					event.preventDefault();
					var $button = angular.element(event.currentTarget);
					var action = $button.attr("href")||$button.data("action")||$scope.dialog.action;
					if(action && $scope.dialog.data){
						$http.put(action, $scope.dialog.data).success(function(response){
							if($scope.dialog.modified === undefined){
								$scope.list.push(response);
								$scope.list = $filter('orderBy')($scope.list, "orderNo", false);
							}else{
								$scope.list[$scope.dialog.modified] = response;
							}
							$scope.dialog.close();
						});
					}
				};
			});
		}]);
	});
})(window.angular);