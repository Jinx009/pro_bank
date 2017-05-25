/*表单验证*/
define(function(require,exports,module){
	require('jquery');	
	$("#resetPhoneCode").click(function(){
		var that = $(this);
		var setUrl = "/user/getPwdCode.html?mobilePhone="+$('input[name="mobilePhone"]').val();
		var dataTime = 60;
		var Timer;
		if($("#mobilePhone").val()=="" || $("#phone_error").text()!=""){
			$("#input-row").addClass("input-error").find("#phone_error").html("请输入正确的手机号码");
			return false;
		}else{
			$.ajax({
				url:setUrl,
				type:"post",
				success:function(data){
					var result = data.result;
					if(result){
						Timer = setInterval(function(){
							dataTime--;
							if(dataTime>0){
								that.val(dataTime+"秒后重新发送").attr("disabled","disabled");
							}else{
								clearInterval(Timer);
								that.val("重新发送").removeAttr("disabled");
							}
						},1000)	
					}else{
						$("#input-row").addClass("input-error").find("#phone_error").html("该手机号不存在!");
					}
				}
			})
		}
	})
	
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){	
			$("#mobile_phonegetForm").validate({
				rules: {
					mobilePhone: {
						required: true,
						isMobile:true
				   	},
				   	validCode:{
				   		required:true,
				    },
				    pwd: {
						required: true,
						regexPassword:true
				    },
				    confirmNewPwd: {
						required: true,
						equalTo: "#password"
				    }  
				},
				messages:{
					mobilePhone:{
						required:"请输入手机号码",
						isMobile:"请输入正确的手机号码"
					},
					validCode:{
					   	 required:"请输入验证码"
				   	},
				    pwd: {
						required: "密码应包含英文和数字(特殊字符)" ,
						regexPassword:'密码格式错误'
					},
				    confirmNewPwd: {
						required: "请输入确认密码",
						equalTo: "两次输入密码不一致"
					}				   
				},
				errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				},
				success:function(element){
					element.parents(".input-row").removeClass("input-error");
				},
				submitHandler: function(form,event,validator){ 
				 	var checkFlag=false;
				 	var username="";
				    $.ajax({
						url:"/wx/phoneCode.html?phone="+$('input[name="mobilePhone"]').val()+"&type=check"+"&code="+$('input[name="validCode"]').val(),
						type:"post",
						async:false,
						success:function(data){
							var result = data.result;
							if(result==true){
								checkFlag=true;
								$("#username").val(data.username);
							}else{
								alert("验证码错误");
							}
						}
					})
					if(checkFlag){
						$(form).ajaxSubmit(function(data){
							form.submit();
			        	});
					}
				}  
			});
		})
	})
})