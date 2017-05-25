var myreg = /^[0-9\ ]+$/;
var myreg2 = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
/**
 * 绑定成功跳转
 */
function successUrl()
{
	
	location.href = "/nb/wechat/product/product_menue.action";
}
/**
 * 发送验证码
 */
function sendMsg()
{
	var tel = $("#tel").val();
	
	if(validateTel(tel))
	{
		$.ajax({
			url:"/nb/wechat/getBindCode.html?tel="+tel,
			type:"GET",
			success:function(res)
			{
				if("success"==res.result)
				{
					var time=60;
					
					var timeFun=setInterval(function()
					{
						time--;
						
		 				if(time>0)
		 				{
		 					$('#tel_msg').html(time+"秒后重新获取").attr("disabled",true);
		 				}
		 				else
		 				{
		 					time=60;
		 					
		 					$('#tel_msg').html("获取验证码").removeAttr("disabled");
		 					
		 					clearInterval(timeFun);
						}
					},1000)
				}
			}
		})
	}
	else
	{
		$("#error_msg").html("手机号码不正确!");
	}
}

/**
 * 绑定方法
 */
function bindAccount()
{
	var tel = $("#tel").val();
	var pwd = $("#pwd").val();
	var yzm = $("#yzm").val();
	var couponCode = $("#couponCode").val();
	
	var params = "tel="+tel+"&pwd="+pwd+"&yzm="+yzm+"&bind_status="+bind_status+"&couponCode="+couponCode;
	var params2 = "tel@"+tel+"!couponCode@"+couponCode;
	
	if(false==validateTel(tel))
	{
		$("#error_msg").html("手机号码不正确!");
	}
	else if(""===pwd||null===pwd||pwd.length<8)
	{
		$("#error_msg").html("密码至少为8位数字与字母组合");
	}
	else
	{
		$.ajax({
			url:"/nb/wechat/writeBindData.action",
			type:"POST",
			data:"bindData="+params2,
			success:function(res)
			{
				
			}
		})
		
		$.ajax({
			url:"/nb/wechat/bindUser.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#errorMsg").html(res.errorMsg);
					$("#mask_div").show();
					$("#success_div").show();
				}
				else
				{
					$("#error_msg").html(res.errorMsg);
				}
			}
		})
	}
	
}
var bind_status = false;
function checkTel()
{
		var tel = $("#tel").val();
		
		if(validateTel(tel))
		{
			$.ajax({
				url:"/nb/wechat/userExists.html?tel="+tel,
				type:"POST",
				dataType:"json",
				success:function(res)
				{
					if("success"!=res.result)
					{
						$("#telType").html("注册新用户");
						$("#telType").attr("href","javascript:showRegi()");
						
						$("#telTitle").html("登陆");
						$("#telMsg").html("注册用户，请输入密码进行登录");
						$("#telMsg").attr("color","red");
						
						$("#bind_wechat_div").hide();
						$("#forgetPwd").show();
						
						bind_status = true;
					}
				}
			})
		}
	else
	{
	}
}

/**
 * 忘记密码
 */

function forgetPwd()
{
	location.href = "/nb/wechat/forgetPwd.html?redirectURL="+$("#redirectURL").val();
}
/**
 * 手机号码校验
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile)
{
    if(mobile.length==0)
    {
       error_msg = "请输入手机号码!";
       
       return false;
    }    
    if(mobile.length!=11)
    {
    	error_msg = "请输入有效的手机号码";
    	
        return false;
    }
    
    if(!myreg.test(mobile))
    {
    	error_msg = "请输入有效的手机号码";
    	
        return false;
    }
    if(!myreg2.test(mobile))
    {
    	error_msg = "请输入有效的手机号码";
    	
        return false;
    }
    return true;
}

/**
 * 删除错误信息
 */
function inputFocus()
{
	$("#error_msg").html("");
}

function showMyTel()
{
	$("#telType").html("注册新用户");
	$("#telType").attr("href","javascript:showRegi()");
	
	$("#telTitle").html("登陆");
	$("#telMsg").html("注册用户，请输入密码进行登录");
	$("#telMsg").attr("color","red");
	
	$("#bind_wechat_div").hide();
	$("#forgetPwd").show();
	
	bind_status = true;
}
function showRegi()
{
	bind_status = false;
	$("#tel").val("");
	$("#telType").html("已有账号登陆");
	$("#telType").attr("href","javascript:showMyTel()");
	
	$("#telTitle").html("注册");
	$("#telMsg").html("注册成功后自动与当前微信账号进行绑定");
	$("#telMsg").attr("color","red");
	
	$("#bind_wechat_div").show();
	$("#forgetPwd").hide();
}
/**
 * 关闭协议
 */
function hideAgree()
{
	$("#agreement_one").hide();
	$("#agreement_two").hide();
}
/**
 * 显示协议div框
 * @param element_id
 */
function showAgreement(element_id)
{
	$("#"+element_id).show();
}

function getTrimStr(str)
{
	var a = /^[0-9a-zA-Z]*$/g;
	
	if(""!==str&&null!==str)
	{
		var array = str.split("");
		var new_str = "";
		
		for(var i = 0;i<array.length;i++)
		{
			if(a.test(array[i]))
			{
				new_str += array[i];
			}
		}
		return new_str;
	}
	else
	{
		return "";
	}
}