(function() {
    'use strict';

    angular
        .module('hipsterstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('whish-list', {
            parent: 'entity',
            url: '/whish-list?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hipsterstoreApp.whishList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/whish-list/whish-lists.html',
                    controller: 'WhishListController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whishList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('whish-list-detail', {
            parent: 'entity',
            url: '/whish-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hipsterstoreApp.whishList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/whish-list/whish-list-detail.html',
                    controller: 'WhishListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whishList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WhishList', function($stateParams, WhishList) {
                    return WhishList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'whish-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('whish-list-detail.edit', {
            parent: 'whish-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/whish-list/whish-list-dialog.html',
                    controller: 'WhishListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhishList', function(WhishList) {
                            return WhishList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('whish-list.new', {
            parent: 'whish-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/whish-list/whish-list-dialog.html',
                    controller: 'WhishListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                creationDate: null,
                                hidden: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('whish-list', null, { reload: 'whish-list' });
                }, function() {
                    $state.go('whish-list');
                });
            }]
        })
        .state('whish-list.edit', {
            parent: 'whish-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/whish-list/whish-list-dialog.html',
                    controller: 'WhishListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhishList', function(WhishList) {
                            return WhishList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('whish-list', null, { reload: 'whish-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('whish-list.delete', {
            parent: 'whish-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/whish-list/whish-list-delete-dialog.html',
                    controller: 'WhishListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WhishList', function(WhishList) {
                            return WhishList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('whish-list', null, { reload: 'whish-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
