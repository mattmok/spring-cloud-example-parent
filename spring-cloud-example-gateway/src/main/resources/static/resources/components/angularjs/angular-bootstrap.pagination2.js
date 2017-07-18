angular.module('ui.bootstrap.pagination2', ['ui.bootstrap.pagination','ngResource','LocalStorageModule'])
.controller('Pagination2Controller',[ '$scope', '$parse', '$attrs', '$resource', '$filter', 'localStorageService',function($scope, $parse, $attrs, $resource, $filter, localStorageService) {
	var self = this;
	$scope.itemsPerPage = parseInt($attrs.itemsPerPage || 20, 10);
	this.hasResults = function(){
		return $scope.list && $scope.list.length > 0;
	};
	this.getOriginalPage = function(page){
		if(localStorageService.isSupported && $attrs.itemsPerPage !== undefined){
			return page > 1 && $scope.page.pageSize !== undefined ? Math.ceil(page * $scope.itemsPerPage / $scope.page.pageSize) : 1;
		}else{
			return page;
		}
	};
	
	this.getCacheKey = function(n){
		return 'scope:'+$scope.$id + ":list["+ n +"]";
	};
	
	this.getCacheValue = function(n){
		if(localStorageService.isSupported){
			return localStorageService.get(self.getCacheKey(n));
		}
	};
	
	this.setCacheValue = function(array, page){
		var size = $attrs.itemsPerPage === undefined ? 1 : Math.ceil(array.length / $scope.itemsPerPage);
		for(var i = 1;i <= size;i++){
			var _array = $attrs.itemsPerPage === undefined ? array : angular.copy(array).splice(0,$scope.itemsPerPage);
			if(_array && _array.length > 0){
				localStorageService.set(self.getCacheKey(Number((page - 1) * ($scope.page.pageSize/$scope.itemsPerPage) + i)), _array);
			}
		}
	}
	
	this.clearAllCache = function(){
		localStorageService.clearAll('^scope:'+$scope.$id+':list\[\+?[0-9]+\]*$');
	};
	
	this.pageChanged = function(page, parameters, total){
		var _page = self.getOriginalPage(page);
		$scope.loading = true;
		$resource($attrs.uri + ":page" + "/" + ":size", {"page":_page, "size":$scope.itemsPerPage},{query:{method:'POST',responseType:'json'}}).query(parameters).$promise.then(function(response){
			if(_page == 1){
				$scope.page = response;
			}
			if(localStorageService.isSupported){
				self.setCacheValue(response.list, _page);
				$scope.list = self.getCacheValue(page);
			}else{
				$scope.list = response.list;
			}
			$scope.loading = false;
		});
	};
	
	this.checkedAll = function(checked){
		if(checked){
			if($scope.list && $scope.list.length > 0){
				angular.forEach($scope.list, function(item){
					$scope.checkedItems.push(item.id);
				});
				if($attrs.checkAllUri){
					$resource($attrs.checkAllUri,{},{query:{method:'POST',responseType:'json', isArray: true}}).query($scope.parameters).$promise.then(function(response){
						$scope.checkedItems = response;
					});
				}
			}
		}else{
			$scope.$apply(function(){
				$scope.checkedItems = [];
			});
		}
	};
	this.checkedItem = function(checked, value){
		var i = $scope.checkedItems.indexOf(value);
		if(checked && i === -1){
			$scope.$apply(function(){
				$scope.checkedItems.push(value);
			});
		}
		if(!checked && i > -1){
			$scope.$apply(function(){
				$scope.checkedItems.splice(i , 1);
			});
		}
	};
	
	var indexOf = function(item, expression){
		if(item !== undefined && angular.isObject(item) && !angular.isArray(item)){
			if(expression === undefined && item.id !== undefined){
				expression = {"id":item.id};
			}
			var target = $filter('filter')($scope.list, expression, true)[0];
			if(target !== undefined){
				return $scope.list.indexOf(target);
			}else{
				throw 'no matching object found';
			}
		}
		return -1;
	};
	
	this.refresh = function(item, expression){
		var i = indexOf(item, expression);
		if(i !== -1){
			$scope.list[i] = item;
			self.setCacheValue($scope.list, $scope.page.pageNum);
		}
	};
	this.unshift = function(item){
		$scope.page.total = $scope.page.total + 1;
		var array = self.getCacheValue(1) || [];
		array.unshift(item);
		self.clearAllCache();
		self.setCacheValue(array.slice(0, 20), 1);
		$scope.page.pageNum = 1;
		$scope.pageChanged();
	};
	
	this.push = function(item){
		$scope.page.total = $scope.page.total + 1;
		$scope.page.pageNum = $scope.page.pages;
		if($scope.page.total % $scope.page.pageSize == 1){
			$scope.page.pageNum = $scope.page.pages + 1;
		}
		var array = self.getCacheValue($scope.page.pageNum);
		if(array){
			array.push(item);
			self.setCacheValue(array, $scope.page.pageNum);
		}
		$scope.pageChanged();
	};
	
	this.remove = function(item, expression){
		var i = indexOf(item, expression);
		if(i > -1){
			var last = $scope.page.pageNum == $scope.page.pages;
			var array = self.getCacheValue($scope.page.pageNum) || [];
			if(array.length > 0){
				array.splice(i, 1);
				if(last){
					if(array.length == 0){
						$scope.page.pageNum = $scope.page.pages - 1;
						localStorageService.remove(self.getCacheKey($scope.page.pages));
					}else{
						self.setCacheValue(array, $scope.page.pages);
					}
				}else{
					self.clearAllCache();
				}
			}
			$scope.page.total = $scope.page.total - 1;
			$scope.pageChanged();
		}
	};
	this.removeAll = function(){
		if($scope.list){
			$scope.list = undefined;
		}
		if($scope.checkedItems){
			$scope.checkedItems = undefined;
		}
		self.clearAllCache();
	};
	
	$scope.pagination = this;
	$scope.page = {"pageNum": 1};
	$scope.$watch('parameters', function(parameters) {
		if(!parameters){
			return;
		}
		if($scope.checkedItems){
			$scope.checkedItems = [];
		}
		self.clearAllCache();
		self.pageChanged(1, parameters);
	});
	
	
	$scope.pageChanged = function(){
		var list = self.getCacheValue($scope.page.pageNum);
		if(list){
			$scope.list = list;
			return;
		}
		self.pageChanged($scope.page.pageNum, $scope.parameters, $scope.page.pageSize);
	};

	$scope.$on('$destroy', function(){
		self.clearAllCache();
	});
}]).directive('pagination2',['$parse' ,function($parse) {
	return {
		restrict : 'EA',
		require: ['pagination2'],
		controller : 'Pagination2Controller',
		templateUrl : function($element, $attrs) {
			return $attrs.templateUrl||'uib/template/pagination/pagination-responsive.html';
		},
		transclude : true,
		replace : true,
		link: function(scope, element, attrs, ctrls){
			var ctrl = ctrls[0];
			var lineHeight = scope.itemsPerPage * 28;
			var colspan = $("table > thead > tr", element).children().length;
			var norecords = angular.element("<tr class='norecordss'><td colspan='"+colspan+"' class='norecords'>暂无数据</td></tr>");
			var width = $(element).width();
			if(width === 0){
				angular.forEach($(element).parentsUntil(), function(k,v){
					width = $(this).width();
					if(width > 0){
						return;
					}
				});
			}
			var left = width/2 - 10;
			$(".norecords", norecords).css({'line-height':lineHeight - 10 + 'px','padding-left':left});
			$('.table-responsive', element).css({'min-height':scope.itemsPerPage * 28 + 32});
			$('.table-responsive tbody', element).append(norecords);
			
			scope.$watch('parameters', function() {
				$('thead input:checkbox', element).prop("checked", false);
			});
			
			scope.$watch('loading',function(loading){
				$('.table-responsive', element).css({'overflow':loading?'hidden':'auto'});
				$('.overlay',element).css({'height':lineHeight + 'px','line-height':lineHeight + 'px','padding-left':left});
			});
			
			scope.$watch('list',function(list){
				var overflow = list && list.length > 0?'auto':'hidden';
				var borderBottom = list && list.length < scope.itemsPerPage ? '1px solid #ddd':'none';
				$('.table-responsive', element).css({'overflow':overflow,'border-bottom': borderBottom});
				list && list.length > 0 ? norecords.hide(): norecords.show();
			});
			
			$(element).on('click', 'thead input:checkbox', function(){
				ctrl.checkedAll(this.checked);
			});
			$(element).on('click', 'tbody input:checkbox', function(){
				ctrl.checkedItem(this.checked, this.value);
			});
		},
		scope:{
			uri:'@',
			parameters:'=',
			itemsPerPage:'@',
			checkedItems:'=',
			pagination:'=?id'
		}
	};
}]).directive('orderBy', function() {
	return {
		restrict : 'A',
		template : function($element, $attrs) {
			return "<td order='order'><ng-transclude></ng-transclude><span class='pull-right glyphicon' style='margin-top:2px;'></span></td>"
		},
		transclude : true,
		replace : true,
	    controllerAs: 'order',
		link: function($scope, $element, $attrs){
			var orderby = $attrs['orderBy'];
			$element.on('click', function(){
				if(orderby){
					$scope.$apply(function(){
						$scope.order = {
							name : orderby,
							status: $scope[status] = !$scope[status]
						};
					});
					$("span.glyphicon",$element.parent().children().not($element)).removeClass('glyphicon-triangle-top glyphicon-triangle-bottom');
					if($scope.order.status){
						$('span.pull-right', $element).removeClass("glyphicon-triangle-top").addClass("glyphicon-triangle-bottom");
					}else{
						$('span.pull-right', $element).removeClass("glyphicon-triangle-bottom").addClass("glyphicon-triangle-top");
					}
				}
			});
		},
		scope:{
			order: '='
		}
	};
}).directive('editable', ['$parse', function($parse) {
	return {
		require : 'ngModel',
		template : function(element, attrs) {
			return "<span></span><input type='text' style='display:none;'>"
		},
		link: function(scope, element, attrs, ctrls){
//			console.log($parse(attrs.ngChange)(scope));
			var width = element.width();
			var padding = element.css('padding-left') + ' ' + element.css('padding-top') + ' '+ element.css('padding-right') + ' '+ element.css('padding-bottom');
			var editor = angular.element('input:text', element);
			var platform = angular.element('span', element);
			element.on('click', function(event){
				element.css({'padding':'2px 0'});
				editor.val(ctrls.$viewValue);
				editor.width(width - 2).show().focus();
				platform.hide();
			});
			editor.on('keypress', function(event){
				if(event.keyCode === 13){
					event.preventDefault();
					if(editor.val().length > 0){
						scope.$apply(function() {
							ctrls.$setViewValue(editor.val());
						});
						editor.blur();
					}
				}
			});
			editor.on('blur',function(){
				editor.hide();
				platform.show();
				element.css({'padding':padding});
				scope.$apply(function() {
					ctrls.$render();
				});
			});
			
			ctrls.$render = function() {
				platform.text(ctrls.$viewValue);
			};
			
			if(ctrls.$viewChangeListeners[0] && ctrls.$viewChangeListeners[0].$promise){
				ctrls.$viewChangeListeners[0].$promise.then(function(response){
					console.log(response);
				});
			}
			
		}
	};
}]).directive('contenteditable', ['$parse', function($parse){
	  return {
		require : 'ngModel',
		link : function(scope, element, attrs, ctrl) {
			var container = element.parent();
			element.on('click', function() {
				container.css({'padding':'0'});
				element.css({'border':'1px solid #000', 'background-color':'#fff', 'padding':'4px', height: container.height()});
			});
			element.on('blur', function() {
				scope.$apply(function() {
					ctrl.$render();
				});
				container.removeAttr('style');
				element.removeAttr('style');
				$("input:text", element).remove();
			});
			element.on('keypress', function(event){
				if(event.keyCode === 13){
					event.preventDefault();
					if(element.html().length > 0){
						scope.$apply(function() {
							ctrl.$setViewValue(element.text());
						});
						element.blur();
					}
				}
			});

			ctrl.$render = function() {
				element.empty().text(ctrl.$viewValue);
			};
		}
	};
}]);
