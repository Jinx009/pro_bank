var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = '';
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
    	errorMsg = '请输入密码!';
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
 * 去登录
 */
function goLogin(){
	var url = '/cf/wechat/login.html';
	var redirectUrl = $('#redirectUrl').val();
	if(''!=redirectUrl&&null!=redirectUrl){
		url = url+'?redirectUrl='+redirectUrl;
	}
	location.href = url;
}

/**
 * 发送短信
 */
function sendCode(){
	var mobilePhone = $('#userName').val();
	if(!validateTel(mobilePhone)){
		$('#errorMsg').html(errorMsg);
	}else{
		var params = 'mobilePhone='+mobilePhone;
		$.ajax({
			url:'/cf/forgetPwdCode.html',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if('success'==res.result){
					var time=60;
					var timeFun=setInterval(function(){
						time--;
						if(time>0){
							$('#code_text').html(time+'秒后重发').attr('onclick','');
						}
						else{
							time=60;
							$('#code_text').html('验证码').attr('onclick','sendCode()');
							clearInterval(timeFun);
						}
					},500)
				}
			}
		})
	}
}

/**
 * 输入校验
 */
function checkPhone(){
	var userName = $('#userName').val();
	if(validateTel(userName)){
		$.ajax({
			url:'/cf/userExists.action?mobilePhone='+userName,
			type:'GET',
			dataType:'json',
			success:function(res){
				if('success'==res.result){
					layer.msg('该手机还未注册成为平台用户,请先注册',{
				        time: 3500
				    });
					 setTimeout(function(){
                    	 location.href="/cf/wechat/register.html";
                     },2500);
				}
			}
		})
	}
}

/**
 * 忘记密码
 */
function doModify(){
	var userName = $('#userName').val();
	var pwd = $('#pwd').val();
	var rePwd = $('#rePwd').val();
	var code = $('#code').val();
	
	var params = 'mobilePhone='+userName+'&pwd='+pwd+'&code='+code;
	if(!validateTel(userName)){
		$('#errorMsg').html(errorMsg);
	}else if(!validatePwd(pwd)){
		$('#errorMsg').html(errorMsg);
	}else if(rePwd!=pwd){
		$('#errorMsg').html('两次密码不一致！');
	}else if(null==code||''==code){
		$('#errorMsg').html('验证码不能为空！');
	}else{
		$.ajax({
			url:'/cf/forgetpwd.html',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if("success"==res.result){
					var url = '/cf/wechat/login.html';
					var redirectUrl = $('#redirectUrl').val();
					if(''!=redirectUrl&&null!=redirectUrl){
						url = redirectUrl;
					}
					layer.alert('恭喜您重置密码成功，请重新登录！',{title:false,closeBtn:0},function(){
						
						var localStorage = window.localStorage;
						localStorage.setItem("mobile_",'');
						localStorage.setItem("md5_",'');
						location.href = url;
					})
				}else{
					$('#errorMsg').html(res.errorMsg);
				}
			}
		})
	}
}

