//定义服务层
app.service("brandService",function ($http) {
    //查询所有品牌
    this.findAll=function () {
        return $http.get('../brand/findAll.do');
    }
    //分页查询
    this.findPage=function (pageNum,pageSize) {
        return $http.get("../brand/findPage.do?pageNum="+pageNum+"&pageSize="+pageSize);
    }
    //条件分页查询
    this.search=function (pageNum,pageSize,searchEntity) {
        return $http.post("../brand/search.do?pageNum="+pageNum+"&pageSize="+pageSize,searchEntity);
    }

    //根据id回显数据
    this.findOne=function (id) {
        return $http.get("../brand/findOne.do?id="+id);
    }
    //新增品牌
    this.add=function (entity) {
        return $http.post("../brand/add.do",entity);
    }
    //修改品牌
    this.update=function (entity) {
        return $http.post("../brand/update.do",entity);
    }
    //批量删除
    this.dele=function (ids) {
        return $http.get("../brand/delete.do?ids="+ids);
    }
    //查询模板关联的品牌列表
    this.selectBrandOptions=function () {
        return $http.get("../brand/selectBrandOptions.do");
    }

})