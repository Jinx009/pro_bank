define(function(require,exports,module){
	require('jquery');
	// 校验码验证
	// require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
	// 	require.async("/plugins/jquery-validation-1.13.0/additional-methods",function(){
	// 		$("#emailgetForm").validate({
	// 			rules:{
	// 				code:{
	// 					required:true
	// 				}
	// 			},
	// 			messages:{
	// 				code:{
	// 					required:"请输入校验码"
	// 				}
	// 			},
	// 			errorElement:"em",
	// 			errorPlacement:function(error,element){
	// 				element.parent("dd").next(".error").css("display","block").html("<label></label>"+error.html());
	// 			},
	// 			submitHandler:function(form,event,validator){
	// 				require.async("jquery.form",function(){
	// 					$(form).ajaxSubmit(function(data){
	// 						$(".forgetPwdForm dd.error").css("display","block");
	// 						if(data.result)
	// 						{
	// 							$(".forgetPwdForm dd:eq(2)").html('<label></label>密码找回成功，请<a href="/user/login.html">重新登录</a>！');
	// 						}
	// 						else
	// 						{
	// 							$(".forgetPwdForm dd:eq(2)").html("<label></label>"+data.msg);
	// 						}
	// 					})
	// 				})
	// 			}
	// 		});
	// 	})
	// })

	$("#resetEmailCode").click(function(){
		var that = $(this)
		var setUrl = "/user/getPwdCode.html?email="+$('input[name="email"]').val();
		var dataTime = 60;
		var Timer;
		$.ajax({
			url:setUrl,
			type:"post",
			success:function(data){
				if(data.result==true)
				{
					Timer = setInterval(function(){
						dataTime--;
						if(dataTime>0)
						{
							that.val(dataTime+"秒后重新发送").attr("disabled","disabled");
						}
						else{
							clearInterval(Timer);
							that.val("点击重新发送").removeAttr("disabled");
						}
					},1000)
				}
			}
		})
	})
		
	
})

