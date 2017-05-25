define(function(require, exports,module){
	require('jquery');
	//解决IE下不支持placeholder
	//解决IE不支持HTML5表单属性placeholder的问题
(function ($) {
    $.fn.placeholder = function (options) {
        var defaults = {
            pColor: "#ccc",
            pActive: "#999",
            pFont: "14px",
            activeBorder: "#080",
            posL: 10,
            zIndex: "99"
        },
        opts = $.extend(defaults, options);
        //
        return this.each(function () {
            if ("placeholder" in document.createElement("input")) return;
            $(this).parent().css("position", "relative");
            var isIE = $.browser.msie,
            version = $.browser.version;
  
            //不支持placeholder的浏览器
            var $this = $(this),
                msg = $this.attr("placeholder"),
                iH = $this.outerHeight(),
                iW = $this.outerWidth(),
                iX = $this.position().left,
                iY = $this.position().top,
                oInput = $("<label>", {
                "class": "placeholderCss",
                "text": msg,
                "css": {
                    "position": "absolute",
                    "left": iX + "px",
                    "top": iY + "px",
                    "height": iH + "px",
                    "line-height": iH + "px",
                    "color": opts.pColor,
                    "font-size": opts.pFont,
                    "z-index": opts.zIndex,
                    "cursor": "text"
                }
                }).insertBefore($this);
            //初始状态就有内容
            var value = $this.val();
            if (value.length > 0) {
            oInput.hide();
            };
  
            //
            $this.on("focus", function () {
                var value = $(this).val();
                if (value.length > 0) {
                    oInput.hide();
                }
                oInput.css("color", opts.pActive);
                //
  
                if(isIE && version < 9){
                    var myEvent = "propertychange";
                }else{
                    var myEvent = "input";
                }
  
                $(this).on(myEvent, function () {
                    var value = $(this).val();
                    if (value.length == 0) {
                        oInput.show();
                    } else {
                        oInput.hide();
                    }
                });
  
            }).on("blur", function () {
                var value = $(this).val();
                if (value.length == 0) {
                    oInput.css("color", opts.pColor).show();
                }
            });
            //
            oInput.on("click", function () {
                $this.trigger("focus");
                $(this).css("color", opts.pActive)
            });
            //
            $this.filter(":focus").trigger("focus");
        });
    }
})(jQuery)

	


$(".investors").click(function(){
	$(".borrowers").removeClass("reg-tabhover")
	$(".user-type").val("1")
	if(($(this).is(".reg-tabhover"))){	
	}
	else{
     $(".investors").addClass("reg-tabhover")
	}
})

$(".borrowers").click(function(){
	$(".investors").removeClass("reg-tabhover")
	$(".user-type").val("2")
	if(($(this).is(".reg-tabhover"))){	
	}
	else{
     $(".borrowers").addClass("reg-tabhover")
	}
})
		
	//加载城市选择插件
	require.async('jquery-citySelect/jquery.cityselect',function(){
		var prov = $(".province").val();
		var city =$(".cityOpt").val();
		var dist =$(".dist").val();
		$("#city").citySelect({
			url:"../../../themes/theme_default/media/js/jquery-citySelect/city.json",
			prov:"上海", //省份 
		    city:"黄浦区", //城市 
		    dist:"", //区县 
		    required:true,
		    nodata:"none" //当子集无数据时，隐藏select  
			});
	})
	//协议弹窗
	$("#service_contract").click(function(){
		require.async("/plugins/layer-v1.8.4/layer.min",function(){
			var i = $.layer({
				type :1,
				title : "协议内容",
				closeBtn :[0,true],
				border : [10 , 0.3 , '#000', true],
				area :['auto','550px'],
				page : {dom:'#modal_dialog'}
			});
		})
	});

	$("#service_contract1").click(function(){
		require.async("/plugins/layer-v1.8.4/layer.min",function(){
			var i = $.layer({
				type :1,
				title : "协议内容",
				closeBtn :[0,true],
				border : [10 , 0.3 , '#000', true],
				area :['auto','550px'],
				page : {dom:'#modal_dialog1'}
			});
		})
	});
	//邮箱调用
	require.async('commonJS/jquery.mailAutoComplete',function(){
		$("#customTest").mailAutoComplete({
			boxClass: "out_box",
			listClass: "list_box",
			focusClass: "focus_box",
			markCalss: "mark_box",
			autoClass: false,
			textHint: true,
			hintText: ""
		});
	})
	//表单验证
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#signupForm").validate({
				rules: {
				  	userName:{
				  		required:true,
				  		regexUserName:true,
						remote:{
								type: "POST",
								url: "/user/checkUserName.html",
								dataType: "json",
								data:{userName: function(){return $("#username").val();}}
							}
						},
				    ui:{
						remote:{
								type: "POST",
								url: "/user/inviteRegister.html",
								dataType: "json",
								data:{ui: function(){return $("#invite_username").val();}}
							}
						},
					email: {
							required: true,
							email:true,
							remote:{
								type: "get",
								url: "/user/checkEmail.html",
								data:{
									email: function(){
										return $("#customTest").val();
									}
								}
							}	
				   		},
				   		mobilePhone: {
							required: true,
							isMobile:true,
							remote:{
								type: "get",
								url: "/user/checkMobilePhone.html",
								data:{
									mobilePhone: function(){
										return $("#mobilePhone").val();
									}
								}
							}	
				   		},
				   	code:{
				   		required:true
				   	},
				   pwd: {
							required: true,
							regexPassword:true
				   },
				   confirmPassword: {
							required: true,
							equalTo: "#password"
				   },
				   agree:{
				   		required:true,
				   },
				   cardId:{
				   		isIdCardNo:true
				   },
				   realName:{
				   		required: true,
				   		realName:true
				   }
				},
				messages:{
				   	userName:{
							required:"用户名由英文字母、数字组成,且不能为纯数字",
							regexUserName:"用户名由4到16位的英文字母、数字组成",
							remote:"该用户名已经存在"
					   },
					ui:{
							remote:"邀请码不正确"
					   },
				    email: {
							required: "请输入正确的email地址",
							email: "请输入正确的email地址",
							remote:"邮箱地址已经存在"
						   },
					mobilePhone:{
						required:"请输入手机号码",
				   		isMobile:"手机号码格式错误",
				   		remote:"手机号码已经存在！"
				   	},
				   	code:{
				   		required:"手机验证码格式错误"
				   	},
				   pwd: {
							required: "请输入长度为8到16位的密码,包含英文和数字(特殊字符)" ,
							regexPassword:'密码格式错误'
						},
				   confirmPassword: {
							required: "请输入确认密码",
							equalTo: "两次输入密码不一致"
						},
						validCode:{
					   			required:"请输入验证码"
					   	},
						agree:{
						   	required:"请勾选服务协议条款"
						},
						cardId:{
						   	required:"请填写您的身份证号",
						   	isIdCardNo:"身份证号码格式错误"
						},
						realName:{
						   	required:"请填写您的真实姓名",
						   	realName:"真实姓名不正确"
						}
				},
				errorPlacement:function(error, element){
				  	element.parents("li").find(".msg_tip").html(error);	
				},
				success:function(element){
					element.parents("li").find(".msg_tip").html("");
				},
				submitHandler: function(form,event,validator) 
				{      
				   	require.async('jquery.form',function(){
				   		$(form).ajaxSubmit(function(data){
					    	  if(data.result ==true)
				    		  {
				    		  	$(".js_sucEmail").text(data.email);
					    		$(".js_resetEmail").val(data.email);
					    		$(".js_userid").val(data.userId);
					    		$(".js_sucUsername").text(data.userName);
				    		  	$(".reg_process li:eq(1)").addClass("hover").siblings().removeClass("hover");
				    		  	$(".reg_content").slideUp();
				    		  	$(".success_reg_con").slideDown();
					    		 loginEmail(data.email);
				    		  }
				    		  else
				    		  {
				    		  	$("input[name='validCode']").val('');
				    		  	if(data.msg == "用户名格式错误！")
				    		  	{
				    		  		$(".reg_table li:eq(0)").find(".msg_tip").html('<label id="customTest-error" class="error" for="customTest">用户名格式错误！</label>');
				    		  	}
				    		  	else if(data.msg == "验证码错误！")
				    		  	{
				    		  		$(".reg_table li:eq(5)").find(".msg_tip").html('<label id="customTest-error" class="error" for="customTest">验证码错误！</label>');
				    		  	}
				    		  	else if(data.msg == "手机已经被使用")
				    		  	{
				    		  		$(".reg_table li:eq(2)").find(".msg_tip").html('<label id="customTest-error" class="error" for="customTest">手机号码已经存在！</label>');
				    		  	}
				    			$(".valicode_img").each(function(){$(this).attr("src",'/validimg.html?t=' + Math.random());})
				    		  }
				        });
				   	})
				}  
			});
		})
	})
	//密码强弱判断
	$("#password").keyup(function(){
		var strongRegex = new RegExp("^(?=.{8,})(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W).*$", "g"); 
		var mediumRegex = new RegExp("^(?=.{8,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g"); 
		var enoughRegex = new RegExp("(?=.{8,}).*", "g"); 
		var $pw = $(".passwordStrength");
		if (false == enoughRegex.test($(this).val())) { 
			$pw.removeClass('level1 level2 level3').addClass('level0'); 
			//密码小于八位的时候，密码强度图片都为灰色 
		} 
		else if (strongRegex.test($(this).val())) {
			$pw.removeClass('level1 level2 level3').addClass('level3');
			//密码为八位及以上并且字母数字特殊字符三项都包括,强度最强 
		} 
		else if (mediumRegex.test($(this).val())) { 
			$pw.removeClass('level1 level2 level3').addClass('level2');
			//密码为八位及以上并且字母、数字、特殊字符三项中有两项，强度是中等 
		} 
		else { 
			$pw.removeClass('level1 level2 level3').addClass('level1');
			//如果密码为8为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的 
		} 
		return true; 
	});
	//邮件再次发送
	$("#reset_email").click(function(){
	    	var  that = $(this)
	    	var EmailVal = $(".js_sucEmail").text();
	    	var userid = $(".js_userid").val();
	    	var timeNum = 60;
	    	resttEmailUrl = "/user/sentActivationEmail.html?userId="+userid;
	    	$.ajax({
	    		url:resttEmailUrl,
	    		type:"post",
	    		data:{email:EmailVal},
	    		success:function(data){
	    			var result = data.result;
	    			var Time = "";
	    			if(result==true)
    				{
    					Time = setInterval(function(){
    						timeNum--;
    						if(timeNum>0){
    							that.val(timeNum+"秒后重发").addClass("disabled");
    						}
    						else{
    							clearInterval(Time);
    							that.val("重新发送").removeAttr("disabled");
    						}
    					},1000)
    				}
	    		}
	    	});
	    });
	
		//["qq.com","gmail.com","126.com","163.com","hotmail.com","yahoo.com","yahoo.com.cn","live.com","sohu.com","sina.com"]	
		//点击登录邮箱地址
	    function loginEmail(emailValue){
	    	var loginEmailBox = $("#loginEmail");
	    	var loginEmailValue = "";
	    	emailValue = (emailValue.split("@"))[1];
	    	switch (emailValue)
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
	    	loginEmailBox.attr("href","http://"+loginEmailValue);
	    } 

	    //邮箱倒计时
	    var time = parseInt($(".remain_time").text())
	    if(time > 0)
	    {
	    	var t = setInterval(function(){
	    		time --;
	    		$(".remain_time").text(time);
	    		if(time <= 1)
	    		{	
	    			window.location.href = $(".reg_link").attr("href");
	    		}
	    	},1000);
	    }
	    
});


