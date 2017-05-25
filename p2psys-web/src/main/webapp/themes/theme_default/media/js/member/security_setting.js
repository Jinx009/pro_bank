define(function(require,exports,module){
	require('jquery');
	//按钮倒计时
	var wait = 60;
	get_code_time = function (o) {
		if (wait == 0) {
			o.removeAttribute("disabled");
			o.value = "重新发送";
			wait = 60;
		} else {
			o.setAttribute("disabled", true);
			o.value = "(" + wait + ")秒后重新获取";
			wait--;
			setTimeout(function() {
				get_code_time(o);
			}, 1000)
		}
	}
	//-S-密保问题初始化
	function questionFun(){
		var option1,option2,option3,option4,option5,option6,option7;
		option1 = '<option value="您母亲的姓名是？" data-value="_string">您母亲的姓名是？</option>'
		option2 = '<option value="您父亲的姓名是？" data-value="_string">您父亲的姓名是？</option>'
		option3 = '<option value="您配偶的姓名是？" data-value="_string">您配偶的姓名是？</option>'
		option4 = '<option value="您的出生地是？" data-value="_string">您的出生地是？</option>'
		option5 = '<option value="您父亲的生日是？" data-value="_number">您父亲的生日是？</option>'
		option6 = '<option value="您母亲的生日是？" data-value="_number">您母亲的生日是？</option>'
		option7 = '<option value="您配偶的生日是？" data-value="_number">您配偶的生日是？</option>';
		
		var options = option1+option2+option3+option4+option5+option6+option7
		
		var objBox = $("select");
		var valued = [],itemVal = "";
		var itemOption,optionVal;
		objBox.each(function(){
			$(this).append(options);
		});
		objBox.bind("change" ,function(){
			var that = $(this)
			valued.push(that.val());//获取到所有select的value
			if(valued.length>3){
				valued.shift();
			}
			itemVal = valued.join("？")
			itemOption = $("option",objBox);
			itemOption.each(function(i){
				optionVal = $(itemOption[i]).attr("value");
				if(itemVal.match(optionVal)){
					$(itemOption[i]).hide();
				}else{
					$(itemOption[i]).show();
				}
			})
		})
	}
	questionFun();
	//-E-密保问题初始化
	
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate.min',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			//-S 实名认证
			$("#modifyVerificationForm").validate({
				rules:{
					realName:{
						required:true
					},
					cardId:{
						required:true,
						isIdCardNo:true,
						remote:{
							type: "get",
							url: "/user/checkCardId.html",
							data:{
								cardId: function(){
									return $("#cardId").val();
								}
							}
						}
					},
					cardPositive:{
						required:true
					},
					cardOpposite:{
						required:true
					}
				},
				messages:{
					realName:{
						required:"请输入真实姓名"
					},
					cardId:{
						required:"请输入身份证号码",
						isIdCardNo:"请正确输入您的身份证号码",
						remote:"身份证号码已存在"
					},
					cardPositive:{
						required: "请上传身份证正面照"
					},
					cardOpposite: {
						required: "请上传身份证反面照"
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
	    				$("#modifyVerificationForm").ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							layer.msg("实名认证申请成功，等待系统审核", 2, -1);
	    							window.location.reload();	
		    					}else{
		    						layer.msg(data.msg, 2, -1);
		    						$("#modifyVerificationForm")[0].reset();
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E 实名认证
			
			//-S 邮箱获取验证码
				$("#mailValidata").validate({
				rules:{
					code:{
						required:true
					}
				},
				messages:{
					code:{
						required: "请输入验证码"
					}
				},
				errorElement:'em',
				errorPlacement:function(error,element){
					if(element.hasClass("setting_vtxt")){
						element.next().next().html(error);
					}else{
						element.next().html(error);
					}
			    },
			    success:function(label){
			    },  
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit({
	    					dataType:'json',
	    					success:function(data){
	    						if(data.result){
	    							$("#modifyEmailForm").show();
	    							$("#mailValidata").hide();
		    					}else{
		    						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
		    							$.layer({
										    type: 1,
										    closeBtn: [0,true],
							               		    title: "&nbsp;",
										    area: ['384px', '186px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
										    },
										    close: function(index){
										    	layer.closeAll();
										    	$("#mailValidata")[0].reset();
										    }
										});
										$(".failBtn").click(function(){
											layer.closeAll();
											$("#mailValidata")[0].reset();
										});
		    						})
		    					}
	    					}
	    				});
	    			});
		       	      } 
			})
			//-E 邮箱获取验证码
			
			
			//-S 修改邮箱
		$("#modifyEmailForm").validate({
				rules:{
					email:{
						required:true,
						email:true,
						remote:{
							type: "get",
							url: "/user/checkEmail.html",
							data:{
								email: function(){
									return $("#email").val();
								}
							}
						}
					}
				},
				messages:{
					email:{
						required:"请输入新邮箱",
						email:"邮箱格式有误,请输入正确邮箱",
						remote:"邮箱地址已经存在"
					}
				},
				errorElement:'em',
				errorPlacement:function(error,element){
					if(element.hasClass("setting_vtxt")){
						element.next().next().html(error);
					}else{
						element.next().html(error);
					}
			    },
			    success:function(label){
			    },  
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit({
	    					dataType:'json',
	    					success:function(data){
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
			    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>邮箱绑定成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
			    					}else{
			    						$.layer({
										    type: 1,
										    closeBtn: [0,true],
							                		    title: "&nbsp;",
										    area: ['384px', '186px'],
										    border: [1, 1, '#cecfd0'],
										    page: {
										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
										    },
										    close: function(index){
										    	layer.closeAll();
										    	$("#modifyEmailForm")[0].reset();
										    }
										});
										$(".failBtn").click(function(){
											layer.closeAll();
											$("#modifyEmailForm")[0].reset();
										});
			    					}
		    					})
	    					}
	    				});
	    			});
		        } 
			})
			//-E 修改邮箱
			
			
			//-S 短信获取验证码
			$("#mobilePhoneValidata").validate({
				rules:{
					code:{
						required:true
						
					}
				},
				messages:{
					code:{
						required: "请输入验证码"
					}
				},
				errorElement:'em',
				errorPlacement:function(error,element){
					if(element.hasClass("setting_vtxt")){
						element.next().next().html(error);
					}else{
						element.next().html(error);
					}
			    },
			    success:function(label){
			    },  
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							$("#modifyPhoneForm").show();
	    							$("#mobilePhoneValidata").hide();
		    					}else{
		    						layer.msg(data.msg, 2, -1);
		    						$("#mobilePhoneValidata")[0].reset();
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E 短信获取验证码
			
			$("#video-btn").click(function(){
				require.async('jquery.form',function(){
				$("#modifyPayPwdForm1").ajaxSubmit({
                   type: "post",
                   url: "/member/useridentify/doVideo.html",
                   dataType: "json",
                   success: function(data){
                    require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
						layer.msg(data.msg, 2, -1);
                  });
               }
              })
			})
		  })

			
			//-S 绑定手机
			$("#modifyPhoneForm").validate({
				rules:{
					mobilePhone:{
						required:true,
						isMobile:true,
						remote:{
							type: "get",
							url: "/user/checkMobilePhone.html",
							data:{
								mobilePhone: function(){
									return $("#mobilePhone").val();
								}
							}
						}
					},
					code:{
						required:true
					}
				},
				messages:{
					mobilePhone:{
						required:"请输入手机号码",
						isMobile:"请填写正确的手机号码",
						remote:"该手机号码已存在"
					},
					code:{
						required: "请输入验证码"
					}
				},
				errorElement:'em',
				errorPlacement:function(error,element){
					if(element.hasClass("setting_vtxt")){
						element.next().next().html(error);
					}else{
						element.next().html(error);
					}
			    },
			    success:function(label){
			    },  
			    submitHandler:function(form){
	    			require.async('jquery.form',function(){
	    				$(form).ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							layer.msg("手机号码验证成功！", 2, -1);
	    							window.location.reload();	
		    					}else{
		    						layer.msg(data.msg, 2, -1);
		    						$(".setting_vtxt").val("");
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E 绑定手机
			
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
				errorElement:'em',
				errorPlacement:function(error,element){
					element.next().html(error);
			    },
			    success:function(label){
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
			    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>登录密码修改成功！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
		    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>登录密码已锁定，请24小时后重试，如有疑问请与客服联系，400-6366-800！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
			    						layer.msg(data.msg, 2, -1);
			    						$("#modifyPwdForm")[0].reset();
		    						}
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E登陆密码修改验证
			
			//-S支付密码验证
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
						regexPayPassword:"原密码格式错误"
					},
					newPayPwd:{
						required: "请输入6位数字",
						regexPayPassword:'新密码格式错误'
					},
					confirmNewPayPwd: {
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
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							$.ajax({
	    					    		url:"/nb/pc/cash/checkBank.html?random="+Math.random(),
	    					    		type:"post",
	    					    		success:function(json){
	    					    			var jsonObj = JSON.parse(json); 
	    					    			if(jsonObj.result == false)
	    					    			{
	    					    				$.layer({
				    							    type: 1,
				    							    closeBtn: [0,true],
				    				                		    title: "&nbsp;",
				    							    area: ['384px', '186px'],
				    							    border: [1, 1, '#cecfd0'],
				    							    page: {
				    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>支付密码修改成功！</span></div><br/><a href="/member/cash/bank.html" class="bank-btn">立即前往绑卡</a></div>'
				    							    },
				    							    close: function(index){
				    							    	window.location.reload();
				    							    },
				    							    end: function(){
				    							    	window.location.reload();
				    							    },
				    						});
	    					    			}
	    					    			else
	    					    			{
	    					    				$.layer({
				    							    type: 1,
				    							    closeBtn: [0,true],
				    				                		    title: "&nbsp;",
				    							    area: ['384px', '186px'],
				    							    border: [1, 1, '#cecfd0'],
				    							    time:3,
				    							    page: {
				    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>支付密码修改成功！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
		    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>支付密码已锁定，请24小时后重试，如有疑问请与客服联系，400-6366-800！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
			    						layer.msg(data.msg, 2, -1);
			    						$("#modifyPayPwdForm")[0].reset();
		    						}
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E支付密码验证
			
			//-S密保问题验证
			$("#modifyQuestionForm").validate({
				rules:{
					answer1:{
						required:true
					},
					answer2:{
						required:true
					},
					answer3:{
						required:true
					}
				},
				messages:{
					answer1:{
						required:"请输入问题一答案"
					},
					answer2:{
						required: "请输入问题二答案"
					},
					answer3: {
						required: "请输入问题三答案"
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
			    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>密保问题修改成功！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
		    					}else{
		    						layer.msg("密保问题修改失败！", 2, -1);
		    						$("#modifyQuestionForm")[0].reset();
		    					}
	    					})
		    			});
	    			});
		        } 
			})
			//-E密保问题验证
		});
	})
	
	
	//获取邮箱验证码
	$("#getMailVcode").click(function(){
		var o = this;
		$.ajax({
			url:'/nb/pc/security/modifyEmailCode.html?email='+$('#userMail').text(),
			type:'post',
			success:function(data){
				if(data.result){
					get_code_time();
				}
			}
		});
	})
	
	
	$("#getNewMailVcode").click(function(){
		var o = this;
		if($('#email').val()!="" ){
			//判断邮箱是否存在
			$.ajax({
				url: "/user/checkEmail.html",
				type:'post',
				data:{email:$("#email").val()},
				success:function(data){
					if(data){
						$.ajax({
							url:'/member/security/bindEmailCode.html?email='+$('#email').val(),
							type:'post',
							success:function(data){
								if(data.result){
									get_code_time(o);
								}
							}
						});
					}else{
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
							$.layer({
							    type: 1,
							    closeBtn: [0,true],
				                		    title: "&nbsp;",
							    area: ['384px', '186px'],
							    border: [1, 1, '#cecfd0'],
							    page: {
							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>该邮箱地址已存在</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
							    },
							    close: function(index){
							    	layer.closeAll();	
							    }
							});
							$(".failBtn").click(function(){
								layer.closeAll();	
							});
							return false;
						})
					}
				}
			})		
		}else{
			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
				$.layer({
				    type: 1,
				    closeBtn: [0,true],
	                		    title: "&nbsp;",
				    area: ['384px', '186px'],
				    border: [1, 1, '#cecfd0'],
				    page: {
				        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>请输入新邮箱</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
				    },
				    close: function(index){
				    	layer.closeAll();
				    }
				});
				$(".failBtn").click(function(){
					layer.closeAll();
				});
				return false;
			})
		}
	})
		

	
	//绑定手机时，获取验证码
	$("#getPhoneVcode").click(function(){
		var mobile = /^1[3|4|5|8][0-9]{9}$/; 
		$.ajax({
			type: "get",
			url: "/user/checkMobilePhone.html",
			data:{mobilePhone:$("#mobilePhone").val()},
			success:function(data){
				if(data){
					if($('#mobilePhone').val()!="" && mobile.test($('#mobilePhone').val())){
						$.ajax({
							url:'/nb/pc/security/bindPhoneCode.html?mobilePhone='+$('#mobilePhone').val(),
							type:'post',
							success:function(data){
								if(data.result){
									var time=60;
									var timeFun=setInterval(function(){
										time--;
										if(time>0){
											$('#getPhoneVcode').val(time+"秒后重新获取").attr("disabled",true);
										}else{
											time=60;
											$('#getPhoneVcode').val("获取验证码").removeAttr("disabled");
											clearInterval(timeFun);
										}
									},1000);
								}
							}
						});			
					}else{
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
								layer.msg("请输入正确的手机号码！", 1, -1);
								return false;
						})
					}
					
					}else{
						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
							layer.msg("该手机号码已存在", 1, -1);
							return false;
						})
					}
				
			}
		})
		

	})
	
	//修改手机，获取验证码
	$("#getHavePhoneVcode").click(function(){
		$.ajax({
			url:'/nb/pc/modifyPhoneCode.html?mobilePhone='+$('#userMobilePhone').text(),
			type:'post',
			success:function(data){
				if(data.result){
					var time=60;
					var timeFun=setInterval(function(){
						time--;
						if(time>0){
							$('#getHavePhoneVcode').val(time+"秒后重新获取").attr("disabled",true);
						}else{
							time=60;
							$('#getHavePhoneVcode').val("获取验证码").removeAttr("disabled");
							clearInterval(timeFun);
						}
					},1000);
				}
			}
		});
	})
	
	//前台登陆页面验证
	var count = $("#payCount").val();
	if( count == 1){
		$("#payCount").before('<dd><input class="code" name="validCode" autocomplete="off" placeholder="验证码" type="text" maxlength="4" /><img src="/validimg.html" align="absmiddle" class="valicode_img" alt="点击刷新" /></dd>');
	    $(".valicode_img").click(function(){
	    	$(this).attr("src",'/validimg.html?t=' + Math.random());
	    });
	}
	

	//手风琴切换效果
	require.async('commonJS/jquery.accordion',function(){
		$(".setting dd").accordion({
			wrapBox    : ".setting",//最外围wrap元素
			titleBox   : ".setting_btn",//显示的标题
			contentBox : ".setting_form",//隐藏的内容
			hoverClass : "setting_btn_selected",//展示之后的样式
			isClick    : true  //默认点击展开
		});
	})

});
