 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,goodsService,itemCatService,typeTemplateService,uploadService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}
		);
	}
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			//绑定编辑器中输入的商品介绍的hml片段
			$scope.entity.goodsDesc.introduction = editor.html();
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	/*$scope.reloadList();//重新加载*/
					$scope.entity={};//临时清空添加页面
					//清空文本编辑器的内容
					editor.html("");
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//查询一级分类列表数据
	$scope.selectItemCatList1=function () {
		itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCatList1=response;
        })
    }

    //基于一级分类的改变,联动查询二级分类列表数据
	$scope.$watch("entity.goods.category1Id",function (newValue,oldValue) {
		itemCatService.findByParentId(newValue).success(function (response) {
			$scope.itemCatList2=response;
        })
	});
	//基于二级分类的改变,联动查询三级分类列表数据
    $scope.$watch("entity.goods.category2Id",function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCatList3=response;
        })
    });

    //基于三级分类的改变,查询模板数据
    $scope.$watch("entity.goods.category3Id",function (newValue,oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            //展示品牌列表
            $scope.entity.goods.typeTemplateId=response.typeId;
        })
    });

    //模板数据查询关联品牌
    $scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            //展示品牌列表
			$scope.brandList=JSON.parse(response.brandIds);
			//展示扩展属性
			$scope.entity.goodsDesc.customAttributeItems=JSON.parse(response.customAttributeItems);
        })
    });



    //初始化图片对象
	$scope.imageEntity={};

	//文件上传
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(function (response) {
			if(response.success){
				//上传成功,图片回显
				$scope.imageEntity.url=response.message;
			}else{
				alert(response.message);
			}
        })
    }
    //初始化entity对象
    $scope.entity={goods:{},goodsDesc:{itemImages:[]},itemList:[]};
	//完成图片保存功能
	$scope.saveImage=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);
    }
	//删除图片
    $scope.deleImages=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

});	
