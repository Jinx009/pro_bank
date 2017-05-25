define(function(require,exports,module){
	require('jquery');
	$("#resetPhoneCode").click(function(){
		var that = $(this);
		var setUrl = "/user/getPwdCode.html?mobilePhone="+$('input[name="mobilePhone"]').val();
		var dataTime = 60;
		var Timer;
		$.ajax({
			url:setUrl,
			type:"post",
			success:function(data){
				var result = data.result;
				if(result==true)
				{
					Timer = setInterval(function(){
						dataTime--;
						if(dataTime>0)
						{
							that.val(dataTime+"秒后重新发送").attr("disabled","disabled");
						}
						else{
							clearInterval(Timer);
							that.val("重新发送").removeAttr("disabled");
						}
					},1000)
				}
			}
		})
	})
})

