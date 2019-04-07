 //控制层 
app.controller('seckillController' ,function($scope,$controller   ,$location,seckillService){
	
	$controller('baseController',{$scope:$scope});//继承

    //查询秒杀商品列表
    $scope.selectSeckillGoodsList=function () {
        seckillService.selectSeckillGoodsList().success(function (response) {
            $scope.seckillGoodsList=response;
        })
    }

});