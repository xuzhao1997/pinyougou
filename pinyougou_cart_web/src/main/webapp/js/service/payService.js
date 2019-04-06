//服务层
app.service('payService',function($http){
	    	
	//生成支付二维码
	this.createNative=function(){
		return $http.get('pay/createNative.do');
	}

});
