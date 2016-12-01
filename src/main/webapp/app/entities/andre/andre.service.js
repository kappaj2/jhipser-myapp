(function() {
    'use strict';
    angular
        .module('hipsterstoreApp')
        .factory('Andre', Andre);

    Andre.$inject = ['$resource'];

    function Andre ($resource) {
        var resourceUrl =  'api/andres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
