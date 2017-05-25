define(function(require,exports,module){
	require('jquery');
	
	//TAB标签
	require.async('/themes/theme_default/media/js/common1',function(){
		$(".noticeConfig").tabChange({
			isClick:true,
		    isHover:false,
			childLi:".noticeConfig_tab li",//tab选项卡
			childContent:".noticeConfig_tab_content",//tab内容
			hoverClassName:"hover",//选中当前选项卡的样式
			callBack:false	
		});
	});
	
	$(".switchBtn").click(function(){
		if($(this).hasClass("on")){
			$(this).removeClass("on").attr("title","点击开启");
			$(this).parent().siblings().attr("checked",false);
			$(this).parent().siblings().attr("value","0");
		}else{
			$(this).addClass("on").attr("title","点击关闭");
			$(this).parent().siblings().attr("checked",true);
			$(this).parent().siblings().attr("value","1");
		}
	});
	//设置选中效果
	$(".on_off:checked").siblings().find(".switchBtn").addClass("on");
	
	//表单提交
	$(".saveSetting_btn").click(function(){
		require.async('/themes/theme_default/media/js/jquery.form',function(){
			$("#form1").ajaxSubmit(function(data){
				if(data.result){ 			
	        		window.location.href = "/member/message/send.html";
	        	}
	    	})
		});
	});

});