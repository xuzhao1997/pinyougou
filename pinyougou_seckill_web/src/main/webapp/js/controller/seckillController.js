 //控制层 
app.controller('seckillController' ,function($scope,$controller   ,$location,$interval,seckillService){
	
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
            var endTime = new Date($scope.seckillGoods.endTime).getTime();
            var nowTime = new Date().getTime();
            $scope.secondes =Math.floor( (endTime-nowTime)/1000 );

            var time =$interval(function () {
                if($scope.secondes>0){
                    //时间递减
                    $scope.secondes--;
                    //时间格式化
                    $scope.timeString=$scope.convertTimeString($scope.secondes);
                }else{
                    //结束时间递减
                    $interval.cancel(time);
                }
            },1000);
        })
    }

    $scope.convertTimeString=function (allseconds) {
        //计算天数
        var days = Math.floor(allseconds/(60*60*24));

        //小时
        var hours =Math.floor( (allseconds-(days*60*60*24))/(60*60) );

        //分钟
        var minutes = Math.floor( (allseconds-(days*60*60*24)-(hours*60*60))/60 );

        //秒
        var seconds = allseconds-(days*60*60*24)-(hours*60*60)-(minutes*60);

        //拼接时间
        var timString="";
        if(days>0){
            timString=days+"天:";
        }

        if(hours<10){
            hours="0"+hours;
        }
        if(minutes<10){
            minutes="0"+minutes;
        }
        if(seconds<10){
            seconds="0"+seconds;
        }
        return timString+=hours+":"+minutes+":"+seconds;
    }

});