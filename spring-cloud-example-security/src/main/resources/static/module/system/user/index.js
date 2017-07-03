(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('UserController', ["$rootScope", "$scope", "$http", "$resource", "$q", function($rootScope, $scope, $http, $resource, $q){
			var User = $resource("user/:id", { "id" : "@id" }, {
				roles : { method : 'GET', url : 'role/list', isArray: true},
				validateUsername : {method : 'GET', url : 'user/checkname/:name'},
				get : { method : 'GET'},
				save : { method : 'PUT'},
				remove : { method : 'DELETE'}
			});
			var selected = {"alias": "全部"};
			$scope.searchForm = {
				"roles":[selected],
				"data":{
					"authorities":[selected]
				},
				submit: function(){
					$scope.searchForm.data = angular.copy($scope.searchForm.data);
				}
			};
			$scope.info = function(target){
				$scope.dialog.editabled = false;
				$scope.dialog.increased = false;
				$scope.dialog.title = "用户详情";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.show();
			};
			$scope.add = function(){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = true;
				$scope.dialog.title = "新增用户";
				$scope.dialog.data = {
					"authorities" : []
				};
				$scope.dialog.show();
			};
			$scope.edit = function(target){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = false;
				$scope.dialog.title = "修改用户";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.data.id = target.username;
				$scope.dialog.show();
			};
			$scope.remove = function(target){
				$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该用户信息?',callback:function(){
                    User.remove({"id":target.username},function(response){
                    	$scope.pagination.remove(target);
                    });
				}}).show();
			};
			
			$scope.$on('dialog-completed', function(e, dialog, element){
				User.roles(function(response){
					$scope.searchForm.roles = $scope.searchForm.roles.concat(response);
					$scope.dialog.roles = response;
					
				});
				dialog.validateUsername = function(username){
					return $q(function(resolve, reject) {
						if(username === undefined || username.length < 11){
							reject();
						}else if(username !== $scope.dialog.data.username){
							User.validateUsername({'name':username}, function(response){
								response.state === true ? reject(): resolve();
							});
						}
					});
				};
				dialog.submit = function(event){
					event.preventDefault();
					User.save($scope.dialog.data,function(response){
						if($scope.dialog.increased){
							$scope.pagination.unshift(response);
						}else{
							$scope.pagination.refresh(response, {"username":response.username});
						}
                    });
					$scope.dialog.close();
				};
			});
		}]);
	});
})(window.angular);