function doSet(){
	var pwd = $('#pwd').val();
	var rePwd = $('#rePwd').val();
	var result = validatePayPwd(pwd);
	var params = 'pwd='+pwd;
	if('ok'!=result){
		alert(result);
	}else{
		$.ajax({
			url:'/cf/wechat/user/setPayPwd.action',
			type:'POST',
			data:params,
			dataType:'json',
			success:function(res){
				if(200==res.code){
					layer.msg('设置成功！',{
				        time: 2500
				    });
					var url = $('#url').val();
					 setTimeout(function(){
                    	 location.href=url;
                     },2500);
				}
			}
		})
	}
}
