define(function(require,exports,modlue){
	var logined_name,logined_h_name,logined_img,login_before,logined,status_txt,status_txt,J_indexlogin_header;
	logined_name = $(".logined_name");
	logined_img = $(".logined_img");
	logined = $(".logined");
	login_gif = $(".login_gif");
	login_before = $(".login_before");
	head_info_name = $("#status_webname");
	head_info_status = $("#status_txt");
	logined_h_name = $(".J_info_b");
	J_indexlogin_header = $(".J_indexlogin_header");
	if(session_status==true)
	{
		loginGif_after();
		logined_name.text(fm_login_name);
		logined_img.attr("src",fm_login_img);	
	}else{
		loginGif_before();
	}
	
	function loginGif_after(){
		login_gif.remove();
		login_before.remove();
		logined.show();	
	}
	
	function loginGif_before(){
		logined.hide();
		setTimeout(function(){
			//login_gif.remove();
			login_before.show();	
		},100);
	}
	

	
	$('#loginForm').ajaxForm({
		beforeSend:function(){
			var user = $("#username");
    		var pwd = $("#password");
    		var valicode = $("input[name=valicode]");
    		var pwdVal = pwd.val();
    		var userVal = user.val();
    		var valicodeVal = valicode.val();
    		var jsonmes = $(".js_loginmes");
    		
    		if(userVal!=""&&pwdVal!=""&&valicodeVal!="")
    		{
    			pwd.removeClass("error");
    			user.removeClass("error");
    			return true
    		}else{
    			if(userVal=="")
    			{
        			jsonmes.text("用户名不能为空");
    				user.addClass("error");
    				return false
    			}else{
    				jsonmes.text("");
    				user.removeClass("error");
    			}
        		if(pwdVal=="")
        		{
        			jsonmes.text("密码不能为空");
        			pwd.addClass("error");
        			return false
        		}
        		else{
        			jsonmes.text("");
        			pwd.removeClass("error");
    			}
        		if(valicodeVal=="")
    			{
        			jsonmes.text("验证码不能为空");
    				valicode.addClass("error");
    				return false
    			}else{
    				jsonmes.text("");
    				valicode.removeClass("error");
    			}
        		
    		}
		},
		success:function(data){
				var formData = data;
				var nameVal = formData.userName;
				var imgVal = formData.imgurl;
				var resultVal = formData.result;
				var resultMes = formData.msg||"";
				if(resultVal==true){
					loginGif_after();
					logined_name.text(nameVal);
					logined_h_name.text(nameVal);
					logined_img.attr("src",imgVal);	
					head_info_name.text(nameVal);
					J_indexlogin_header.show().siblings(".float_right").hide();
					head_info_status.text("退出").attr("href",webroot+"/user/logout.html");
				}
				else{
					
					$(".js_loginmes").text(resultMes);
					$(".valicode_img").each(function(){
						$(this).attr("src",'/validimg.html?t=' + Math.random());
					})
				}
			}
	}); //首页登录显示
	
	
	
	
});