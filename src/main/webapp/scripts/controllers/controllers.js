(function () {
   'use strict';

	var app = angular.module('beerOMeter',
		['beerOMeter.services', 'beerOMeter.directives', 'ngRoute', 'ngSanitize', 'ui.bootstrap', 'pascalprecht.translate']);

	//############################################################################################

	app.filter("nl2br", function($filter) {
		return function(data) {
			if (!data) {
				return data;
			}
			return data.replace(/\n\r?/g, '<br />');
		};
	});

	//############################################################################################

	app.config(['$routeProvider', function($routeProvider) {
		$routeProvider
		  .when('/showhelp', {
			controller: 'ShowHelpCtrl',
			templateUrl:'views/showhelp/view.html'
		  })		
		  .when('/languageselection', {
			controller: 'LanguageSelectionCtrl',
			templateUrl:'views/languageselection/view.html'
		  })
		  .when('/main', {
			controller: 'MainCtrl',
			templateUrl:'views/main/view.html'
		  })
		  .otherwise({redirectTo: '/main'});
	}]);

	//############################################################################################

	app.config(['$translateProvider', function ($translateProvider) {
		$translateProvider.useLoader('customLoader');
		$translateProvider.preferredLanguage('en');
		$translateProvider.fallbackLanguage('en');
	}]);

	//############################################################################################

	app.controller('LanguageSelectionCtrl', ['$scope', '$route', '$translate', '$location', '$modalInstance', '$modal',
		function($scope, $route, $translate, $location, $modalInstance, $modal) {
			$scope.selectLanguage = function(lang) {
				$translate.use(lang);
				$modalInstance.close();

				var modalInstance = $modal.open({
					controller: 'ShowHelpCtrl',
					templateUrl:'views/showhelp/view.html'
				});					
			};
	}]);
	
	app.controller('ShowHelpCtrl', ['$scope', '$route', '$translate', '$location', '$modalInstance',
		function($scope, $route, $translate, $location, $modalInstance) {
			$scope.closeModal = function() {
				$modalInstance.close();
			};
	}]);	

	app.controller('MainCtrl', ['$scope', '$route', '$translate', '$location', '$window', '$modal', 'Question', 'Country', 'District', 'Candidate', 'Party',
		function($scope, $route, $translate, $location, $window, $modal, Question, Country, District, Candidate, Party) {
			var lang = $location.search().lang;
			if (lang) {
				$translate.use(lang);
				$location.search('lang', null);
			}

			$scope.FilterForm = {};
			
			$scope.viewOptions = [
				{key: 1, value: 'Generic.Field.Candidates'},
				{key: 2, value: 'Generic.Field.Parties'}
			];
			
			$scope.viewBy = $scope.viewOptions[0];
			
			var partiesById = {};
			var countriesById = {};
			var districtsById = {};			
			
			$scope.filterDistricts = function() {
				District.query({CountryId: $scope.FilterForm.CountryId}, function(response) {
					$scope.FilterForm.DistrictId = null;
					$scope.FilterForm.districts = response;					
					
					angular.forEach(response, function(district, key) {
						var id = district.id;
						var name = district.name;
						districtsById[id] = name;
					});
					
					$scope.loadPartiesAndCandidates();					
				});							
			};
			
			$scope.loadPartiesAndCandidates = function() {
				var urlParams = {};
			
				angular.forEach($scope.questions, function(question, key) {
					urlParams[question.id] = question.point;
				});
				
				urlParams.CountryId = $scope.FilterForm.CountryId;
				urlParams.DistrictId = $scope.FilterForm.DistrictId;
				urlParams.PartyId = $scope.FilterForm.PartyId;

				Party.query(urlParams, function(response) {															
					angular.forEach(response, function(party, key) {
						party.country = countriesById[party.countryId];
						var id = party.id;
						var name = party.name;
						partiesById[id] = name;
					});												
					
					$scope.parties = response;
					
					Candidate.query(urlParams, function(response) {							
						angular.forEach(response, function(candidate, key) {
							candidate.party = partiesById[candidate.partyId];
							candidate.country = countriesById[candidate.countryId];
							candidate.district = districtsById[candidate.districtId];
						});
						$scope.candidates = response;
						
					});				
				});													
			};			
		
			Country.query({}, function(response) {
				$scope.FilterForm.countries = response;

				angular.forEach(response, function(country, key) {
					var id = country.id;
					var name = country.name;
					countriesById[id] = name;
				});															
			});		
			Question.query({}, function(response) {
				angular.forEach(response, function(question, key) {
					question.point = 50;
				});		
				$scope.questions = response;
				
				$scope.loadPartiesAndCandidates();
			});
			
			$scope.showCandidateInfo = function(id) {
				Candidate.get({id: id}, function(response) {
					response.party = partiesById[response.partyId];
					response.country = countriesById[response.countryId];
					response.district = districtsById[response.districtId];
				
					$scope.candidate = response;
				});			
			};
			
			$scope.showHelp = function() {
				var modalInstance = $modal.open({
					controller: 'ShowHelpCtrl',
					templateUrl:'views/showhelp/view.html'
				});			
			};
			
			$scope.changeLanguage = function() {		
				var modalInstance = $modal.open({
					controller: 'LanguageSelectionCtrl',
					templateUrl:'views/languageselection/view.html'
				});
			};
			
			$scope.changeLanguage();			
	}]);
}());