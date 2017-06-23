'use strict';
define(['app'], function (app) {
	app.register.controller('UserPasswdController', function($scope, $http, $timeout, $q){
		$scope.userpasswd = {};
		$scope.submit = function(event){
			$http.put('/authority/user/center/updatepwd', $scope.userpasswd).success(function(response){
				$scope.$confrim.init({content : response.message, callback : function(){
					if(response.state){
						angular.forEach($scope.form.$$success.required, function(n){
							$scope.form[n.$name].$invalid = true;
						});
						angular.element("form")[0].reset();
					}else{
						$scope.form.oldpasswd.$invalid = true;
					}
				}}).show();
			});
		}
	});
});