app.service('uploadService',function ($http) {

   //文件上传
    this.uploadFile=function () {
        //天使结合html5表单对象实现文件上传
        var formData = new FormData();
        //获取页面选择的对象
        formData.append("file",file.files[0]);
        //发起请求,提交上传文件
        return $http({
            url:"../upload/uploadFile.do",
            method:"post",
            data:formData,
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity
        });
    }

})