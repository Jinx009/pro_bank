function postData(){
	var pwd = $('#pwd').val();
	var newPwd = $('#newPwd').val();
	var rePwd = $('#rePwd').val();
	var result = validatePwd(newPwd);
	
	if('ok'!=result){
		$('.error-tip').html(result);
	}else if(null==pwd&&''==pwd){
		$('.error-tip').html('原始密码未输入！');
	}else if(newPwd!=rePwd){
		$('.error-tip').html('确认密码不一致！');
	}else{
		var params = 'pwd='+pwd+'&newPwd='+newPwd+'&rePwd='+rePwd;
		
		$.ajax({
			url:'/cf/wechat/changePwd.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					layer.alert('修改成功！',{title:false,closeBtn: 0},function(){
						location.href = '/cf/wechat/user/index.action';
					});
				}else{
					$('.error-tip').html(res.data);
				}
			}
		})
	}
	
}