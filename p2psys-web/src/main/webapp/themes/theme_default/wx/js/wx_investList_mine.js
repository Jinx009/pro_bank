define(function(require,exports,module){
    require('jquery');
    wt();
    function wt(){
        var tpl = require('/themes/theme_default/wx/tpl/investList_mine.tpl');//载入tpl模板
        //初始化数据列表显示
        require.async('./showTable',function(showTable){
            showTable.ajaxUrl('/member/invest/mineList.html?time=-1&status=-1&randomTime='+(new Date()).getTime(),tpl);
        }); 
    }
    $("#zc").click(function(){
      $("#yx").removeClass("current");
      $("#wt").removeClass("current");
      $(this).addClass("current");
      $("#invest_list").html("暂无数据");
    })
    $("#yx").click(function(){
      $("#wt").removeClass("current");
      $("#zc").removeClass("current");
      $(this).addClass("current");
      $("#invest_list").html("暂无数据");
    })
    $("#wt").click(function(){
      wt();
      $("#zc").removeClass("current");
      $("#yx").removeClass("current");
      $(this).addClass("current");
    });
});
