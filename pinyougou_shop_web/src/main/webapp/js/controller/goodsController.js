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
        });
        typeTemplateService.selectSpecList(newValue).success(function (response) {
			$scope.specList=response;
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
    $scope.entity={goods:{isEnableSpec:'1'},goodsDesc:{itemImages:[],specificationItems:[]},itemList:[]};
	//完成图片保存功能
	$scope.saveImage=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);
    }
	//删除图片
    $scope.deleImages=function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //规格选项勾选和取消勾选
	$scope.updateSpecAttribute=function ($event,specName,specOption) {
		//判断规格名称对应的规格对象是否存在勾选的规格列表中,如果存在,就返回该对象
        var specObject = $scope.getObjectByValue($scope.entity.goodsDesc.specificationItems,"attributeName",specName);
			if(specObject!=null){//如果存在
				if($event.target.checked){//勾选
					//在已有的规格对象中规格选项列表中,添加勾选的选项列表数据
                    specObject.attributeValue.push(specOption);
				}else{//取消勾选
					//在已有的规格对象中规格选项列表中,移除取消勾选的选项列表数据
                    specObject.attributeValue.splice(specObject.attributeValue.indexOf(specOption),1);
					//如果规格对象中规格选项列表中规格数据全部移除
					if(specObject.attributeValue.length == 0){
						//从勾选的规格列表中,移除该规格对象
						var index = $scope.entity.goodsDesc.specificationItems.indexOf(specObject);
                        $scope.entity.goodsDesc.specificationItems.splice(index,1);
					}
				}
			}else{//如果不存在,新增规格对象到勾选的规格列表中
				$scope.entity.goodsDesc.specificationItems.push({"attributeName":specName,"attributeValue":[specOption]});
			}
    }

    //构建组装item列表
	$scope.createItemList=function () {
		//初始化itemList列表
		$scope.entity.itemList=[{spec:{},price:0.00,num:999,status:"1",isDefault:"0"}];
		//勾选的规格结果集数组
		var checkSpecList = $scope.entity.goodsDesc.specificationItems;
		//全部取消勾选的规格结果集
		if(checkSpecList.length==0){
            $scope.entity.itemList=[];
		}
		//获取勾选的规格名称和规格选项列表
		for(var i = 0;i< checkSpecList.length;i++){
			//抽取方法,动态的组装itemList中对象的spec属性赋值过程
			$scope.entity.itemList = addColumn($scope.entity.itemList,checkSpecList[i].attributeName,checkSpecList[i].attributeValue);

		}
    }
	//动态的组装itemList中对象的spec属性赋值
    addColumn=function (list,specName,specOptions) {
		var newList=[];
		for(var i = 0;i<list.length;i++){
			var oldItem = list[i];
			//循环遍历规格选项列表,获取规格选项值
			for(var j = 0;j<specOptions.length;j++){
				//基于深克隆,构建新的item对象
				var newItem = JSON.parse(JSON.stringify(oldItem));
				newItem.spec[specName]=specOptions[j];
				newList.push(newItem);
			}
		}
		return newList;
    }

    //优化分类商品名称
	$scope.itemCatList=[];
	//加载商品分类列表
	$scope.findItemCatList=function(){
		itemCatService.findAll().success(function (response) {
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}
        })
	}


		//商品审核状态数组
	$scope.status=['未审核','已审核','审核未通过','关闭'];
	//商品上下架状态数组
    $scope.isMarketable=['下架','上架'];

    //商品上下架
    $scope.updateIsMarketable=function (isMarketable) {
        //获取选中的复选框
        goodsService.updateIsMarketable($scope.selectIds,isMarketable).success(function (response) {
            if(response.success){
                $scope.reloadList();//刷新列表
                $scope.selectIds=[];//清空记录审核状态id的数组内容
            }else{
                alert(response.message);
            }
        })
    }

});	
