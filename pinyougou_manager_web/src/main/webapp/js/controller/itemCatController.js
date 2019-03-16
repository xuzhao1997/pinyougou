 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			//指定新增分类的父id
			$scope.entity.parentId=$scope.parentId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	/*$scope.reloadList();//重新加载*/
					$scope.findByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
                    $scope.findByParentId($scope.parentId);
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//定义当前级别的父id
	$scope.parentId=0;

	//查询下级分类
	$scope.findByParentId=function (parentId) {
        $scope.parentId=parentId;
        itemCatService.findByParentId(parentId).success(
        	function (response) {
				$scope.list = response;
        })
    }

    //设置页面展示的分类级别
	$scope.grade=1;//初始化查询一级分类的列表数据
	$scope.setGrade=function (grade) {
		$scope.grade=grade;
    }

    //面包屑导航栏效果实现
	$scope.selectItemCatList=function (entity_p) {
		//一级分类
		if($scope.grade==1){
			$scope.entity_1=null;
            $scope.entity_2=null;
		}
		//二级分类
        if($scope.grade==2){
            $scope.entity_1=entity_p;
            $scope.entity_2=null;
        }
        //三级分类
        if($scope.grade==3){
            $scope.entity_2=entity_p;
        }
        //查询当前分类的子分类
		$scope.findByParentId(entity_p.id);

    }

    //查询分类关联的模板
	$scope.selectTypeTemplateList=function () {
		typeTemplateService.findAll().success(function (response) {
			$scope.typeTemplateList=response;
        })
    }

});	
