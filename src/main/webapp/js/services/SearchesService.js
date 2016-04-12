'use strict';

angular.module('dataintegration')
    .factory('Search', function($resource) {
        return $resource("/searches/:id", {id: "@id"}, {
            update: {
                method: "PUT"
            },
            fetchNow: {
                url: '/searches/:id/fetchNow',
                method: "GET"
            }
        });
    });
