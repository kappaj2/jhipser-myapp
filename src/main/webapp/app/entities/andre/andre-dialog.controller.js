(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('AndreDialogController', AndreDialogController);

    AndreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Andre'];

    function AndreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Andre) {
        var vm = this;

        vm.andre = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.andre.id !== null) {
                Andre.update(vm.andre, onSaveSuccess, onSaveError);
            } else {
                Andre.save(vm.andre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hipsterstoreApp:andreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
