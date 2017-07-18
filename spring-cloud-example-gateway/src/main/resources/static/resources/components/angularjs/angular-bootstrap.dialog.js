angular.module('ui.bootstrap.dialog', [])
.controller('DialogController',['$scope', '$element', function($scope, $element) {
	this.show = function(){
		$scope.dialog.state = true;
		$element.niftyModal('show', {afterClose:function(){
			$scope.$apply(function(){
				$scope.dialog.state = false;
				$element.css({"z-index": -2000});
			});
		}});
		if(angular.element('.nano', $element).length > 0){
			angular.element('.nano', $element).nanoScroller({ scroll: 'top' });
		}
		$element.css({"z-index": 2000});
	};
	this.close = function(){
		$element.niftyModal('hide');
		$scope.dialog.state = false;
	};
	$scope.dialog = this;
}])
.directive('dialog', ['$timeout',function($timeout) {
	return {
		restrict : 'A',
		templateUrl : function(element, attrs) {
			return attrs.templateUrl || '';
		},
		controller : 'DialogController',
		transclude : true,
		link: function(scope, element, attrs, ctrls){
			attrs.$set('role', 'dialog');
			attrs.$set('aria-hidden', true);
			var mdeffect = "md-effect-9";
			if(element.attr('class') != undefined){
				var effect;
				if((effect = /md\-effect\-\d+/.exec(element.attr('class'))) != null){
					mdeffect = effect[0];
				}
			}
			element.css({'max-width':element[0].style.width, "left": "160%", "z-index": -2000});
			angular.element(".md-content", element).css({'max-width':element[0].style.width});
			attrs.$addClass("md-modal " + mdeffect);
			if(element.parent().children('.md-overlay').length === 0){
				element.parent().append('<div class="md-overlay"></div>');
			}
			$timeout(function(){
				element.parent().children('.md-overlay').height(angular.element(window).height());
				element.css({"left": ""});
			}, 1000);
			
			scope.$on('$destory', function(){
				ctrls.close();
			});
			scope.$emit(attrs.dialog + '-completed', ctrls, element);
			
			scope.$watch('dialog.state', function(status){
				if(status !== undefined){
					if(status){
						scope.$emit(attrs.dialog + '-opened', ctrls, element);
					}else{
						scope.$emit(attrs.dialog + '-closed', ctrls, element);
						$(".md-overlay").css("z-index","1999");
					}
				}
            });
		},
		scope: {
			dialog:'=?'
		}
	};
}])
.controller('ConfirmController',[ '$rootScope', '$scope', '$element', function($rootScope, $scope, $element) {
	$rootScope.$confrim = this;
	this.init = function(prop){
		this.title = prop.title || '警告';
		this.content = prop.content || prop;
		this.callback = prop.callback;
		var type = prop.type || 'warning';
		this.type = type;
		if ("success" == type) {
			this.iconType = "fa-smile-o";
		} else if ("danger" == type) {
			this.iconType = "fa-frown-o";
		} else {
			this.iconType = "fa-"+type;
		}
		this.btnType = "btn-"+type;
		$(".md-overlay").css("z-index","2001");
		return this;
	};
	this.show = function(){
		$element.niftyModal('show', {afterClose:function(){
			$scope.$apply(function(){
				$(".md-overlay").css("z-index","1999");
			});
		}});
	};
	this.close = function(){
		$element.niftyModal('hide');
	};
}])
.directive('confrim', function() {
	return {
		restrict : 'E',
		controller : 'ConfirmController',
		templateUrl : function($element, $attrs) {
			return $attrs.templateUrl || "resources/components/angularjs/template/alert/confrim.html";
		},
		replace : true,
		transclude : true,
		link: function(scope, element, attrs){
			if(angular.element('.md-overlay').length === 0){
				element.parent().append('<div class="md-overlay" style="z-index: 1999;"></div>');
			}
		}
	};
}).directive('icheck', [ '$timeout', '$parse', function($timeout, $parse) {
	return {
		restrict : 'AE',
		require: ['ngModel'],
		link : function(scope, element, attrs, ctrls) {
			var ngModel = ctrls[0];
			var containerWidth = attrs.title ? attrs.labelWidth || 58 : 28;
			var width = attrs.title ? (attrs.labelWidth || 58) - 26 : 2;
			var container = angular.element("<div class='checkbox-container'>");
			var labelContainer = angular.element(attrs.id?"<div class='checkbox-label' for='"+attrs.id+"'>":"<div class='checkbox-label'>");
			element.wrap(container.width(containerWidth)).after(labelContainer.css({"width":width}).text(attrs.title));
			element.iCheck({
				checkboxClass : 'icheckbox_square-blue',
				increaseArea : '20%'
			}).on('ifChanged', function(event) {
                if ($(element).attr('type') === 'checkbox' && ngModel) {
                	scope.$apply(function() {
                		ngModel.$setViewValue(event.target.checked);
                    });
                }
                if ($(element).attr('type') === 'radio' && ngModel) {
                    scope.$apply(function() {
                    	ngModel.$setViewValue(value);
                    });
                }
            });
			
			scope.$watch(attrs['ngModel'], function(newValue){
				if(newValue !== undefined){
					$(element).iCheck('update');
				}
            });
			scope.$watch(attrs['title'], function(newValue){
				if(newValue !== undefined){
					container.css({"width": attrs.labelWidth || 58});
					labelContainer.css({"width":(attrs.labelWidth || 58) - 26}).text(newValue);
				}
            });
		}
	};
}])
.directive('msg', ['$timeout',function($timeout) {
	return {
		restrict : 'AE',
		transclude: true,
		replace: true,
		templateUrl : function(element, attrs) {
			return "resources/components/angularjs/template/alert/div.html";
		},
		link: function(scope, element, attrs){
			console.log("2");
			console.log(scope.form['name'].$invalid);
			scope.$msg = this;
			this.fieldError = scope.form['name'].$invalid;
			this.fieldSuccess = scope.form['name'].$valid;
			scope.$watch(function() { return element.find("input").val(); }, function (newValue, oldValue) {
				if (newValue !== oldValue) {
					console.log(scope.form['name']);
					console.log(scope.form['name'].$invalid);
					scope.$msg = this;
					this.fieldError = scope.form['name'].$invalid;
					this.fieldSuccess = scope.form['name'].$valid;
				}
			});
		}
	};
}]);
