app.service("loginService",function ($http) {
    //获取登录人用户名
    this.getLoginName=function () {
        return $http.get("login/getLoginName.do");
    }
})