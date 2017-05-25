define(function(require,exports,module){
	require('jquery');
	
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		require.async("/plugins/jquery-validation-1.13.0/additional-methods",function(){
			$("#J_cash").validate({
				rules: {
					code: {
						required: true
					}
				},
				messages:{
					code:{
						required:"请输入验证码",
					}
				},
				errorPlacement:function(error, element){
//				  	element.parents(".input-row").addClass("border").find(".input-error-tip").html(error);	
				  	$("#code").addClass("border");
				},
				success:function(element){
//					element.parents(".input-row").removeClass("border");
					$("#code").removeClass("border");
				},
				submitHandler:function(form){
					$("#cashBtn").attr({"disabled":"disabled"});
					document.form1.submit();
						
			    }
		});
		});
	});
	

});