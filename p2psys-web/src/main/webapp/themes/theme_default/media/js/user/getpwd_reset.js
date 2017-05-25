define(function(require,exports,module){
	require('jquery');
	
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#emailgetForm").validate({
				 rules:{
					 pwd:{
						required: true,
						regexPassword:true
					   },
					 confirmNewPwd: {
						required: true,
						equalTo: "#password"
					   }

				 },
				 messages:{
					 pwd: {
						required: "请输入长度为8到16位的密码，且至少包含英文和数字",
						regexPassword:"密码格式错误"
					  },
					 confirmNewPwd: {
						required: "请输入确认密码",
						equalTo: "两次输入密码不一致"
					 }
				 },
				 showErrors: function(errorMap, errorList) {
				   	this.defaultShowErrors();
				   	$(".normalTips").css("display","none");
				   	$(".errorTips").css("display","none");
				   	if(errorMap.pwd){
				   		$(".errorTips").css("display","block");
				   		$(".errorTips b").html(errorMap.pwd);
				   	}else if(errorMap.confirmNewPwd){
				   		$(".errorTips").css("display","block");
				   		$(".errorTips b").html(errorMap.confirmNewPwd);
				   	}
				   	else{
				   		$(".errorTips").css("display","none");
				   	}
				   },
				errorPlacement:function(error,element){

				},
				submitHandle:function(form){
					form.submit();
				}
				
			})
		})
	})
		
	
})

