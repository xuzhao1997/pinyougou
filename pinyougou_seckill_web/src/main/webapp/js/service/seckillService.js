//服务层
app.service('seckillService',function($http){
	    	
	//查询秒杀商品列表
	this.selectSeckillGoodsList=function(){
		return $http.get('seckill/selectSeckillGoodsList.do');
	}
	//基于秒杀商品id查询秒杀商品详情
    this.findOne=function (seckillGoodsId) {
        return $http.get("seckill/findOne.do?seckillGoodsId="+seckillGoodsId);
    }

    //生成秒杀订单
    this.submitSeckillOrder=function (seckillGoodsId) {
        return $http.get("seckill/submitSeckillOrder.do?seckillGoodsId="+seckillGoodsId);
    }
});
