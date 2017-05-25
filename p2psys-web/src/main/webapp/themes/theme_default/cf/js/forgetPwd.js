var flag = false;
/**
 * 执行注册
 */
function goModify(){
	var mobilePhone = $("#username").val();
	var pwd = $("#pwd").val();
	var repwd = $("#repwd").val();
	var code = $("#code").val();
	
	var params = "mobilePhone="+mobilePhone+"&pwd="+pwd+"&code="+code;
	
	if(!validateTel(mobilePhone)){
		$("#register_error").html(errorMsg);
	}else if(!validatePwd(pwd)){
		$("#register_error").html(errorMsg);
	}else if(pwd!=repwd){
		$("#register_error").html("确认密码不一致!");
	}else{
		if(flag){
			$.ajax({
				url:"/cf/forgetpwd.html",
				data:params,
				dataType:"json",
				type:"POST",
				success:function(res){
					if("success"==res.result){
						layer.alert('恭喜您重置密码成功！',{title:false,closeBtn: 0},function(){
							goHomeUrl();
						});
					}else{
						$("#register_error").html(res.errorMsg);
					}
				}
			})
		}
	}
}

/**
 * 发送短信
 */
function sendCode(){
	var mobilePhone = $("#username").val();
	if(!validateTel(mobilePhone)){
		$("#register_error").html(errorMsg);
	}else{
		var params = "mobilePhone="+mobilePhone;
		$.ajax({
			url:"/cf/forgetPwdCode.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					var time=60;
					var timeFun=setInterval(function(){
						time--;
						if(time>0){
							$("#code_text").html(time+"秒后重发").attr("onclick","");
						}
						else{
							time=60;
							$("#code_text").html("获取验证码").attr("onclick","sendCode");
							clearInterval(timeFun);
						}
					},1000)
				}
			}
		})
	}
}

/**
 * 验证账户是否存在
 */
function checkMobilePhone(){
	var mobilePhone = $("#username").val();
	var params = "mobilePhone="+mobilePhone;
	$("#register_error").html("");
	if(validateTel(mobilePhone)){
		$.ajax({
			url:"/cf/userExists.html",
			data:params,
			type:"POST",
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					flag=false;
					$("#register_error").html("该用户不存在,请联系客服!");
				}else{
					flag = true;
				}
			}
		})
	}
}
var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(16[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var errorMsg = "";
/**
 * 手机号码校验
 * @param mobile
 * @returns {Boolean}
 */
function validateTel(mobile){
    if(mobile.length==0){
       errorMsg = "请输入手机号码!";
       return false;
    }    
    if(mobile.length!=11){
    	errorMsg = "请输入有效的手机号码";
        return false;
    }
    if(!myreg.test(mobile)){
    	errorMsg = "请输入有效的手机号码";
        return false;
    }
    if(!myreg.test(mobile)){
    	errorMsg = "请输入有效的手机号码";
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
    	errorMsg = "请输入密码!";
       return false;
    }    
    if(pwd.length<8){
    	errorMsg = "密码至少8位!";
        return false;
    }
    if(!/^(?=.*[a-z])[a-z0-9]+/ig.test(pwd)){
    	errorMsg = "密码必须是字母与数字组合!";
    	return false;
    }
    return true;
}

$(function(){
	$(".backLogin").click(function(event){
		event.preventDefault();
		goHomeUrl();
	});
})

/**
 * 修改密码
 * 
 */
function goHomeUrl(){
	var url = "/cf/user/index.html";
	location.href = url;
}