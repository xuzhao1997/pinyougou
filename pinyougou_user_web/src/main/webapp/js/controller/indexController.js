app.controller("indexController",function ($scope,$controller,loginService) {

    //继承
    $controller("baseController",{$scope:$scope});

    //获取登录人用户名
    $scope.getLoginName=function () {
        loginService.getLoginName().success(function (response) {
            $scope.loginName=response.loginName;
        })
    }
})