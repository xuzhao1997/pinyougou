//定义控制器，为模型数据name初始化赋值
//参数一：控制器名称 参数二：控制器要做的事情
//$scope 可以理解为全局的作用域对象  作用：相当于html代码与js代码交换的桥梁
//$http内置服务，作用：发起http请求，与java程序进行数据交互。注意：发起的请求，全部是异步请求（ajax)
app.controller("indexController",function ($scope,$controller,contentService) {

    //编写继承代码
    //参数一：继承的父控制器名称 参数二：固定写法，共享$scope作用域对象
    $controller("baseController",{$scope:$scope});

    //根据广告分类查询相关广告列表数据
    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList=response;
        })
    }

    //门户网站对接搜索模块,搜索功能
    $scope.search=function () {
        location.href="http://search.pinyougou.com/search.html#?keywords="+$scope.keywords;
    }

});