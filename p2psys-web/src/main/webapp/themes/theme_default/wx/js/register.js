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
            posL: 44,
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

		if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder();
			});
		}


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
		
	//服务产品协议弹窗
	$("#service_contract").click(function(){
		require.async("/plugins/layer-v1.8.4/layer.min",function(){
			var i = $.layer({
				type :1,
				title : "协议内容",
				closeBtn :[0,true],
				border : [10 , 0.3 , '#000', true],
				area :['310px','250px'],
				page : {dom:'#modal_dialog'}
			});
		})
	});
	//风险提示条款弹窗
	$("#service_contract1").click(function(){
		require.async("/plugins/layer-v1.8.4/layer.min",function(){
			var i = $.layer({
				type :1,
				title : "风险条款",
				closeBtn :[0,true],
				border : [10 , 0.3 , '#000', true],
				area :['310px','250px'],
				page : {dom:'#modal_dialog1'}
			});
		})
	});
	//表单验证
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#signupForm").validate({
				rules: {
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
					    validCode:{
					   		required:true,
					    },
					    agree:{
					   		required:true,
					    }
				},
				messages:{
					mobilePhone:{
						required:"请输入手机号码",
				   		isMobile:"手机号码格式错误",
				   		remote:"手机号码已经存在！"
				   	},
				   	code:{
				   		required:"手机验证码格式错误"
				   	},
				    pwd: {
						required: "至少8个字符，包含字母和数字" ,
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
					}
				},
				errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				},
				success:function(element){
					element.parents(".input-row").removeClass("input-error");
				},
				submitHandler: function(form,event,validator) 
				{      
				   	require.async('jquery.form',function(){
				   		$(form).ajaxSubmit(function(data){
					    	  if(data.result ==true){
//					    		$(".js_userid").val(data.userId);
//					    		$(".js_sucUsername").text(data.userName);
//				    		  	$(".reg_process li:eq(1)").addClass("hover").siblings().removeClass("hover");
				    		  	$(".reg_content").slideUp();
				    		  	$(".success_reg_con").slideDown();
					    		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
										//构造确认框DOM
										$.layer({
											type: 1,
											closeBtn: true,
							                title: "&nbsp;",
										    area: ['450px', '190px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'><span>恭喜您，注册成功！现在就去"+"<a style='color:#f0f;font-weight:bold;text-decoration:none;' href='/wx/login.html' target='_blank'>"+"登录"+"</a></span></div>"+"<div class='tipsBtnBar'>"+"<a href='/wx/login.html' class='okBtn'>"+"确定"+"</a></div></div>"
										    }
										});
								}); 
				    		  }else 
				    		  {
				    			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
										//构造确认框DOM
										$.layer({
											type: 1,
											closeBtn: true,
							                title: "&nbsp;",
										    area: ['450px', '190px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: "<div class='tipsWrap w450'>"+"<div  class='tipsTxt'><span id='msg-xx' >手机校验码错误!</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/wx/register.html' class='okBtn'>"+"重新注册"+"</a></div></div>"
										    }
										});
								});
				    		  	$("input[name='validCode']").val('');
				 
				    			$("#valicode_img").each(function(){$(this).attr("src",'/validimg.html?t=' + Math.random());})
				    		  }
				        });
				   	})
				}  
			});
		})
	})
	/* 判断是否有勾选协议 */
	$("#zhuce").click(function(){
		if($("#dianji").attr("checked")){
			$(this).val() = 1;
			$(this).submit();
		}else{
			$("#pro_msg").html("亲，请先勾选协议哦");
		}
	});
	//密码强弱判断
	// $("#password").keyup(function(){
	// 	var strongRegex = new RegExp("^(?=.{8,})(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W).*$", "g"); 
	// 	var mediumRegex = new RegExp("^(?=.{8,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g"); 
	// 	var enoughRegex = new RegExp("(?=.{8,}).*", "g"); 
	// 	var $pw = $(".passwordStrength");
	// 	if (false == enoughRegex.test($(this).val())) { 
	// 		$pw.removeClass('level1 level2 level3').addClass('level0'); 
			//密码小于八位的时候，密码强度图片都为灰色 
		// } 
		// else if (strongRegex.test($(this).val())) {
		// 	$pw.removeClass('level1 level2 level3').addClass('level3');
			//密码为八位及以上并且字母数字特殊字符三项都包括,强度最强 
		// } 
		// else if (mediumRegex.test($(this).val())) { 
		// 	$pw.removeClass('level1 level2 level3').addClass('level2');
			//密码为八位及以上并且字母、数字、特殊字符三项中有两项，强度是中等 
		// } 
		// else { 
		// 	$pw.removeClass('level1 level2 level3').addClass('level1');
			//如果密码为8为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的 
	// 	} 
	// 	return true; 
	// });
	    
});


