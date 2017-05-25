define(function(require,exports,module){
	require('jquery');
	var showTable = require("../member/showTableUpdate");
	showTable.ajaxUrl("/member/redpacket/availableRedPacketJSON.html?status=2",require("../../tpl/member/redpacket_availableRedPacket.tpl"));



	$("#choiceRedPacket-btn").live("click",function(){
		$.ajax({
                   type: "post",
                   url: "/modules/system/redPacket/exchangeRedPacket.html",
                   dataType: "json",
                   data:{'ids': $("#choiceRedPacket-btn").val(),'redPacket_id':$(".hb-id").val()},
                   success: function(data){
                    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
                      //构造确认框DOM
                      $.layer({
                        type: 1,
                        closeBtn: true,
                                title: "&nbsp;",
                          area: ['450px', '190px'],
                          border: [1, 1, '#cecfd0'],
                          page: {
                              html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='javascript:' onclick='javascript:window.location.href=window.location.href;'  class='okBtn'>"+"返回"+"</a></div></div>"
                          }
                      });
                  });
                
              }
              })
	})

	$("#choiceRedPacket-btn1").live("click",function()
	{
		location.href = "/nb/pc/product/productList.html?id=1";
	})
	//状态搜索
	$("#status a").live("click",function(){
		$(this).addClass("current").siblings().removeClass("current");
		search();
	});

	//搜索通用功能
	function search()
	{
		var tpl;
		var param;
		var statusVal = $("#status a.current").data("val");
		switch(statusVal)
		{
			case 1://未使用红包
			param = "/member/redpacket/availableRedPacketJSON.html?status=2";
			tpl = require("../../tpl/member/redpacket_availableRedPacket.tpl");
			break;
			case 2://已使用红包
			param = "/member/redpacket/availableRedPacketJSON.html?status=1";
			tpl = require("../../tpl/member/redpacket_availableRedPacket_used.tpl");
			break;
			case 3://已过期红包
			param = "/member/redpacket/availableRedPacketJSON.html?status=-1";
			tpl = require("../../tpl/member/redpacket_availableRedPacket_overdue.tpl");
			break;
		}
		showTable.ajaxUrl(param,tpl);
	}

});