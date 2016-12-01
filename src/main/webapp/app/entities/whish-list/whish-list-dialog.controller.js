(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .controller('WhishListDialogController', WhishListDialogController);

    WhishListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WhishList', 'User'];

    function WhishListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WhishList, User) {
        var vm = this;

        vm.whishList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.whishList.id !== null) {
                WhishList.update(vm.whishList, onSaveSuccess, onSaveError);
            } else {
                WhishList.save(vm.whishList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hipsterstoreApp:whishListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
