 //控制层 
app.controller('payController' ,function($scope,$controller   ,payService){
	
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
        })
    }

});