define(function(require,exports,module){
	require('jquery');
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate.min',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			//-S登陆密码修改验证
			$("#modifyPwdForm").validate({
				rules:{
					pwd:{
						required:true,
						regexPassword:true
					},
					newPwd:{
						required:true,
						regexPassword:true
					},
					confirmNewPwd:{
						required:true,
						equalTo: "#password"
					}
				},
				messages:{
					pwd:{
						required:"请输入原密码",
						regexPassword:"原密码格式错误"
					},
					newPwd:{
						required: "至少8到16位英文和数字",
						regexPassword:'新密码格式错误'
					},
					confirmNewPwd: {
						required: "请再输一次新密码",
						equalTo: "两次密码不一致"
					  }
				},
				errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				},
				success:function(element){
					element.parents(".input-row").removeClass("input-error");
				},
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    							if(data.result){
		    							$.layer({
		    							    type: 1,
		    							    closeBtn: [0,true],
		    				                title: "&nbsp;",
		    							    area: ['384px', '186px'],
		    							    border: [1, 1, '#cecfd0'],
		    							    time:3,
		    							    page: {
		    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span >恭喜，您的登录密码修改成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
		    							    },
		    							    close: function(index){
		    							    	window.location.href = "/wx/login.html";
		    							    },
		    							    end: function(){
		    							    	window.location.href = "/wx/login.html";
		    							    },
		    							    success: function(layero){
		    							    	var time =3; 
		    							    	function closeTime () {
		    										time--;
		    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
			    								}
		    							    	setInterval(function(){
	    											closeTime();
	    										}, 1000);
		    							    }
		    							});
		    					}else{
		    						$.layer({
									    type: 1,
									    closeBtn: [0,true],
						                		    title: "&nbsp;",
									    area: ['384px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="/wx/account/modifyPwd.html" class="okBtn failBtn">确定</a></div></div>'
									    },
									    close: function(index){
									    	layer.closeAll();
									    }
									});
		    						$("#modifyPwdForm")[0].reset();
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E登陆密码修改验证
			
			//-S交易密码验证
			$("#modifyPayPwdForm").validate({
				rules:{
                    payPwd:{
						required:true,
						regexPayPassword:true
					},
					newPayPwd:{
						required:true,
						regexPayPassword:true
					},
					confirmNewPayPwd:{
						required:true,
						equalTo: "#pay_password"
					}
				},
				messages:{
                    payPwd:{
						required:"请输入原密码",
						regexPayPassword:"原密码为6位数字"
					},
					newPayPwd:{
						required: "请输入新密码",
						regexPayPassword:'新密码为6位数字'
					},
					confirmNewPayPwd: {
						required: "请再输一次新密码",
						equalTo: "两次密码不一致"
					  }
				},
				errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				},
				success:function(element){
					element.parents(".input-row").removeClass("input-error");
				},
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							$.ajax({
	    					    		url:"/member/cash/checkBank.html",
	    					    		type:"post",
	    					    		success:function(json){
	    					    			var jsonObj = JSON.parse(json); 
	    					    			if(jsonObj.result == false){
	    					    				$.layer({
				    							    type: 1,
				    							    closeBtn: [0,true],
				    				                		    title: "&nbsp;",
				    							    area: ['384px', '186px'],
				    							    border: [1, 1, '#cecfd0'],
				    							    page: {
				    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>恭喜，您的交易密码修改成功！</span></div><div class="tipsBtnBar"><a href="/wx/account/addBank.html" class="okBtn failBtn">立即前往绑卡</a></div></div>'
				    							    },
				    							    close: function(index){
				    							    	window.location.reload();
				    							    },
				    							    end: function(){
				    							    	window.location.reload();
				    							    }
				    							});
				    					    }else{
				    							$.layer({
				    							    type: 1,
				    							    closeBtn: [0,true],
				    				                title: "&nbsp;",
				    							    area: ['384px', '186px'],
				    							    border: [1, 1, '#cecfd0'],
				    							    time:3,
				    							    page: {
				    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>恭喜，您的交易密码修改成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
				    							    },
				    							    close: function(index){
				    							    	window.location.href = "/wx/account/setting.html";
				    							    },
				    							    end: function(){
				    							    	window.location.href = "/wx/account/setting.html";
				    							    },
				    							    success: function(layero){
				    							    	var time =3; 
				    							    	function closeTime () {
				    										time--;
				    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
					    								}
				    							    	setInterval(function(){
			    											closeTime();
			    										}, 1000);
				    							    }
				    							});
	    									}	
	    					    		}
	    					    	});
		    					}else{
		    						if(data.lock) {
		    							$.layer({
		    							    type: 1,
		    							    closeBtn: [0,true],
		    				                		    title: "&nbsp;",
		    							    area: ['384px', '186px'],
		    							    border: [1, 1, '#cecfd0'],
		    							    time:3,
		    							    page: {
		    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>支付密码已锁定，请24小时后重试，如有疑问请与客服联系，400-6366-800！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
		    							    },
		    							    close: function(index){
		    							    	window.location.reload();
		    							    },
		    							    end: function(){
		    							    	window.location.reload();
		    							    },
		    							    success: function(layero){
		    							    	var time =3; 
		    							    	function closeTime () {
		    										time--;
		    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
			    								}
		    							    	setInterval(function() {
	    											closeTime();
	    										}, 1000);
		    							    }
		    							});
		    						}else {
			    						$.layer({
										    type: 1,
										    closeBtn: [0,true],
							                title: "&nbsp;",
										    area: ['384px', '186px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="/wx/account/modifyPaypwd.html" class="okBtn failBtn">确定</a></div></div>'
										    },
										    close: function(index){
										    	layer.closeAll();
										    }
										});
			    						$("#modifyPayPwdForm")[0].reset();
			    					}
		    					}
	    					})
		    			});
	    			});
		        } 
			})
		})
	})

})
