define(function(require,exports,module){
	var showTable = require('./showTable');
	var search = function(param,tpl){
		//具体日期搜索
		$("#timeStatus a").click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			if( $(".sInput").val() == undefined){
				var param0 = param + "time="+$(this).data("val") + "&status=" + $("#status a.current").data("val") ;
			}else{
				var param0 = param + "time="+$(this).data("val") + "&status=" + $("#status a.current").data("val") + "&borrowName=" + encodeURIComponent($(".sInput").val()); ;
			}
			showTable.ajaxUrl(param0,tpl);
		});

		//交易状态
		$("#status a").click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			if( $(".sInput").val() == undefined){
				var param0 = param + "status=" + $(this).data("val");
			}else{
				var param0 = param + "status=" + $(this).data("val") +"&borrowName=" + encodeURIComponent($(".sInput").val());
			}
			
			if (!$("#fullTime").is(":hidden")){
				if($("#startTime").val()!=""){
					param0 += "&startTime=" + $("#startTime").val();
				}
				if($("#endTime").val() != ""){
					param0 +=  "&endTime="+ $("#endTime").val();
				}
			}
			else{			
				param0 += "&time=" + $("#timeStatus a.current").data("val");
			}
			showTable.ajaxUrl(param0,tpl);
		});

		//日期范围搜索
		$("#searchBtn").click(function(){
			if( $(".sInput").val() == undefined || $(".sInput").val() == "")
			{
				var param0 = param + "status=" + $("#status a.current").data("val");
			}
			else
			{
				var param0 = param + "status=" + $("#status a.current").data("val") + "&borrowName=" + encodeURIComponent($(".sInput").val());
			}
			if($('#shortTime').is(":visible"))
			{
				param0 += "&time="+$('#timeStatus a.current').data("val");
			}
			else if($('#fullTime').is(":visible"))
			{
				param0 += "&startTime="+$('#startTime').val()+"&endTime="+$('#endTime').val();
			}
			showTable.ajaxUrl(param0,tpl);
		});
		
		//标种标题
		var nameVal = $(".sInput").val();
	};
	exports.search = search;
	//具体日期和日期范围切换
	$("#timeStatus em").click(function(){
		$("#shortTime").hide();
		$("#fullTime").show();
	});
	
	$("#fullTime em").click(function(){
		$("#fullTime").hide();
		$("#shortTime").show();
	});

	
	//类型展开切换
	if($("#status a").length > 8)
	{
		$("#status a:gt(7)").css("display","none");
		$("#status a:lt(8)").css("display","inline-block");
		$("#status").append('<em>更多类型&gt;&gt;</em>');
	}
	else
	{
		$("#status a:lt(8)").css("display","inline-block");
	}
	$("#status em").live('click',function(){
		if($("#status a:gt(7)").is(':hidden'))
		{
			$("#status a:gt(7)").css("display","inline-block");
			$("#status em").html('&lt;&lt;默认类型');
		}
		else
		{
			$("#status a:gt(7)").css("display","none");
			$("#status em").html('更多类型&gt;&gt;');
		}
	});
	
	
});