define(function(require,exports,module){
	var showTable = require('./showTable');
	var search = function(param,tpl){
		//具体日期搜索
		$("#timeStatus a").click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			if( $(".sInput").val() == undefined){
				var param0 = param + "time="+$(this).data("val");
			}else{
				var param0 = param + "time="+$(this).data("val") + "&userName=" + encodeURIComponent($(".sInput").val()); ;
			}
			showTable.ajaxUrl(param0,tpl);
		});

		//日期范围搜索
		$(".searchBtn").click(function(){
			if( $(".sInput").val() == undefined || $(".sInput").val() == ""){
				var param0 = param;
			}else{
				var param0 = param + "userName=" + encodeURIComponent($(".sInput").val());
			}
			if($('#shortTime').is(":visible")){
				param0 += "&time="+$('#timeStatus a.current').data("val");
			}
			else if($('#fullTime').is(":visible"))	{
				param0 += "&startTime="+$('#startTime').val()+"&endTime="+$('#endTime').val();
			}
			showTable.ajaxUrl(param0,tpl);
		});
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

	
	
});