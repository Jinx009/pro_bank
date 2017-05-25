var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = "";
/**
 * 手机号码校验
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile){
    if(mobile.length==0){
       errorMsg = '请输入手机号码!';
       return false;
    }    
    if(mobile.length!=11){
    	errorMsg = '请输入有效的手机号码';
        return false;
    }
    if(!myreg.test(mobile)){
    	errorMsg = '请输入有效的手机号码';
        return false;
    }
    if(!myreg.test(mobile)){
    	errorMsg = '请输入有效的手机号码';
        return false;
    }
    return true;
}
 /**
  * 校验
  * @param pwd
  * @returns {Boolean}
  */
function validatePwd(pwd){
    if(pwd.length==0){
    	errorMsg = '请输入新密码!';
       return false;
    }    
    if(pwd.length<8){
    	errorMsg = '密码至少8位!';
        return false;
    }
    if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd)){
    	errorMsg = '密码必须是字母与数字组合!';
    	return false;
    }
    return true;
}
 /**
 *登录方法
 */
function doLogin(){
	var username = $('#username').val();
	var pwd = $('#pwd').val();
	var params = 'mobilePhone='+username+'&pwd='+pwd;
	
	if(!validateTel(username)){
		$('#errorMsg').html(errorMsg);
	}else if(!validatePwd(pwd)){
		$('#errorMsg').html(errorMsg);
	}else{
		$.ajax({
			url:'/cf/doLogin.html',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if('success'==res.result){
					var url = '/pro/list.html?id=2';
					var redirectUrl = $('#redirectUrl').val();
					if(''!=redirectUrl&&null!=redirectUrl){
						url = redirectUrl;
					}
					location.href = url;
				}else{
					$('#errorMsg').html(res.errorMsg);
				}
			}
		})
	}
}
/**
 * 去注册
 */
function goRegister(){
	var url = '/cf/register.html';
	var redirectUrl = $('#redirectUrl').val();
	if(''!=redirectUrl&&null!=redirectUrl){
		url = url+'?redirectUrl='+redirectUrl;
	}
	location.href = url;
}

$(function(){
	$('.forgetPwd').click(function(event){
		event.preventDefault();
		goForgetPwd();
	});
})

/**
 * 修改密码
 * 
 */
function goForgetPwd(){
	var url = '/cf/forget.html';
	var redirectUrl = $('#redirectUrl').val();
	if(''!=redirectUrl&&null!=redirectUrl){
		url = url+'?redirectUrl='+redirectUrl;
	}
	location.href = url;
}

