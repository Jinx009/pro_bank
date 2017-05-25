var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 

/**
 * 显示协议div框
 * @param element_id
 */
function showAgreement(element_id)
{
	$("#"+element_id).show();
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
    
    return true;
}
/**
 * 执行注册
 */
function executeRegister()
{
	var tel = $("#tel").val();
	var pwd = $("#pwd").val();
	var repwd = $("#repwd").val();
	var valid_code = $("#valid_code").val();
	var tel_code = $("#tel_code").val();
	var invite_code = getTrimStr($("#invite_code").val());
	
	var params = "tel="+tel+"&pwd="+pwd+"&repwd="+repwd+"&valid_code="+valid_code+"&tel_code="+tel_code+"&invite_code="+invite_code;
	
	if(!validateTel(tel))
	{
		$("#tel_error").val("号码不合法");
		
		return;
	}
	if(pwd!=repwd)
	{
		$("#repwd_error").val("密码不一致");
		
		return;
	}
	if(!$("#agree").is(":checked")) 
	{
		$("#error_msg").html("请先同意注册协议!");
		
		return;
	}
	else
	{
		$.ajax({
			url:"/nb/wechat/executeRegister.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#mask_div").show();
					$("#success_div").show();
				}
				else
				{
					if(res.errorMsg)
					{
						$("#error_msg").html(res.errorMsg);
					}
				}
			}
		})
	}
}
/**
 * 校验手机号是否存在
 */
function validTel()
{
	
	var tel = $("#tel").val(); 
	
	if(validateTel(tel))
	{
		var params = "tel="+tel;
		
		$.ajax({
			url:"/nb/wechat/userExists.html",
			type:"POST",
			data:params,
			ansyc:false,
			dataType:"json",
			success:function(res)
			{
				if("failure"===res.result)
				{
					$("#tel_error").val(res.errorMsg);
				}
				else
				{
					$("#tel_error").val("");
				}
			}
		})
	}
}
/**
 * 获取手机验证码
 */
function getRegisterCode()
{
	var tel = $("#tel").val();
	
	if(validateTel(tel)&&""==$("#tel_error").val())
	{
		var params = "tel="+tel;
		
		$.ajax({
			url:"/nb/wechat/getRegisterCode.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
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
		})
	}
	else
	{
		if("号码已存在"!=$("#tel_error").val())
		{
			$("#tel_error").val("号码不合法");
		}
	}
}
/**
 * 确认密码验证
 */
function validRepwd()
{
	var pwd = $("#pwd").val();
	var repwd = $("#repwd").val();
	
	if(pwd!=repwd&&repwd.length>=pwd.length)
	{
		$("#repwd_error").val("密码不一致");
	}
	else
	{
		$("#repwd_error").val("");
	}
}
/**
 * 注册成功跳转
 */
function successUrl()
{
	var url = $("#redirectURL").val();
	
	if(""!==url)
	{
		location.href = url;
	}
	else
	{
		location.href = "/nb/wechat/product/index.html";
	}
}
/**
 * 关闭协议
 */
function hideAgree()
{
	$("#agreement_one").hide();
	$("#agreement_two").hide();
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