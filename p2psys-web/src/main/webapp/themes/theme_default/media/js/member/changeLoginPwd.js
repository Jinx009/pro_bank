define(function(require,exports,module){
	require('jquery');
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
	require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
		$("#changeLoginPwd_form").validate({
			rules:{
				pwd:{
					required:true,
					regexPassword:true
				},
				confirmPassword:{
					required:true,
					equalTo: "#password"
				}
			},
			messages:{
				pwd:{
					required: "至少8到16位英文和数字",
					regexPassword:"新密码格式错误"
				},
				confirmPassword: {
					required: "请再输一次新密码",
					equalTo: "两次密码不一致"
				}
			},
			errorElement:'em',
			errorPlacement:function(error,element){
				element.next().html(error);
		    },
		    success:function(label){
		    }, 
		    submitHandler:function(form){
		    	require.async('jquery.form',function(){
		    		$(form).ajaxSubmit(function(data){
    					if(!data.result)
    					{
    						$("#changeLoginPwd_form ul li:eq(0) span").html('<em class="error">'+data.msg+'</em>');
    					}
    					else
    					{
    						$("#changeLoginPwd_form ul li:eq(2) span").html('<em class="error">登录密码初始化成功，请重新登录！</em>');
    						setTimeout(function(){
    							window.location.href = "/user/login.html";
    						},2000);
    					}
	    			});
		    	});
	        }
	    });
	//密码强弱判断
	$("#password").keyup(function(){
		var strongRegex = new RegExp("^(?=.{8,})(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W).*$", "g"); 
		var mediumRegex = new RegExp("^(?=.{8,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g"); 
		var enoughRegex = new RegExp("(?=.{8,}).*", "g"); 
		var $pw = $(".passwordStrength");
		if (false == enoughRegex.test($(this).val())) { 
			$pw.removeClass('level1 level2 level3').addClass('level0'); 
			//密码小于八位的时候，密码强度图片都为灰色 
		} 
		else if (strongRegex.test($(this).val())) {
			$pw.removeClass('level1 level2 level3').addClass('level3');
			//密码为八位及以上并且字母数字特殊字符三项都包括,强度最强 
		} 
		else if (mediumRegex.test($(this).val())) { 
			$pw.removeClass('level1 level2 level3').addClass('level2');
			//密码为八位及以上并且字母、数字、特殊字符三项中有两项，强度是中等 
		} 
		else { 
			$pw.removeClass('level1 level2 level3').addClass('level1');
			//如果密码为8为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的 
		} 
		return true; 
	});
		});
	});
});