app.controller('brandController',function ($scope,brandService) {
    $scope.findAll=function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        )
    };
    //定义分页控件对象
    $scope.paginationConf = {
        currentPage:1,  				//当前页
        totalItems:10,					//总记录数
        itemsPerPage:10,				//每页记录数
        perPageOptions:[10,20,30,40,50], //分页选项，下拉选择一页多少条记录
        onChange:function(){			//页面变更后触发的方法
            $scope.reloadList();		//启动就会调用分页组件
        }
    };
    $scope.reloadList=function () {
        $scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }
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

    //定义一个批量删除的数组
    $scope.selectIds=[];
    //更新复选框选中状态的方法
    $scope.updateSelection=function ($event,id) {
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else{
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }
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