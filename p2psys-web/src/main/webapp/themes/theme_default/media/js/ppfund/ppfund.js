define(function(require,exports,module){
	require('jquery');
	var showTable = require("../member/showTableUpdate");
	showTable.ajaxUrl("/ppfund/myInList.html?",require("../../tpl/ppfund/myIn.tpl"));
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
			case 1://我的购买
			param = "/ppfund/myInList.html?";
			tpl = require("../../tpl/ppfund/myIn.tpl");
			break;
			case 2://我的转出
			param = "/ppfund/myOutList.html?";
			tpl = require("../../tpl/ppfund/myOut.tpl");
			break;
		}
		showTable.ajaxUrl(param,tpl);
	}

	$(".zc").live("click",function(){
		var zcid = $(this).parent().find(".zcid").val()
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
				type: 1,
				closeBtn: true,
                title: "&nbsp;",
			    area: ['450px', '190px'],
			    border: [1, 1, '#cecfd0'],
			    page: {
			        html: "<div class='tipsWrap w450' id='thps'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"是否确定转出？"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='###' class='okBtn' id='okBtn' >"+"确定"+"</a>"+"<a href='###' class='okBtn' id='okClose'>"+"取消"+"</a></div></div>"
			    }
			});
			
			$("#okBtn").click(function(){
				$.ajax({
					url:zcid,
					type:"get",
					dataType:"json",
					success:function(json){
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
							//构造确认框DOM
							$.layer({
								type: 1,
								closeBtn: true,
				                title: "&nbsp;",
							    area: ['450px', '190px'],
							    border: [1, 1, '#cecfd0'],
							    page: {
							        html: "<div class='tipsWrap w450' id='thps'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"转出成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/ppfund/ppfund.html' class='okBtn' >"+"确定"+"</a></div></div>"
							    }
							});
					});
						}
					})
				layer.closeAll();
			})
			
			$("#okClose").click(function(){
				layer.closeAll();
			})
	});
	})

	$(".yz").live("click",function(){
		var $this = $(this);
		$("#startTime").val("");
		$("#time .content").html("");
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			
			require.async('/themes/theme_default/media/js/date',function(){
			require.async('/plugins/laydate-v1.1/laydate',function(){
            	$(".searchBtn").click(function(){	

            		var yztime = $(".dateInput").val();

            		if(yztime==""){
            			$("#time .content").html("请选择预约转出时间");
            			return false;
            		}
            		else{
            		 
					$.ajax({
						url:"/ppfund/updateOutTime.html?id="+$this.next().html()+"&outTime="+$("#startTime").val(),
						type:"get",
						dataType:"json",
						success:function(json){
						    
							if(json.result==false){
								$("#time .content").html(json.msg);						
						     }
						else{
							require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
									//构造确认框DOM
									$.layer({
										type: 1,
										closeBtn: true,
						                title: "&nbsp;",
									    area: ['450px', '190px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: "<div class='tipsWrap w450' id='thps'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"恭喜您，预约成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/ppfund/ppfund.html' class='okBtn' >"+"确定"+"</a></div></div>"
									    }
									});
							});
						}
						}
					})
				}
				})
					
			$.layer({
				type: 1,
				closeBtn: [0,true],
				title: "请输入转出时间",
				area: ['300px', '150px'],
				border: [1, 1, '#cecfd0'],
				page: {dom : '#time'}
					
			});
			});
			});
		});
	});

})