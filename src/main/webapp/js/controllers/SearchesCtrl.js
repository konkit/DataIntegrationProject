'use strict';

angular.module('dataintegration')
    .controller('SearchesCtrl', function ($scope, Search, $state, $stateParams) {
        function reload() {
            if(typeof $stateParams.searchId !== "undefined") {
                $scope.search = Search.get({id: $stateParams.searchId})
            } else {
                $scope.list = Search.query();
            }
        }
        reload();

        $scope.save = function(searchData) {
            Search.save(searchData);
            $state.transitionTo("searches");
            reload();
        };

        $scope.update = function(searchData) {
            Search.update(searchData);
            $state.transitionTo("searches");
            reload();
        };

        $scope.delete = function(searchData) {
            Search.delete({id: searchData.id});
            $state.reload();
            reload();
        };

        $scope.fetchNow = function(searchData) {
            Search.fetchNow({id: searchData.id});
            $state.reload();
            reload();
        };
    });
