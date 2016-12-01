(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('andre', {
            parent: 'entity',
            url: '/andre',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hipsterstoreApp.andre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/andre/andres.html',
                    controller: 'AndreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('andre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('andre-detail', {
            parent: 'entity',
            url: '/andre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hipsterstoreApp.andre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/andre/andre-detail.html',
                    controller: 'AndreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('andre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Andre', function($stateParams, Andre) {
                    return Andre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'andre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('andre-detail.edit', {
            parent: 'andre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/andre/andre-dialog.html',
                    controller: 'AndreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Andre', function(Andre) {
                            return Andre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('andre.new', {
            parent: 'andre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/andre/andre-dialog.html',
                    controller: 'AndreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                user_name: null,
                                passwd: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('andre', null, { reload: 'andre' });
                }, function() {
                    $state.go('andre');
                });
            }]
        })
        .state('andre.edit', {
            parent: 'andre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/andre/andre-dialog.html',
                    controller: 'AndreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Andre', function(Andre) {
                            return Andre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('andre', null, { reload: 'andre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('andre.delete', {
            parent: 'andre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/andre/andre-delete-dialog.html',
                    controller: 'AndreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Andre', function(Andre) {
                            return Andre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('andre', null, { reload: 'andre' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
