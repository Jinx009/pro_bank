var error_msg = "";
function changePwd()
{
	var pwd = $("#pwd").val();
	var newpwd = $("#newpwd").val();
	var renewpwd = $("#renewpwd").val();
	
	if(!validatePwd(pwd))
	{
		wechatAlert(error_msg,"0","");
		
		return;
	}
	if(!validateNewPwd(newpwd))
	{
		wechatAlert(error_msg,"0","");
		
		return;
	}
	else
	{
		var params = "pwd="+pwd+"&repwd="+newpwd;
		
		$.ajax({
			url:"/nb/wechat/account/doChangePwd.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res)
			{
				if("success"===res.result)
				{
					wechatAlert(res.errorMsg,"1","/nb/wechat/account/setting.html");
				}
				else
				{
					wechatAlert(res.errorMsg,"0","");
				}
			}
		})
	}
}
/**
 * 校验
 * @param pwd
 * @returns {Boolean}
 */
function validateNewPwd(pwd)
{
    if(pwd.length==0)
    {
       error_msg = "请输入新密码!";
       
       return false;
    }    
    if(pwd.length<8)
    {
    	error_msg = "新密码至少8位!";
    	
        return false;
    }
    if($("#newpwd").val()!==$("#renewpwd").val())
    {
    	error_msg = "确认密码不一致!";
    	
        return false;
    }
    if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd))
    {
    	error_msg = "密码必须是字母与数字组合!";
    	
    	return false;
    }
    
    return true;
}
/**
 * 校验
 * @param pwd
 * @returns {Boolean}
 */
function validatePwd(pwd)
{
    if(pwd.length==0)
    {
       error_msg = "请输入原始密码!";
       
       return false;
    }    
    if(pwd.length<8)
    {
    	error_msg = "原始密码至少8位!";
    	
        return false;
    }
    
    return true;
}