var myreg = /^[0-9\ ]+$/;
var error_msg = "";

/**
 * 执行设置
 */
function doSet() {
	var pwd = $("#payPwd").val();
	var rePwd = $("#rePayPwd").val();

	var params = "pwd="+pwd+"&rePwd="+rePwd;
	if(!validatePayPwd(pwd,rePwd)){
		$("#errorMsg").html(error_msg);
	}else{
		$.ajax({
			url:"/cf/set/pay-pwd.html",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(res){
				if("success"==res.result){
					layer.confirm('设置交易密码成功！', {
					    btn: ['返回上一页','确定'],title:false,closeBtn: 0 
					}, function(){
						goBack();
					},function(){
						goBack();
					});
				}else{
					layer.alert(res.errorMsg,{title:false,closeBtn: 0});
				}
			}
		})
	}
}


/**
 * 返回上一页
 */
function goBack(){
	var redirectUrl = $("#redirectUrl").val();
	
	if(null!=redirectUrl&&""!=redirectUrl){
		location.href = redirectUrl;
	}else{
		layer.closeAll();
	}
}

/**
 * 校验
 * 
 * @param pwd
 * @returns {Boolean}
 */
function validatePayPwd(pwd,rePwd) {
	if(null==pwd||""==pwd){
		error_msg = "交易密码不能为空!";
		return false;
	}
	if (pwd.length == 0) {
		error_msg = "请输入交易密码!";
		return false;
	}
	if (pwd.length != 6) {
		error_msg = "请输入6位有效数字!";
		return false;
	}
	if (!myreg.test(pwd)) {
		error_msg = "请输入6位有效数字!";
		return false;
	}
	if (pwd!=rePwd) {
		error_msg = "两次密码不一致!";
		return false;
	}

	return true;
}
