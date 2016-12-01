(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('AndreDeleteController',AndreDeleteController);

    AndreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Andre'];

    function AndreDeleteController($uibModalInstance, entity, Andre) {
        var vm = this;

        vm.andre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Andre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
