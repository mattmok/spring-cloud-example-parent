require.config({
    baseUrl: '/gateway/',
    paths:{
    	'async':'resources/components/angularjs/async',
    	'echart':'resources/components/echarts/dist/echarts.min',
    	'SuperMap':'resources/components/supermap/libs/SuperMap',
    	'BMap':'resources/components/js/BMap',
    	'FlipClock':'resources/components/js/FlipClock',
    	'jqueryui':'resources/components/jqueryui/jqueryui'
    },
    map: {
        '*': {
            'css': 'resources/components/angularjs/require-css.min'
        }
    },
    urlArgs: 'ver=2.0'
});
require.onError = function (error) {
	console.error(error);
    if (error.requireType === 'scripterror') {
    	window.location.href = error.originalError.target.baseURI + 'index.html';
    }
};
require([ 'app', 'index' ], function() {
	angular.bootstrap(document.body, ['app']);
});