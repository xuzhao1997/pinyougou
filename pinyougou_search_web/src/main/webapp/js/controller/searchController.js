app.controller("searchController",function ($scope,$controller,searchService) {

    //继承分页
    $controller("baseController",{$scope:$scope});

    //初始化搜索条件对象
    $scope.searchMap={
        keywords:"",
        category:"",//分类条件
        brand:"",
        spec:{},//规格条件
        price:"",
        sortField:"",//排序字段
        sort:"ASC",//排序方式
        pageNum:1,
        pageSize:60
    };
    //商品搜索功能
    $scope.search=function () {
        searchService.searchItem($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
        })
    }

    //条件过滤查询
    $scope.addFilterCondition=function (key,value) {
        //判断条件如果是品牌,分类,价格区间
        if(key=="brand" || key=="category" || key=="price"){
            $scope.searchMap[key]=value;
        }else{
            //规格条件,组装$scope.searchMap规格名称和对应的选项值
            $scope.searchMap.spec[key]=value;
        }
        //从新查询商品数据
        $scope.search($scope.searchMap);
    }

    //删除条件过滤查询
    $scope.removeSearchItem=function (key) {
        //判断条件如果是品牌,分类,价格区间
        if(key=="brand" || key=="category" || key=="price"){
            $scope.searchMap[key]="";
        }else{
            //规格条件,组装$scope.searchMap规格名称和对应的选项值
           delete $scope.searchMap.spec[key]
        }
        //从新查询商品数据
        $scope.search($scope.searchMap);
    }
    
    //排序查询
    $scope.sortSearch=function (sortField,sort) {
        $scope.searchMap.sort = sort;//组装排序方式
        $scope.searchMap.sortField = sortField;//组装排序字段
        //从新查询商品数据
        $scope.search($scope.searchMap);
    }
    
})