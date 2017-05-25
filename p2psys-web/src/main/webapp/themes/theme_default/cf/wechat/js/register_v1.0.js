/**
 * 返回
 */
function goBack(){
	location.href = '/cf/wechat/login.html';
}
var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = '';
/**
 * 手机号码校验
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile){
    if(mobile.length==0){
       layer.msg("请输入手机号码!"); 
       return false;
    }    
    if(mobile.length!=11){
    	layer.msg("请输入有效的手机号码"); 
        return false;
    }
    if(!myreg.test(mobile)){
    	layer.msg("请输入有效的手机号码"); 
        return false;
    }
    if(!myreg.test(mobile)){
    	layer.msg("请输入有效的手机号码"); 
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
    	layer.msg("请输入密码!"); 
       return false;
    }    
    if(pwd.length<8){
    	layer.msg("密码至少8位!"); 
        return false;
    }
    if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd)){
    	layer.msg("密码必须是字母与数字组合!"); 
    	return false;
    }
    return true;
}
 /**
 *注册方法
 */
function doRegister(){
	var userName = $('#userName').val();
	var pwd = $('#pwd').val();
	var rePwd = $('#rePwd').val();
	var code = $('#code').val();
	
	var params = 'mobilePhone='+userName+'&pwd='+pwd+'&code='+code;
	
	if(!validateTel(userName)){
		$('#errorMsg').html(errorMsg);
	}else if(!validatePwd(pwd)){
		$('#errorMsg').html(errorMsg);
	}
	else if(rePwd!=pwd){
		layer.msg("两次密码不一致！"); 
	}else{
		$.ajax({
			url:'/cf/wechat/doRegister.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					var url = '/cf/wechat/pro/index.action?id=2';
					var redirectUrl = $('#redirectUrl').val();
					if(''!=redirectUrl&&null!=redirectUrl){
						url = redirectUrl;
					}
					location.href = url;
				}else{
					$('#errorMsg').html(res.data);
				}
			}
		})
	}
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
			url:'/cf/registerCode.html',
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
							$('#code_text').html('获取验证码').attr('onclick','sendCode()');
							clearInterval(timeFun);
						}
					},1000)
				}
			}
		})
	}
}