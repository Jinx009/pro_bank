define(function(require,exports,module){
	require('jquery');
	
	$("#refreshCode").click(function(){
		$(".valicode_img").trigger("click");
	})
	
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#emailgetForm").validate({
				 ignore:".ignore",
				 rules:{
					 email:{
						 email:true,
						 required:true
					 },
					 valicode:{
					 	required:true,
					 	minlength:4
					 }
				 },
				 messages:{
					 email:{
						 required:"请输入正确的email地址",
						 email:"请输入正确的email地址"
					 },
					 valicode:{
					 	required:"请输入验证码",
					 	minlength:"验证码格式错误"
					 }
				 },
				 showErrors: function(errorMap, errorList) {
				   	this.defaultShowErrors();
				   	$(".errorTips").css("display","none");
				   	if(errorMap.email){
				   		$(".errorTips").css("display","block");
				   		$(".errorTips b").html(errorMap.email);
				   	}else if(errorMap.valicode){
				   		$(".errorTips").css("display","block");
				   		$(".errorTips b").html(errorMap.valicode);
				   	}
				   	else{
				   		$(".errorTips").css("display","none");
				   	}
				   },
				   errorPlacement:function(error,element){
				   },
				   submitHandler:function(form){
					   	require.async("jquery.form",function(){
					   		$(form).ajaxSubmit(function(data){
					   			if(data.result == false)
					   			{
					   				$(".errorTips").css("display","block");
					   				$(".errorTips b").html(data.msg);
					   				$(".code").val('');
					   				$(".valicode_img").trigger("click");
					   			}
					   			else{
					   				window.location.href = "/user/getpwd_email2.html?email="+$("input[name='email']").val();
					   			}
					   		});	
					   	})
				   }
			})
		})
	})
		
	
})

