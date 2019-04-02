 //控制层 
app.controller('cartController' ,function($scope,$controller   ,cartService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //查询购物车列表
	$scope.findCartList=function(){
        cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
				sum();
			}			
		);
	}    

	//添加商品到购物车
	$scope.addItemToCartList=function (itemId,num) {
		cartService.addItemToCartList(itemId,num).success(function (response) {
			if(response.success){
				//添加成功,重新查询购物车列表
				$scope.findCartList();
			}else{
				//添加购物车失败
				alert(response.message);
			}
        })
    }
    //统计商品数量和总金额
	sum=function () {
		//循环遍历购物车列表数据,获取每个商品数量和商品小计金额,然后做累加
		$scope.totalNum=0;
		$scope.totalMoney=0.00;
		for(var i=0;i<$scope.cartList.length;i++){
			var cart = $scope.cartList[i];
			//获取购物车商品列表
			var orderItemList = cart.orderItemList;
			for(var j = 0;j<orderItemList.length;j++){
                $scope.totalNum += orderItemList[j].num;
                $scope.totalMoney += orderItemList[j].totalFee;
			}
		}
    }
    
});	
