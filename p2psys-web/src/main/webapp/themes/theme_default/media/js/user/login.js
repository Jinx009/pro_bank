define(function(require,exports,module){
	require('jquery');
	//解决IE下不支持placeholder
	
	require.async('common1',function(){
		if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder();
			});
		}
	})
	
	//前台登陆页面验证
	var count = $("#count").val();
	if( count == 1){
		$(".showErrorTips").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
	    $(".valicode_img").click(function(){
	    	$(this).attr("src",'/validimg.html?t=' + Math.random());
	    });
	}

	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#login").validate({
				rules:{
			    	userName:{
			    		required: true,
			    		minlength: 4
			    	},
			    	password:{
			    		required: true,
			    		regexPassword:true
			    	},
			    	validCode:{
			    		required: true,
			    		minlength: 4
			    	}
			    },
			    messages:{
			    	userName:{
			    		required: "请输入用户名",
			    		minlength: "用户名格式错误"
			    	},
			    	password:{
			    		required: "请输入密码",
			    		regexPassword:"密码格式错误"
			    	},
			    	validCode:{
			    		required: "请输入验证码",
			    		minlength: "验证码格式错误"
			    	}
			   },
			 	errorElement:'em',
			 	showErrors:function(errorMap, errorList){
			 		this.defaultShowErrors();
			 		$(".showErrorTips").css("display","none");
			 		if(errorMap.userName){
			 			$(".showErrorTips").css("display","block");
			 			$(".errorTips b").html(errorMap.userName);
			 		}else if(errorMap.password){
			 			$(".showErrorTips").css("display","block");
			 			$(".errorTips b").html(errorMap.password);
			 		}else if(errorMap.validCode){
			 			$(".showErrorTips").css("display","block");
			 			$(".errorTips b").html(errorMap.validCode);
			 		}else{
			 			$(".errorTips b").html('');
			 		}
			 	},
				errorPlacement:function(error,element){
			    },
			    submitHandler:function(form){
			    	require.async('jquery.form',function(){
				    	$(form).ajaxSubmit(function(data){
				        	var resultMsg = data.msg||""; 
				        	if(data.result){ 			
				        		var url = "/member/main.html"
				        		if (data.redirectURL != "") {
				        			url = data.redirectURL;
				        		}
				        		window.location.href = url;
				        	}else{
				        		$(".showErrorTips").css("display","block");
				        		$(".errorTips b").html(resultMsg);
				        		if(data.count){
				        			$(".showErrorTips").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
				        		    $(".valicode_img").click(function(){
				        		    	$(this).attr("src",'/validimg.html?t=' + Math.random());
				        		    });
				        		    $("input[typ='validCode']").val('');
				        		}else{
				        			$(".errorTips b").html(resultMsg);
					        		$("#email").val(data.email)
					        		$("#userid").val(data.userid);
					        		$("input[typ='validCode']").val('');
					        		$(".valicode_img").each(function(){
										$(this).attr("src",'/validimg.html?t=' + Math.random());
					        		})
				        		}
				        	} 			        	
				    	}); 
			    	})
			     }  
			});


		})
	})

	$("#reset_email").live("click",function(){
    	$.ajax({
    		url:"/user/sentActivationEmail.html?userId="+$("#userid").val(),
    		type:"post",
    		data:{email:$("#email").val()},
    		success:function(data){
    			if(data)
    			{
    				loginEmail($("#email").val());
    			}
    			else
    			{
    				$(".errorTips b").html("邮件发送失败！");
    			}
    			
    		}
    	});
	});

	//["qq.com","gmail.com","126.com","163.com","hotmail.com","yahoo.com","yahoo.com.cn","live.com","sohu.com","sina.com"]	
	//点击登录邮箱地址
    function loginEmail(emailValue){
    	var email_suffix = (emailValue.split("@"))[1];
    	var loginEmailValue = "";
    	switch (email_suffix)
    	{
    		case "qq.com":
    			loginEmailValue = "mail.qq.com";
    			break;
    		case "gmail.com":
    			loginEmailValue = "mail.google.com";
    			break;
    		case "126.com":
    			loginEmailValue = "mail.126.com";
    			break;
    		case "163.com":
    			loginEmailValue = "mail.163.com";
    			break;
    		case "hotmail.com":
    			loginEmailValue = "login.live.com";
    			break;
    		case "yahoo.com":
    			loginEmailValue = "login.yahoo.com";
    			break;
    		case "live.com":
    			loginEmailValue = "login.live.com";
    			break;
    		case "sohu.com":
    			loginEmailValue = "mail.sohu.com";
    			break;
    		case "sina.com":
    			loginEmailValue = "mail.sina.com";
    			break;	
    	}
    	if(loginEmailValue)
    	{
    		$(".errorTips b").html('邮件已发送，点击<a href="http://'+loginEmailValue+'" target="_blank">立即激活</a>');
    	}
    	else
    	{
    		$(".errorTips b").html("邮件已发送，请注意查收！");
    	}
    }
})

