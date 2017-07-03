(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('RoleController',[ "$scope", "$resource", "$q",function($scope, $resource, $q){
			var selected = {name : "全部"};
			var Role  = $resource("role/:authority", { "authority": "@authority", "resourceid": "@resourceid"}, {
				save: { method : 'PUT'},
				remove: { method : 'DELETE'},
				validateAuthority: {method : 'GET', url : 'role/check/:authority'},
				getAuthorities: {method : 'GET', url : 'role/auth/:authority', isArray: true},
				saveAuthority: {method : 'PUT', url : 'role/auth/:authority/:resourceid'},
				deleteAuthority: {method : 'DELETE', url : 'role/auth/:authority/:resourceid'}
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
				$scope.dialog.data = {};
				$scope.dialog.show();
			}
			$scope.edit = function(target){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = false;
				$scope.dialog.title = "修改角色";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.show();
			}

			$scope.remove = function(target){
				$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该该角色信息?',callback:function(){
					Role.remove({"authority":target.authority},function(response){
                    	$scope.pagination.remove(target, {"alias":target.alias});
                    });
				}}).show();
			};
		    var resourceTree;
			$scope.auth = function(target){
				$scope.authDialog.title = "权限设置";
				if(resourceTree){
					resourceTree.checkNode(resourceTree.getNodesByFilter(function(node){
						return node.level == "0";
					},true), false, true);
				}
				Role.getAuthorities({"authority":target.authority},function(response){
					resourceTree = $.fn.zTree.init($("#resourceTree"), {
						check: {enable: true},
						data: { simpleData: { enable: true } },
						callback: {
							onCheck: function(event, treeId, treeNode){
								var parameters = {"authority": target.authority, "resourceid": treeNode.id};
								treeNode.checked? Role.saveAuthority(parameters): Role.deleteAuthority(parameters);
							}
						}
					}, response);
			    	$scope.authDialog.show();
				});
			};
			$scope.$on('dialog-completed', function(e, dialog, element){
				dialog.validate = function(value){
					return $q(function(resolve, reject) {
						if(value === undefined || value.length < 3){
							reject();
						}else if(value !== $scope.dialog.data.authority){
							Role.validateAuthority({'authority': value}, function(response){
								response.state === true ? reject(): resolve();
							});
						}
					});
				};
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