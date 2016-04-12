'use strict';

angular.module('dataintegration')
    .controller('SearchesCtrl', function ($scope, Search, $state, $stateParams) {
        if(typeof $stateParams.searchId !== "undefined") {
            $scope.search = Search.get({id: $stateParams.searchId})
        }
        $scope.list = listSearches();

        function listSearches() {
            return Search.query();
        }

        $scope.save = function(searchData) {
            Search.save(searchData);
            $state.transitionTo("searches");
        };

        $scope.update = function(searchData) {
            Search.update(searchData);
            $state.transitionTo("searches");
        };

        $scope.delete = function(searchData) {
            Search.delete({id: searchData.id});
            $state.reload();
        };

        $scope.fetchNow = function(searchData) {
            Search.fetchNow({id: searchData.id});
            $state.reload();
        };
    });
