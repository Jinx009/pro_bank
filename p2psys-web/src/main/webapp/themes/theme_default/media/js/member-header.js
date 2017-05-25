 define(function(require,exports,module){
    require('jquery');
    $(".member-right").mouseover(function(){
        $(".member-right ul").removeClass("hide")
    })
     $(".member-right").mouseout(function(){
        $(".member-right ul").addClass("hide")
    })
})