(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('WhishListDetailController', WhishListDetailController);

    WhishListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WhishList', 'User'];

    function WhishListDetailController($scope, $rootScope, $stateParams, previousState, entity, WhishList, User) {
        var vm = this;

        vm.whishList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hipsterstoreApp:whishListUpdate', function(event, result) {
            vm.whishList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
