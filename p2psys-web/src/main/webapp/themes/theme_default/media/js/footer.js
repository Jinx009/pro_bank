define(function(require,exports,modlue){
	require('jquery');
	//解决IE下不支持placeholder
	require.async('common1',function(){
		if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder();
			});
		}
	})
	$("#backTop").live("click", function(){
		var _this = $(this);
		$('html,body').animate({ scrollTop: 0 }, 500 ,function(){
			_this.hide();
		});
	});
	$(window).scroll(function(){
		var htmlTop = $(document).scrollTop();
		if( htmlTop > 500){
			$("#backTop").show();
		}else{
			$("#backTop").hide();
		}
	});
	$(".wx").mouseover(function(){
		$(".wx-tu").removeClass("hide");
	})
	$(".wx").mouseout(function(){
		$(".wx-tu").addClass("hide");
	})
	
});