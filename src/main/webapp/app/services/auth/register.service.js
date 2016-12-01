(function () {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
