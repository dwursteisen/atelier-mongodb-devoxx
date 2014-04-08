'use strict';

/* Controllers */


function AppCtrl($scope, $http) {
    $scope.user = null;
}
AppCtrl.$inject = ['$scope', '$http'];


function NavBarController($scope) {
}
NavBarController.$inject = ['$scope'];


// List of talks
function EntriesListCtrl($scope, $http) {
    $scope.entries = null;
    $scope.lastQuery = "";

    $http({method: 'GET', url: '/api/talks/' })
        .success(function(data, status, headers, config) {
            lastQuery($http, $scope);
            $scope.entries = new Array();
            $scope.entries = data;
        })
        .error(function(data, status, headers, config) {
            $scope.name = 'Error!'
        });

}
EntriesListCtrl.$inject = ['$scope', '$http'];


function TalkCtrl($rootScope, $scope, $routeParams, $http, $location) {
    $scope.talk = null;
    $scope.lastQuery = "";

    $http({method: 'GET', url: '/api/talks/'+ $routeParams.id })
        .success(function(data, status, headers, config) {
            lastQuery($http, $scope);
            $scope.talk = new Array();
            $scope.talk = data;
        })
        .error(function(data, status, headers, config) {
            $scope.name = 'Error!'
        });
}
TalkCtrl.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$location'];


// List of talks
function SpeakersListCtrl($scope, $http) {
    $scope.speakers = null;
    $scope.query = null;
    $scope.lastQuery = "";

    $scope.search = function() {
        var query;
        var uri = '/api/speakers/';
        if ($scope.query) {
            uri = '/api/speakers/byname/'+ $scope.query;
        }
        $http({method: 'GET', url: uri})
            .success(function(data, status, headers, config) {
                lastQuery($http, $scope);
                $scope.speakers = new Array();
                $scope.speakers = data;
            })
            .error(function(data, status, headers, config) {
                $scope.name = 'Error!'
            });
    }

    $scope.search();
}
SpeakersListCtrl.$inject = ['$scope', '$http'];


function SpeakerCtrl($rootScope, $scope, $routeParams, $http, $location) {
    $scope.speaker = null;
    $scope.lastQuery = "";

    $http({method: 'GET', url: '/api/speakers/'+ $routeParams.id })
        .success(function(data, status, headers, config) {
            lastQuery($http, $scope);
            $scope.speaker = new Array();
            $scope.speaker = data;
        })
        .error(function(data, status, headers, config) {
            $scope.name = 'Error!'
        });
}
SpeakerCtrl.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$location'];


function lastQuery($http, $scope) {
    $http({method: 'GET', url: '/api/query/lastQuery'})
        .success(function(query) { $scope.lastQuery = query; });
}
function MapsCtrl($rootScope, $scope, $routeParams, $http, $location) {
    $scope.speakers = [];
    $scope.error = null;
    $scope.lastQuery = "";

    var markers = [];

    $http({method:'GET', url: '/api/geo/'})
        .success(function(data) {

            lastQuery($http, $scope);

            $scope.speakers = data;

            // create a map in the "map" div, set the view to a given place and zoom
            var map = L.map('map').setView([48.8670, 2.3521], 12);


            L.tileLayer('http://localhost:8080/tiles/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(map);

            data.forEach(function (speaker) {
                // add a marker in the given location, attach some popup content to it and open the popup
                markers.push(L.marker([speaker.geo.longitude, speaker.geo.latitude])
                 .addTo(map)
                 .bindPopup(speaker.name.firstName + " "+speaker.name.lastName));

            })

            map.on('click', function(event) {
                $http({method: 'GET', url: '/api/geo/'+event.latlng.lng + "/" +event.latlng.lat})
                    .success(function (data) {

                        lastQuery($http, $scope);

                        markers.forEach(function (m) { map.removeLayer(m) });
                        markers = [];

                        // Je pense qu'il doit y avoir moyen de rationnaliser ce bout de javascript...
                        data.forEach(function (speaker) {

                            markers.push(L.marker([speaker.geo.longitude, speaker.geo.latitude])
                                .addTo(map)
                                .bindPopup(speaker.name.firstName + " "+speaker.name.lastName));
                        });
                    })
                    .error(function(data) {
                        $scope.error = 'Oups ! Probleme lors de la recuperation des donnees :S.' +
                            'Le geo index est il bien present ? ( db.speakers.ensureIndex({geo: \"2dsphere\"}) )';
                    });
            });

        })
        .error(function(data) {
            $scope.error = 'Oups ! Problème lors de la récupération des données :S';
        });
}
MapsCtrl.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$location'];
