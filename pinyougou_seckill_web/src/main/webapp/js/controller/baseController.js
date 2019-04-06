app.controller("baseController",function ($scope) {

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
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }

    //定义批量删除的数组
    $scope.selectIds=[];

    //更新复选框选中状态的方法
    $scope.updateSelection=function ($event,id) {
        //获取复选框勾选状态
        //$event.target获取事件源对象 就是复选框对象
        if($event.target.checked){
            //勾选操作
            //js 往数组添加值 push()
            $scope.selectIds.push(id);
        }else {
            //取消勾选
            //获取移除元素的索引值
            var index=$scope.selectIds.indexOf(id);
            //参数一：移除位置的元素的索引值  参数二：从该位置移除几个元素
            $scope.selectIds.splice(index,1);
        }
    }

    //从json格式数组字符串中获取数组中每个对象的value值，做字符串拼接
    $scope.getStringByValue=function (jsonString,key) {
        //1 解析json字符串  [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        var jsonArr = JSON.parse(jsonString);
        var value="";
        for(var i=0;i<jsonArr.length;i++){
            //注意：json格式对象，基于属性名获取属性值有两种方式
            //1 如果属性名是确定值，  获取属性值方式 对象.属性名  对象[属性名]
            //2 如果属性名是变量，获取属性值方式 ：对象[属性名]

            if(i>0){
                value+=","+jsonArr[i][key];
            }else {
                value+=jsonArr[i][key];
            }

        }

        return value;
    }


});