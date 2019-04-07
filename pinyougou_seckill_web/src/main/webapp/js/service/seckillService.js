//服务层
app.service('seckillService',function($http){
	    	
	//查询秒杀商品列表
	this.selectSeckillGoodsList=function(){
		return $http.get('seckill/selectSeckillGoodsList.do');
	}



});
