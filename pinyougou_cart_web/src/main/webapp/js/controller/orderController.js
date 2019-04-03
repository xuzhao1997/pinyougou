 //控制层 
app.controller('orderController' ,function($scope,$controller   ,addressService,cartService,orderService){
	
	$controller('baseController',{$scope:$scope});//继承

    //寄送至的收件人地址变量
    $scope.address=null;

	//查询登录人关联的收件人地址列表
	$scope.findByUserId=function(){
		addressService.findByUserId().success(function (response) {
			$scope.addressList=response;
			//判断是否是默认列表
			for(var i=0;i<$scope.addressList.length;i++){
				if($scope.addressList[i].isDefault=='1'){
                    $scope.address=$scope.addressList[i];
                    break;
				}
			}
			//如果没有默认地址,以地址列表第一个对象为默认列表
			if($scope.address==null){
				$scope.address=$scope.addressList[0];
			}
        })
	}

	//判断是否是默认收件人地址
	$scope.isSelect=function(addr){
		if($scope.address==addr){
			return true;
		}else{
			return false;
		}
	}

	//切换收件人地址
	$scope.updateAddress=function(addr){
		$scope.address=addr;
	}

	//订单模型数据初始化,默认的是微信支付
	$scope.entity={paymentType:'1'};

	//切换支付方式
	$scope.updatePaymentType=function(type){
		$scope.entity.paymentType=type;
	}

    //查询购物车列表
	$scope.findCartList=function(){
        cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
				sum();
			}			
		);
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

    //提交订单
    $scope.submitOrder=function () {
        $scope.entity.receiverAreaName=$scope.address.address;
        $scope.entity.receiverMobile=$scope.address.mobile;
        $scope.entity.receiver=$scope.address.contact;

        orderService.add($scope.entity).success(function (response) {
            if(response.success){
                //提交订单成功，跳转支付页面
                location.href="pay.html";

            }else {
                alert(response.message);
            }
        })
    }

});