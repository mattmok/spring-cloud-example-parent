(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('RoleController',[ "$scope", "$timeout", "$http","$resource",function($scope, $timeout, $http,$resource){
			var selected = {name : "全部"};
			var Role  = $resource("authority/role/:id", { "id" : "@id" }, {
				get : { method : 'GET'},
				save : { method : 'PUT'},
				getTypes : { method : 'GET',url : 'authority/role/types', isArray: true},
				remove : { method : 'DELETE'}
			});
			$scope.searchForm = {
				roleTypes:[selected],
				data: {
					"type" : selected
				},
				submit: function(){
					$scope.searchForm.data = angular.copy($scope.searchForm.data);
				}
			};

			$scope.info = function(target){
				$scope.dialog.editabled = false;
				$scope.dialog.increased = false;
				$scope.dialog.title = "角色详情";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.show();
			};
			$scope.add = function(){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = true;
				$scope.dialog.title = "新增角色";
				$scope.dialog.data = {
				};
				$scope.dialog.show();
			}
			$scope.edit = function(target){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = false;
				$scope.dialog.title = "修改角色";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.data.id = target.authority
				$scope.dialog.show();
			}

			$scope.remove = function(target){
				$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该该角色信息?',callback:function(){
					Role.remove({"id":target.authority},function(response){
                    	$scope.pagination.remove(target, {"alias":target.alias});
                    });
				}}).show();
			};
		    var resourceTree;
			$scope.openAuthDialog = function(event, target){
				event.preventDefault();
				var $button = angular.element(event.currentTarget);
				$scope.authDialog.title = $button.data("title");
				if(resourceTree){
					resourceTree.checkNode(resourceTree.getNodesByFilter(function(node){
						return node.level == "0";
					},true), false, true);
				}
				$http.get('authority/role/auth/' + target.authority).success(function(response){
					resourceTree = $.fn.zTree.init($("#resourceTree"), {
						check: {enable: true},
						data: { simpleData: { enable: true } },
						callback: {
							onCheck: function(event, treeId, treeNode){
								$http({'method' : treeNode.checked?'PUT':'DELETE', 'url' : 'authority/role/auth/' + target.authority + "/" + treeNode.id}).success(function(response){});
							}
						}
					}, response);
			    	$scope.authDialog.show();
			    });
			};
			
			Role.getTypes({}, function(response){
				$scope.searchForm.roleTypes = $scope.searchForm.roleTypes.concat(response);
				$scope.dialog.roleTypes = response;
            });
			$scope.$on('dialog-completed', function(e, dialog, element){
				dialog.submit = function(event){
					event.preventDefault();
					Role.save(dialog.data,function(response){
						if($scope.dialog.increased){
							$scope.pagination.push(response);
						}else{
							$scope.pagination.refresh(response, {"authority":response.authority});
						}
	                });
					dialog.close();
				};
			});
		}]);
	});
})(window.angular);