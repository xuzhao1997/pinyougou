//定义服务层
app.service("specificationService",function ($http) {
    //查询
    this.findAll=function () {
        return $http.get('../specification/findAll.do');
    }
    //分页查询
    this.findPage=function (pageNum,pageSize) {
        return $http.get("../specification/findPage.do?pageNum="+pageNum+"&pageSize="+pageSize);
    }
    //条件分页查询
    this.search=function (pageNum,pageSize,searchEntity) {
        return $http.post("../specification/search.do?pageNum="+pageNum+"&pageSize="+pageSize,searchEntity);
    }
    //根据id回显数据
    this.findOne=function (id) {
        return $http.get("../specification/findOne.do?id="+id);
    }
    //新增
    this.add=function (entity) {
        return $http.post("../specification/add.do",entity);
    }
    //修改
    this.update=function (entity) {
        return $http.post("../specification/update.do",entity);
    }
    //批量删除
    this.dele=function (ids) {
        return $http.get("../specification/delete.do?ids="+ids);
    }
    //查询模板关联的规格列表
    this.selectSpecOptions=function () {
        return $http.get("../specification/selectSpecOptions.do");
    }

})