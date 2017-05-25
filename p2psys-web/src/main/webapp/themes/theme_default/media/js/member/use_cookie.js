define(function(require,exports,modlue){
	require.async('../member/cookie',function(){
	require.async('./cookie',function(){
		function use_cookie(){
		var conMsg = "";
		var conTit = "";
		var username = $(".user_headshot").find("p").text();
		var alert_name = $.cookie("URLName")
		if(alert_name=="false")
		{
			return false;	
		}
		if(real_status=="3")
		{
			conMsg = "尊敬的用户"+username+"：欢迎来到"+webname+",请先进行<a href='/member/security/setting.html' style='color:#f00'>实名认证</a>";
			conTit = "实名认证"
			layerMes(conMsg,conTit)	
			
		}
		if(phone_status=="3"&&real_status=="2")
		{
			conMsg = "尊敬的用户"+username+"：欢迎来到"+webname+",您实名认证正在审核，请继续进行<a href='/member/security/setting.html' style='color:#f00'>手机认证</a>";
			conTit = "手机认证"
			layerMes(conMsg,conTit)	
		}
		if(phone_status=="3"&&real_status=="1")
		{
			conMsg = "尊敬的用户"+username+"：欢迎来到"+webname+",您已经完成实名认证，请继续进行<a href='/member/security/setting.html' style='color:#f00'>手机认证</a>";
			conTit = "手机认证"
			layerMes(conMsg,conTit)	
		}
		if(phone_status=="1"&&real_status=="1"&&email_status=="3")
		{
			conMsg = "尊敬的用户"+username+"：欢迎来到"+webname+",您已经完成手机认证，实名认证，请继续进行<a href='/member/security/setting.html' style='color:#f00'>邮箱认证</a>";
			conTit = "邮箱认证"
			layerMes(conMsg,conTit)	
		}
		
		function layerMes(conMsg,conTit)
		{
			layer.confirm(conMsg,
			function(index){
				layer.close(index);
				$.cookie("URLName","false",{expires:1000})
			},conTit);
			$(".xubox_botton2").text("永久忽略");
			$(".xubox_botton3").text("下次提醒");
		}
	}//账户中心判断
		use_cookie();
	});	
	})
});