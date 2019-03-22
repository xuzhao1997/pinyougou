app.constructor("indexController",function ($scope,$controller, contentService) {

    //继承
    $controller("baseController",{$scope:$scope});

})