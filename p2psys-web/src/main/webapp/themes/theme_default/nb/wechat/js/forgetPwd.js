var myreg = /^[0-9\ ]+$/;
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
 * 发送手机验证码
 */
function sendMsg()
{
	var tel = $("#tel").val();
	
	if(validateTel(tel))
	{
		$.ajax({
			url:"/nb/wechat/getPwdCode.html",
			data:"tel="+tel,
			type:"POST",
			dataType:"json",
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
				else
				{
					$("#error_msg").html(res.errorMsg);
				}
			}
		})
	}
	else
	{
		$("#error_msg").html("手机号码格式错误!");
	}
}
/**
 * 提交修改密码
 */
function changePwd()
{
	var tel = $("#tel").val();
	var pwd = $("#pwd").val();
	var repwd = $("#repwd").val();
	var tel_code = $("#tel_code").val();
	
	var params = "tel="+tel+"&pwd="+pwd+"&tel_code="+tel_code;
	
	if(pwd!=repwd)
	{
		$("#error_msg").html("两次密码不一致!");
	}
	if(""==$("#tel_code").val())
	{
		$("#error_msg").html("手机验证码不能为空!");
	}
	else
	{
		$.ajax({
			url:"/nb/wechat/getPwd.html",
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
					$("#error_msg").html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 重置成功
 */
function successUrl()
{
	var url = $("#redirectURL").val();
	
	if(""!=url)
	{
		location.href = url;
	}
	else
	{
		location.href = "/wechat/account/main.html";
	}
	
}