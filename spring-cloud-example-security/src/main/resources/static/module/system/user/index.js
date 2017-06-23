(function(angular) {
	'use strict';
	define(['app'], function (app) {
		app.register.controller('UserController', ["$rootScope", "$scope", "$http", "$resource", "$timeout","$q", function($rootScope, $scope, $http,$resource, $timeout, $q){
			var User = $resource("authority/user/:id", { "id" : "@id" }, {
				init : { method : 'GET', url : 'authority/user/init'},
				validateUsername : {method : 'GET', url : 'authority/user/checkname/:name'},
				get : { method : 'GET'},
				save : { method : 'PUT'},
				remove : { method : 'DELETE'}
			});
			var Authority = $resource("authority/:id", { "id" : "@id" }, {
				init : {method : 'GET'},
				save : {method : 'PUT'},
				remove : {method : 'POST'}
			});
			
			var user = User.init();
			
			var selectedRole = {"alias": "全部"};
			$scope.searchForm = {
				"roles":[selectedRole],
				"data":{
					"authorities":[selectedRole]
				},
				submit: function(){
					$scope.searchForm.data = angular.copy($scope.searchForm.data);
				}
			};
			
			$scope.info = function(target){
				$scope.dialog.editabled = false;
				$scope.dialog.increased = false;
//				$scope.dialog.hasUpdateDataAuthorityPower = $scope.operator.setting.hasUpdateDataAuthorityPower;
				$scope.dialog.title = "用户详情";
				$scope.dialog.data = angular.copy(target);
				/*if(target.setting === undefined){
					$scope.dialog.data.setting = {};
				}*/
				$scope.dialog.show();
			};
			$scope.add = function(){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = true;
//				$scope.dialog.hasUpdateDataAuthorityPower = $scope.operator.setting.hasUpdateDataAuthorityPower;
				$scope.dialog.title = "新增用户";
				$scope.dialog.data = {
					"authorities" : [],
					"setting":{
						"displayName":$scope.dialog.displayNames[0]
					}
				};
				$scope.dialog.show();
			};
			$scope.edit = function(target){
				$scope.dialog.editabled = true;
				$scope.dialog.increased = false;
//				$scope.dialog.hasUpdateDataAuthorityPower = $scope.operator.setting.hasUpdateDataAuthorityPower;
				$scope.dialog.title = "修改用户";
				$scope.dialog.data = angular.copy(target);
				$scope.dialog.data.id = target.username;
				/*if(target.setting === undefined){
					$scope.dialog.data.setting = {};
				}*/
				$scope.dialog.show();
			};
			$scope.remove = function(target){
				$scope.$confrim.init({title:'警告',type:'warning',content : '是否要删除该用户信息?',callback:function(){
                    User.remove({"id":target.username},function(response){
                    	$scope.pagination.remove(target);
                    });
				}}).show();
			};
			
			var dataAuthorityTree;
			$scope.auth = function(event, target){
				event.preventDefault();
				var $button = angular.element(event.currentTarget);
				$scope.authDialog.title = $button.data("title");;
				if(dataAuthorityTree){
					dataAuthorityTree.checkNode(dataAuthorityTree.getNodesByFilter(function(node){
						return node.level == "0";
					},true), false, true);
				}
				
				$http.get('api/authority/authoritiesTree/' + target.username).success(function(response){
					dataAuthorityTree = $.fn.zTree.init($("#dataAuthorityTree"), {
						check: {enable: true},
						data: { simpleData: { enable: true } },
						callback: {
							onCheck: function(event, treeId, treeNode){
								$http({'method' : treeNode.checked?'PUT':'DELETE', 'url' : 'api/authority/authoritiesTree/' + target.username + "/" + treeNode.id+ "/" +treeNode.level}).success(function(response){});
							}
						}
					}, response);
			    	$scope.authDialog.show();
			    });
			};
			
			$timeout(function(){
				User.init(function(response){
					$scope.searchForm.roles = $scope.searchForm.roles.concat(response.roles);
					$scope.dialog.displayNames = response.displayNames;
					$scope.dialog.roles = response.roles;
				});
				$scope.dialog.validateUsername = function(username){
					return $q(function(resolve, reject) {
						if(username === undefined){
							reject();
						}else if(username !== $scope.dialog.data.username){
							User.validateUsername({'name':username}, function(response){
								response.state === true ? reject(): resolve();
							});
						}
					});
				};
				$scope.dialog.submit = function(event){
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
			}, 1000);
		}]);
	});
})(window.angular);