app.controller('brandController',function ($scope,brandService) {
    $scope.findAll=function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        )
    };

    //发起分页查询请求
    $scope.findPage=function (pageNum,pageSize) {
        brandService.findPage(pageNum,pageSize).success(function (response) {
            $scope.list=response.rows;//当前页结果集
            $scope.paginationConf.totalItems=response.total;//查询总记录数
        })
    }
    //根据id查询回显数据
    $scope.findOne=function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity=response;
        });
    }
    //品牌新增
    $scope.entity = {}
    $scope.save = function () {
        var method = null;
        //基于entity的id值判断是新增还是修改
        if($scope.entity.id!=null){
            //修改操作
            method=brandService.update($scope.entity);
        }else{
            //新增操作
            method=brandService.add($scope.entity);
        }
        method.success(function (response) {
                if(response.success){
                    $scope.reloadList();
                }else{
                    alert(response.message);
                }
            }
        )
    }

    //批量删除数据
    $scope.dele=function () {
        if(confirm('您确定要删除吗?')){
            brandService.dele($scope.selectIds).success(function (response) {
                if(response.success){
                    $scope.reloadList();
                    $scope.selectIds=[];//清空勾选的id数组
                }else{
                    alert(response.message);
                }
            })
        }
    }
    //是否选中为了翻页后回来还能勾选上
    $scope.isChecked = function(id){
        if($scope.selectIds.indexOf(id)!= -1){
            return true;
        }
        return false;
    }
})