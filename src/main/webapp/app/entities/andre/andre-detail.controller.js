(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('AndreDetailController', AndreDetailController);

    AndreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Andre'];

    function AndreDetailController($scope, $rootScope, $stateParams, previousState, entity, Andre) {
        var vm = this;

        vm.andre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterstoreApp:andreUpdate', function(event, result) {
            vm.andre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
