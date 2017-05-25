define(function(require,exports,module){
	require('jquery');
	$.ajax({
		type:"post",
		url:"/member_borrow/security/realNameJson.html"+"?random="+(new Date()).getTime(),
		dataType:"json",
		success:function(json){
			require.async('/plugins/handlebars-v1.3.0/handlebars-v1.3.0',function(){
	            require.async('/plugins/handlebars-v1.3.0/transFormatJson',function(){
				$(".payAccountForm").html(Handlebars.compile(require("../../tpl/security/security_realNameIdentify.tpl"))(json));
			  });
            });
		}
	})
	$(".payAccountBtn").live("click",function(){
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
			    type: 1,
			    closeBtn: false,
			    title: false,
			    area: ['460px', '240px'],
			    shade: [0.5, '#000'],
			    border: 0,
			    page: {
			        html: '<div class="fileconfirm"><div class="fileconfirm_title"><h1 class="float_left">认证提示</h1><span class="float_right closeBtn">X</span></div><div class="fileconfirm_msg"><p>您即将跳往汇付天下进行认证操作，认证完成前，请不要关闭本窗口。认证完成后，请根据您的认证结果点击下面按钮。</p></div><div class="fileconfirmbtn"><a href="/wx/account/main.html" class="okBtn">认证成功</a><a href="/wx/account/realNameIdentify.html" class="cancleBtn failBtn">认证失败</a></div></div>'
			    }
			});	
			//绑定成功操作
			$(".okBtn").click(function(){
				location.reload();				
			});
			//关闭窗口操作
			$(".closeBtn").click(function(){
				layer.closeAll();
			});
			//绑定失败操作
			$(".failBtn").click(function(){
				location.reload();
			});
			
	});
	});
});