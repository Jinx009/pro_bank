/**
 * 返回
 */
/*function goBack(){
	location.href = '/cf/wechat/pro/index.action?id=2';
}*/
var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = "";
$(function(){
	if(isWechat()){
		 var localStorage = window.localStorage;
		 var mobile =localStorage.getItem('mobile_');
		 var md5 = localStorage.getItem('md5_');
		 if(null!=mobile&&''!=mobile&&null!=md5&&''!=md5){
			 $.ajax({
				 url:'/cf/checkStatus.action',
				 type:'POST',
				 data:'mobile='+mobile,
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
						 $('#haha').css('display','none');
						 $('#hehe').css('display','block');
					 }
				 }
			 })
		 }else{
				$('#haha').css('display','none');
				 $('#hehe').css('display','block');
		}
	}else{
		$('#haha').css('display','none');
		 $('#hehe').css('display','block');
	}
})

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
    	errorMsg = '密码至少8位!'                                     ;
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
	var pwd = $("#pwd").val();
	var params = 'mobilePhone='+username+'&pwd='+pwd;
	
	if(!validateTel(username)){
		layer.msg(errorMsg, {
	        time: 1000
	    });
	}else if(!validatePwd(pwd)){
		layer.msg(errorMsg, {
	        time: 1000
	    });
	}else{
		$.ajax({
			url:'/cf/doLogin.html',
			type:'POST'                                                       ,
			data:params,
			dataType:'json',
			success:function(res){
				if('success'==res.result){
					var localStorage = window.localStorage;
					localStorage.setItem("mobile_",username);
					localStorage.setItem("md5_",username);
					var url = '/cf/wechat/pro/index.action?id=2';
					var redirectUrl = $('#redirectUrl').val();
					if(''!=redirectUrl&&null!=redirectUrl){
						url = redirectUrl;
					}
					location.href = url;
				}else{
					layer.msg(res.errorMsg, {
				        time: 1500
				    });
				}
			}
		})
	}
}
/**
 * 去注册
 */
function goRegister(){
	var url = '/cf/wechat/register.html';
	var redirectUrl = $('#redirectUrl').val();
	if(''!=redirectUrl&&null!=redirectUrl){
		url = url+'?redirectUrl='+redirectUrl;
	}
	location.href = url;
}

/**
 * 忘记密码
 */
$(function(){
	$(".forgetPwd").click(function(event){
		event.preventDefault();
		goWechatForgetPwd();
	});
})

/**
 * 修改密码
 * 
 */
function goWechatForgetPwd(){
	var url = "/cf/wechat/forget.html";
	var redirectUrl = $("#redirectUrl").val();
	if(""!=redirectUrl&&null!=redirectUrl){
		url = url+"?redirectUrl="+redirectUrl;
	}
	location.href = url;
}

/**
 * 判断是否为微信浏览器
 * @returns {Boolean}
 */
function isWechat(){
	var u = navigator.userAgent, app = navigator.appVersion; 
	if(u.indexOf("MicroMessenger") > -1){
		return true;
	}
	return false;
}