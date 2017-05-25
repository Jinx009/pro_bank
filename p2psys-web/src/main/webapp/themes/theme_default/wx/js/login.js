define(function(require,exports,module)
{
	require('jquery');
	/**
	 * 解决IE不支持placeholder
	 */
	require.async('common1',function()
	{
		if($.browser.msie) 
		{ 
			$(":input[placeholder]").each(function()
			{
				$(this).placeholder();
			});
		}
	})
	
	/**
	 * 微信前台登录
	 */
	var count = $("#count").val();
	if( count == 1)
	{
		$(".showErrorTips").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
	    $(".valicode_img").click(function()
	    {
	    	$(this).attr("src",'/validimg.html?t=' + Math.random());
	    });
	}

	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function()
	{
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function()
		{
			$("#login").validate(
			{
				rules:
				{
			    	userName:
			    	{
			    		required: true,
			    		minlength: 4
			    	},
			    	password:
			    	{
			    		required: true,
			    		regexPassword:true
			    	}
			    },
			    messages:
			    {
			    	userName:
			    	{
			    		required: "请输入用户名",
			    		minlength: "用户名格式错误"
			    	},
			    	password:
			    	{
			    		required: "请输入密码",
			    		regexPassword:"密码格式错误"
			    	}
			    	
			   },
			    errorPlacement:function(error, element)
			    {
				  element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
			    },
			    success:function(element)
			    {
			    	element.parents(".input-row").removeClass("input-error");
			    },
			    submitHandler:function(form)
			    {
			    	form.action="../user/doLogin.action";
			    	require.async('jquery.form',function()
			    	{
				    	$(form).ajaxSubmit(function(data)
				    	{
				        	var resultMsg = data.msg||""; 
				        	if(data.result)
				        	{ 			
			        		var url = "/wx/account/main.html";
				        		if (data.redirectURL != "") 
				        		{
				    
				        			url= data.redirectURL;
				        			var storage = window.sessionStorage;
				        				
				        			if(storage.getItem("wechat_type"))
				        			{
				        				if("1"!==storage.getItem("wechat_type"))
				        				{
				        					url="/wx/elect.html?redirectUrl="+data.redirectURL;
				        				}
				        			}
				        			if(!storage.getItem("wechat_type"))
				        			{
				        				storage.wechat_type = 1;
				        				url="/wx/elect.html?redirectUrl="+data.redirectURL;
				        			}
				        			
				        		 }
				        		window.location.href = url;
				        	}
				        	else
				        	{
				        		$(".showErrorTips").css("display","block");
				        		$(".errorTips b").html(resultMsg);
				        		if(data.count)
				        		{
				        			$(".showErrorTips").before('<div class="input-row input-button-row"><div class="input-row-inner"><input class="input-text" name="validCode" autocomplete="off" placeholder="验证码" type="text" tabindex="3" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="input-button" alt="点击刷新" /></div></div>');
				        		    $(".input-button").click(function()
				        		    {
				        		    	$(this).attr("src",'/validimg.html?t=' + Math.random());
				        		    });
				        		    $("input[name='validCode']").val('');
				        		}
				        		else
				        		{
				        			$(".errorTips b").html(resultMsg);
					        		$("#userid").val(data.userid);
					        		$("input[name='validCode']").val('');
					        		$(".input-button").each(function()
					        		{
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
	/**
	 * 控制底部的logo显示问题
	 */
	$("#username").focus(function()
	{
		$(".login_logo").hide();
	});
	$("#username").blur(function()
	{
		$(".login_logo").show();
	});
	$("#password").focus(function() 
	{
		$(".login_logo").hide();
	});
	$("#password").blur(function()
	{
		$(".login_logo").show();
	});

})

