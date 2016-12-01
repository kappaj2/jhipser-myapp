(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('AndreController', AndreController);

    AndreController.$inject = ['$scope', '$state', 'Andre'];

    function AndreController ($scope, $state, Andre) {
        var vm = this;
        
        vm.andres = [];

        loadAll();

        function loadAll() {
            Andre.query(function(result) {
                vm.andres = result;
            });
        }
    }
})();
