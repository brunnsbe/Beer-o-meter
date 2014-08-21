(function () {
   'use strict';

	var services = angular.module('beerOMeter.services',
		['ngResource']);

	services.factory('Country', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/countries/:id', {id: '@id'});
	}]);
	services.factory('District', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/districts/:id', {id: '@id'});
	}]);
	services.factory('Party', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/parties/:id', {id: '@id'});
	}]);
	services.factory('Candidate', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/candidates/:id', {id: '@id'});
	}]);

	services.factory('CandidateLoader', ['Candidate', '$route', '$q', '$window', 
		function(Candidate, $route, $q, $window) {
	  return function() {
		var delay = $q.defer(),
			id = $window.id;
			
		Candidate.get({id: id}, function(candidate) {
		  delay.resolve(candidate);
		}, function() {
		  delay.reject('Unable to fetch candidate '  + id);
		});
		return delay.promise;
	  };
	}]);

	services.factory('Question', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/questions/:id', {id: '@id', key: $window.key, CandidateId: $window.id});
	}]);

	services.factory('QuestionsLoader', ['Question', '$route', '$q', '$window', 
		function(Question, $route, $q, $window) {
	  return function() {
		var delay = $q.defer(),
			id = $window.id;
			
		Question.query({}, function(questions) {
		  delay.resolve(questions);
		}, function() {
		  delay.reject('Unable to fetch candidate '  + id);
		});
		return delay.promise;
	  };
	}]);

	services.factory('LanguageKey', ['$resource', '$window',
		function($resource, $window) {
			return $resource('api/v1/languagekeys/');
	}]);

	services.factory('customLoader', ['$q', 'LanguageKey',
		function ($q, LanguageKey) {
			// return loaderFn
			return function (options) {
				var delay = $q.defer();	
				LanguageKey.query({ lang: options.key }, function (languageKeys) {
					var data =  {};
					angular.forEach(angular.fromJson(languageKeys), function (value, key) {
						data[value.name] = value.data;
					});
				
					return delay.resolve(data);
				}, function() {
					delay.reject('Unable to fetch language keys');
				});
			
				return delay.promise;
			};
	}]);
}());