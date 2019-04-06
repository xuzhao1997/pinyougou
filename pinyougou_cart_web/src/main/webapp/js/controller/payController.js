 //控制层 
app.controller('payController' ,function($scope,$controller   ,$location,payService){
	
	$controller('baseController',{$scope:$scope});//继承

    //生成支付二维码
	$scope.createNative=function () {
		payService.createNative().success(function (response) {
			//接收响应结果,将二维码生成
			$scope.out_trade_no=response.out_trade_no;//支付订单号
			$scope.total_fee=(response.total_fee/100).toFixed(2);//支付金额
            //生成二维码
            var qr = window.qr = new QRious({
                element: document.getElementById('qrious'),
                size: 300,
                value: response.code_url,
                level:'H'
            });
            //二维码生成后,就开始一直查询支付状态
            $scope.queryPayStatus();
        })
    }

    //查询支付状态
    $scope.queryPayStatus=function () {
        payService.queryPayStatus($scope.out_trade_no).success(function (response) {
            if(response.success){
                //支付成功
                location.href="paysuccess.html#?money="+$scope.total_fee;
            }else {
                //支付超时
                if(response.message=="timeout"){
                    $scope.createNative();
                }
                //支付失败
                location.href="payfail.html";
            }
        })
    }

    //获取支付金额
    $scope.getMoney=function () {
        $scope.money = $location.search()["money"];
    }

});