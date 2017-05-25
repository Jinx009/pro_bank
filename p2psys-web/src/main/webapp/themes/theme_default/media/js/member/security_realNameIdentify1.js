define(function(require,exports,module){
	require('jquery');
	
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#payAccountForm").validate({
				
				rules:{
					realName:{
			    		required: true,
			    		realName:true
			    	},
			    	cardId:{
			    		required: true,
			    		isIdCardNo:true
			    	},
			    	mobilePhone:{
			    		required: true,
			    		isMobile: true,
						remote:{
							type: "get",
							url: "/user/checkMobilePhone.html"+"?random="+(new Date()).getTime(),
							data:{
								mobilePhone: function(){
									return $("#mobilePhone").val();
								}
							}
						}
			    	},
			    	email: {
							required: true,
							email:true,
							remote:{
								type: "get",
								url: "/user/checkEmail.html",
								data:{
									email: function(){
										return $("#customTest").val();
									}
								}
							}	
				   		},
				   	zzjgCode:{
			    		required: true,
			    	},
			    	businessRegistrationNumber:{
			    		required: true,
			    	},
			    },
			    messages:{
			    	realName:{
			    		required: "请输入真实姓名",
			    		
			    		realName: "真实姓名格式不对"
			    	},
			    	cardId:{
			    		required: "请输入身份证号码",
			    		isIdCardNo: "身份证格式不对"
			    	},
			    	mobilePhone:{
			    		required: "请输入手机号码",
			    		isMobile: "手机号码格式不对",
			    		remote:"该手机号码已经存在"
			    	},
			    	 email: {
							required: "请输入正确的email地址",
							email: "请输入正确的email地址",
							remote:"邮箱地址已经存在"
						   },
					zzjgCode:{
			    		required: "请输入组织机构代码",
			    	},
			    	businessRegistrationNumber:{
			    		required: "请输营业执照编号",
			    	},
			   },
			 	errorElement:'em',
			 	showErrors:function(errorMap, errorList){
			 		this.defaultShowErrors();
			 		
			 		if(errorMap.realName){
			 			$(".errorTips b").html(errorMap.realName);
			 		}
			 		else if(errorMap.cardId){
			 			$(".errorTips b").html(errorMap.cardId);
			 		}
			 		else if(errorMap.mobilePhone){
			 			$(".errorTips b").html(errorMap.mobilePhone);
			 		}
			 		else if(errorMap.zzjgCode){
			 			$(".errorTips b").html(errorMap.zzjgCode);
			 		}
			 		else if(errorMap.businessRegistrationNumber){
			 			$(".errorTips b").html(errorMap.businessRegistrationNumber);
			 		}
			 		else if(errorMap.email){
			 			$(".errorTips b").html(errorMap.email);
			 		}
			 		else{
			 			$(".errorTips b").html('请提交您的信息');
			 		}
			 		
			 	},
				errorPlacement:function(error,element){
					//$(".errorTips b").html(error);
			    },
			   submitHandler:function(form){
			    	form.submit();
			    	require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
						//构造确认框DOM
			    		 $.layer({
	                       type: 1,
	                       closeBtn: [0,true],
	                       title: "&nbsp;",
	                       area: ['460px', '194px'],
	                       border: [1, 1, '#cecfd0'],
	                       page: {
	                           html: '<div class="tipsWrap"><dl><dt>认证完成前，请不要关闭本窗口;</dt><dd>认证完成后，请根据您的认证结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">认证成功</a><a href="javascript:;" class="cancleBtn">认证失败</a></div></div>'
	                       }
	                   });
						$(".okBtn").click(function(){
							window.location.reload();
						});
						$(".cancleBtn").click(function(){
							window.location.reload();
						});
				});
			     }  
			});

		})
	})
});