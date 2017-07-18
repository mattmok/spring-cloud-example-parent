(function(angular) {
	'use strict';
	define([], function () {
	    var app = angular.module('app', [ 'ngRoute', 'ngResource', 'ngCookies', 'LocalStorageModule',
			'ngAnimate', 'ngSanitize', 'ui.bootstrap',
			'ui.bootstrap.pagination2',
			'ui.bootstrap.dialog',
			'ui.bootstrap.copy',
			'ui.validate',
			'ui.select']);
		app.filter('propsFilter', function() {
			return function(items, props) {
				var out = [];
				if (angular.isArray(items)) {
					var keys = Object.keys(props);
					items.forEach(function(item) {
						var itemMatches = false;
						for (var i = 0; i < keys.length; i++) {
							var prop = keys[i];
							var text = props[prop].toLowerCase();
							if (item[prop] && item[prop].toString().toLowerCase()
									.indexOf(text) !== -1) {
								itemMatches = true;
								break;
							}
						}
						if (itemMatches) {
							out.push(item);
						}
					});
				} else {
					// Let the output be the input untouched
					out = items;
				}
				return out;
			};
		});
		app.filter('busFilter', function() {
			return function(items, props) {
				var out = [];
				var key = props['key'];
				var name = props['name'];
				if (angular.isArray(items) && name && name.length > 0) {
					items.forEach(function(item) {
						if((item[key] && item[key].toString().toLowerCase().indexOf(name.toLowerCase()) !== -1)||
								(item['name'] && item['name'].toString().toLowerCase().indexOf(name.toLowerCase()) !== -1)){
							out.push(item);
						}
					});
				} else {
					out = items;
				}
				return out;
			};
		});
		app.factory('$exceptionHandler', function($log) {
			return function(exception, cause) {
//				exception && $log.error(exception);
//				cause && $log.error(cause);
			};
		});
		app.service('$webSocketClient',['$interval', '$log', function($interval, $log){
			var stompClient;
	        this.connect = function(mapping, topics) {
	        	var socket = new SockJS(mapping);
	            stompClient = Stomp.over(socket);
	            stompClient.debug = undefined;
	            stompClient.connect({}, function(frame) {
	            	if(angular.isArray(topics)){
	            		angular.forEach(topics, function(topic, key) {
			                stompClient.subscribe(topic.name, topic.callback);
            			});
	            	}else{
	            		stompClient.subscribe(topics.name, topics.callback);
	            	}
	            });
	            return stompClient;
			};
			this.sendMessage = function(mapping, parameters){
				if (stompClient != null) {
					stompClient.send(mapping, {}, JSON.stringify(parameters));
				}else{
					$log.error('Connection refused ...');
				}
			};
			this.disconnect = function() {
				if (stompClient != null) {
	                stompClient.disconnect();
	            }
			};
	    }]);
	    app.config([
			'$controllerProvider',
			'$locationProvider',
			'$compileProvider',
			'$filterProvider',
			'$routeProvider',
			'$provide',
			'$httpProvider',
			'localStorageServiceProvider',
			'$cookiesProvider',
			function($controllerProvider, $locationProvider, $compileProvider, $filterProvider, $routeProvider, $provide, $httpProvider, localStorageServiceProvider, $cookiesProvider) {
				app.register = {
					controller: $controllerProvider.register,
					directive: $compileProvider.directive,
					filter: $filterProvider.register,
					factory: $provide.factory,
					service: $provide.service
				};
				localStorageServiceProvider.setPrefix('app').setStorageType('sessionStorage');
		    	$locationProvider.html5Mode(true);
		    	$httpProvider.defaults.withCredentials = true;
//		    	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
//		    	$httpProvider.defaults.headers.common['Accept'] = 'application/json';
//		    	$httpProvider.defaults.transformResponse.push(function(data, headersGetter, status){
//		    		if(status === 500){
//		    			if(data.exception === "com.netflix.zuul.exception.ZuulException"){
//		    				return [data];
//		    			}
//		    		}
//		    		return data;
//    			});
		    	$httpProvider.interceptors.push(function($rootScope, $location, $cookies){
		    		return {
		    			request:function(config){
		    				var i = config.url.lastIndexOf('.');
		    				if(i === -1){
		    					config.headers["Accept"] = 'application/json';
		    				}
		    				return config;
		    			},
		    			response: function(response){
		    				if(response.config.url === '/login.html'){
		    					window.location.href = "/login.html";
		    				}else{
		    					return response;
		    				}
		    			},
		    			responseError:function(response){
		    				if(response.status === 404 && response.config.url.lastIndexOf('.') !== -1){
		    					$location.url("build.html");
		    				}else if(response.status === 440){
		    					window.location.href = require.toUrl('login.html');
		    				}else{
		    					let data = response.data[0]||response.data;
	    						$rootScope.$confrim.init(data.message || 'Internal Server Error').show();
		    				}
//		    				return response;
		    			}
		    		};
		    	});
		        $routeProvider
		        .when('/:path*', {
					templateUrl : function($route) {
						if($route.path){
							return 'module/' + $route.path;
						}
					},
					resolve : {
						delay:function($q, $route, $rootScope, $timeout) {
							var dependencies = ['module/'+ $route.current.params.path.replace(/\.html$/ig,".js")];
							var delay = $q.defer();
	                		require(dependencies, function () {
	                			delay.resolve();
	                			$rootScope.$apply();
	                		});
		                    return delay.promise;
		                }
					}
				}).otherwise({
					redirectTo : "/index.html"
				});
		    }
		]);
	    return app;
	});
})(window.angular);