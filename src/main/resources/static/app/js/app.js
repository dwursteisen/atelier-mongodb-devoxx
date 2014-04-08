'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives','ngResource', 'ngSanitize']).
    config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
        $routeProvider.
            when('/', {templateUrl: 'partials/index.html', controller: EntriesListCtrl }).
            when('/talks/:id', {templateUrl: 'partials/talk.html', controller: TalkCtrl }).
            when('/speakers/', {templateUrl: 'partials/speakers.html', controller: SpeakersListCtrl }).
            when('/speakers/:id', {templateUrl: 'partials/speaker.html', controller: SpeakerCtrl }).
            when('/maps/', {templateUrl: 'partials/maps.html', controller: MapsCtrl}).

            otherwise({redirectTo: '/'});

    }]);

