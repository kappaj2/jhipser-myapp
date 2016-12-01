(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('WhishListDeleteController',WhishListDeleteController);

    WhishListDeleteController.$inject = ['$uibModalInstance', 'entity', 'WhishList'];

    function WhishListDeleteController($uibModalInstance, entity, WhishList) {
        var vm = this;

        vm.whishList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WhishList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
