(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('ResourceController',['$scope', '$filter', '$http','$resource', '$timeout',function($scope, $filter, $http,$resource, $timeout){
			var Resources = $resource("resource/:id", {"id":"@id"},{
				list : {method : 'POST',isArray:true, url: "resource/page"},
				save : { method : 'PUT'},
				remove : { method : 'DELETE'}
			});
			
			Resources.list({}, function(response){
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
			$scope.info = function(target){
				$scope.dialog.editabled = false;
				$scope.dialog.increased = false;
				$scope.dialog.title = "资源详情";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.show();
			};
			$scope.add = function(item){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = true;
				$scope.dialog.title = item?"新增子资源":"新增资源";
				$scope.dialog.data = {};
				if(item && item.id != undefined){
					$scope.dialog.data.parentId = item.id;
				}
				$scope.dialog.show();
			};
			$scope.edit = function(target){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = false;
				$scope.dialog.title = "修改资源";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.show();
			};
			
			$scope.remove = function(target){
				$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该资源信息?', callback : function(){
					Resources.remove({"id": target.id},function(response){
						$scope.list.splice($scope.list.indexOf(target), 1);
					});
				}}).show();
			};
			$scope.$on('dialog-completed', function(e, dialog, element){
				dialog.submit = function(event){
					event.preventDefault();
					Resources.save($scope.dialog.data,function(response){
						if($scope.dialog.increased){
							$scope.list.push(response);
							$scope.list = $filter('orderBy')($scope.list, "orderNo", false);
						}else{
							var target = $filter('filter')($scope.list, {"id":response.id}, true)[0];
							if(target !== undefined){
								$scope.list[$scope.list.indexOf(target)] = response;
							}
						}
                    });
					$scope.dialog.close();
				};
			});
		}]);
	});
})(window.angular);