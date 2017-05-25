var error_msg = "";
var myreg = /^[0-9\ ]+$/;
/**
 * 设置成功跳转
 */
function successUrl()
{
	location.href = $("#redirectURL").val();
}
/**
 * 提交信息
 */
function settingPayPwd()
{
	var payPwd = $("#payPwd").val();
	var rePayPwd = $("#rePayPwd").val();
	var params = "payPwd="+payPwd+"&rePayPwd="+rePayPwd;
	
	if(validatePayPwd(payPwd))
	{
		$.ajax({
			url:"/nb/wechat/account/settingPayPwd.html",
			data:params,
			type:"POST",
			dataType:"json",
			success:function(res)
			{
				if("success"==res.result)
				{
					$("#mask_div").show();
					$("#success_div").show();
					
					successUrl();
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
		$("#error_msg").html(error_msg);
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
       error_msg = "请输入交易密码!";
       
       return false;
    }    
    if(pwd.length!=6)
    {
    	error_msg = "请输入6位有效数字作为交易密码";
    	
        return false;
    }
    
    if(!myreg.test(pwd))
    {
    	error_msg = "请输入有效的数字";
    	
        return false;
    }
    if(pwd!==$("#rePayPwd").val())
    {
    	error_msg = "两次密码不一致!";
    	
        return false;
    }
    
    return true;
}