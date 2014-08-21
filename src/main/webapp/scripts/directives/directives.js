(function () {
   'use strict';

	var directives = angular.module('beerOMeter.directives', []);

	directives.directive('resizable', ['$window', function($window) {
		return {
			link: function postLink(scope, element, attrs) {

			  scope.onResizeFunction = function() {
				angular.element(document.getElementsByClassName('panel-body')).css('height', ($window.innerHeight - 190) + "px");
			  };

			  // Call to the function when the page is first loaded
			  scope.onResizeFunction();

			  angular.element($window).bind('resize', function() {			  
				scope.onResizeFunction();
			  });
			}
		  };		
	}]);
}());