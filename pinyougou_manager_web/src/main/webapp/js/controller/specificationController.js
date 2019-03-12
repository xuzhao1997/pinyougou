//定义控制器，为模型数据name初始化赋值
//参数一：控制器名称 参数二：控制器要做的事情
//$scope 可以理解为全局的作用域对象  作用：相当于html代码与js代码交换的桥梁
//$http内置服务，作用：发起http请求，与java程序进行数据交互。注意：发起的请求，全部是异步请求（ajax)
app.controller("specificationController",function ($scope,$controller,specificationService) {

    //编写继承代码
    //参数一：继承的父控制器名称 参数二：固定写法，共享$scope作用域对象
    $controller("baseController",{$scope:$scope});

    //json格式是的数据，[]代表数组 ,{}代表对象
    //$scope.list=[100,200,300];
    //查询所有品牌列表
    $scope.findAll=function () {
        //参数一：请求地址
        //success响应成功后的回调函数，response介绍响应数据
        specificationService.findAll().success(function (response) {
            $scope.list=response;
        });
    }

    //发起分页查询请求
    $scope.findPage=function (pageNum,pageSize) {
        specificationService.findPage(pageNum,pageSize).success(function (response) {
            //response = {total:100,rows:[{},{}]}
            $scope.list=response.rows;//当前页结果集
            $scope.paginationConf.totalItems=response.total;//查询总记录数
        })
    }

    //初始化条件查询对象
    $scope.searchEntity={};

    //条件分页查询请求
    $scope.search=function (pageNum,pageSize) {
        specificationService.search(pageNum,pageSize,$scope.searchEntity).success(function (response) {
            //response = {total:100,rows:[{},{}]}
            $scope.list=response.rows;//当前页结果集
            $scope.paginationConf.totalItems=response.total;//查询总记录数
        })
    }

    //初始化查询条件实体对象
    $scope.searchEntity={};

    //条件分页查询请求
    $scope.findPage=function (pageNum,pageSize) {
        specificationService.findPage(pageNum,pageSize,$scope.searchEntity).success(function (response) {
            //response = {total:100,rows:[{},{}]}
            $scope.list=response.rows;//当前页结果集
            $scope.paginationConf.totalItems=response.total;//查询总记录数
        })
    }

    //根据id查询品牌数据
    $scope.findOne=function (id) {
        specificationService.findOne(id).success(function (response) {
            $scope.entity=response;
        });
    }

    //初始化组合实体类
    $scope.entity={specification:{},specificationOptions:[]};
    //保存规格
    $scope.save=function () {

        var method=null;
        //基于entity的id值判断是新增还是修改
        if($scope.entity.specification.id!=null){
            //修改操作
            method=specificationService.update($scope.entity);
        }else {
            //新增
            method=specificationService.add($scope.entity);
        }

        //如果提交的数据是对象格式，需要发起post请求
        //$scope.entity要提交的品牌对象
        method.success(function (response) {
            //判断品牌是否操作成功
            if(response.success){
                //保存品牌成功，重新加载品牌列表数据
                $scope.reloadList();
            }else {
                //保存失败，提示
                alert(response.message);
            }
        })
    }

    //批量删除数据
    $scope.dele=function () {
        if(confirm("您确定要删除吗？")){
            specificationService.dele($scope.selectIds).success(function (response) {
                //判断品牌是否操作成功
                if(response.success){
                    //删除品牌成功，重新加载品牌列表数据
                    $scope.reloadList();
                    $scope.selectIds=[];//清空勾选的id数组
                }else {
                    //保存失败，提示
                    alert(response.message);
                }
            })
        }
    }
    //新增规格选项
    $scope.addRow=function () {
        $scope.entity.specificationOptions.push({});
    }
    //删除规格选项
    $scope.deleRow=function (index) {
        $scope.entity.specificationOptions.splice(index,1);
    }

});