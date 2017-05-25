var error_msg = "";
var myreg = /^[0-9\ ]+$/;
function changePayPwd()
{
	var pwd = $("#pwd").val();
	var newpwd = $("#newpwd").val();
	var renewpwd = $("#renewpwd").val();
	
	if(!validatePayPwd(pwd))
	{
		wechatAlert(error_msg,"0","");
		
		return;
	}
	if(!validateNewPayPwd(newpwd))
	{
		wechatAlert(error_msg,"0","");
		
		return;
	}
	else
	{
		var params = "pwd="+pwd+"&newpwd="+newpwd;
		
		$.ajax({
			url:"/nb/wechat/account/doChangePayPwd.html",
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
function validatePayPwd(pwd)
{
    if(pwd.length==0)
    {
       error_msg = "请输入原始交易密码!";
       
       return false;
    }    
    if(pwd.length!=6)
    {
    	error_msg = "请输入6位有效数字交易密码";
    	
        return false;
    }
    
    if(!myreg.test(pwd))
    {
    	error_msg = "原始交易密码必须为有效的数字";
    	
        return false;
    }
    
    return true;
}
/**
 * 校验
 * @param pwd
 * @returns {Boolean}
 */
function validateNewPayPwd(pwd)
{
    if(pwd.length==0)
    {
       error_msg = "请输入新交易密码!";
       
       return false;
    }    
    if(pwd.length!=6)
    {
    	error_msg = "请输入6位有效数字作为新交易密码";
    	
        return false;
    }
    
    if(!myreg.test(pwd))
    {
    	error_msg = "新交易密码必须为有效的数字";
    	
        return false;
    }
    if($("#newpwd").val()!==$("#renewpwd").val())
    {
    	error_msg = "确认密码不一致!";
    	
        return false;
    }
    
    return true;
}