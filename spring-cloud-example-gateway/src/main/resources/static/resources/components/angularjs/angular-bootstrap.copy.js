angular.module('ui.bootstrap.copy', [])
.directive('copy', function() {
	return {
		restrict : 'C',
		require: ['?ngModel'],
		link: function(scope, element, attrs, ctrls){
			new Clipboard('.copy', {
				text: function() {
		            return ctrls[0].$modelValue;
		        }
			});
		}
	};
});
