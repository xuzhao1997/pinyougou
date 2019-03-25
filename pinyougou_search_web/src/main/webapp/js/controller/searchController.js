app.controller("searchController",function ($scope,$controller,searchService) {

    //继承分页
    $controller("baseController",{$scope:$scope});

    //初始化搜索条件对象
    $scope.searchMap={
        keywords:""
    };
    //商品搜索功能
    $scope.search=function () {
        searchService.searchItem($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
        })
    }

})