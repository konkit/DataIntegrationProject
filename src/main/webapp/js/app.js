'use strict';

angular
	.module('dataintegration', [
		'ngResource',
		'ui.router',
		'ui.bootstrap'
	])
	.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/");

		$stateProvider
			.state('main', {
                url: '/',
				templateUrl: 'templates/main.html',
				controller: 'MainCtrl'
			})
			.state('searches', {
                url: '/searches',
				templateUrl: 'templates/searches.html',
				controller: 'SearchesCtrl'
			})
            .state('searches_new', {
                url: '/searches/new',
                templateUrl: 'templates/searches_new.html',
                controller: 'SearchesCtrl'
            })
            .state('searches_show', {
                url: '/searches/show/:searchId',
                templateUrl: 'templates/searches_show.html',
                controller: 'SearchesCtrl'
            })
            .state('searches_edit', {
                url: '/searches/edit/:searchId',
                templateUrl: 'templates/searches_edit.html',
                controller: 'SearchesCtrl'
            });
	});
