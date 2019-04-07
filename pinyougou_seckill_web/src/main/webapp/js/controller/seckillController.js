 //控制层 
app.controller('seckillController' ,function($scope,$controller   ,$location,seckillService){
	
	$controller('baseController',{$scope:$scope});//继承

    //查询秒杀商品列表
    $scope.selectSeckillGoodsList=function () {
        seckillService.selectSeckillGoodsList().success(function (response) {
            $scope.seckillGoodsList=response;
        })
    }
    //基于秒杀商品id查询秒杀商品详情
    $scope.findOne=function () {
        //获取秒杀商品首页传递到秒杀详情页
        $scope.seckillGoodsId = $location.search()["seckillGoodsId"];
        seckillService.findOne($scope.seckillGoodsId).success(function (response) {
            $scope.seckillGoods=response;
        })
    }

});