define(function(require,exports,module){
	require('jquery');

	//判断是否已认证或开通第三方接口
	$.ajax({
		url:"/member/cash/checkBank.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data)
		{
			if(data.result==false)
			{
					/*require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function()
					{
						//构造确认框DOM
						$.layer(
						{
							type: 1,
							closeBtn: false,
			                title: "&nbsp;",
						    area: ['384px', '186px'],
						    border: [1, 1, '#cecfd0'],
						    page: 
						    {
						        html: '<div class="tipsWrap w384"><div class="tipsTxt"><span>您尚未绑定银行卡，请先绑定。</span></div><div class="tipsBtnBar"><a href="/wx/account/addBank.html" class="okBtn">马上去绑定</a></div></div>'
						    }
					   });
				    });*/
			}
			else
			{
				var bank_array = new Array();
				var object = {};
				object.name = "工商银行";object.time_money = "5万";object.day_money = "5万";
				bank_array.push(object);
				object = {};
				object.name = "农业银行";object.time_money = "20万(6:00-22:00),2万(22:00-5.59)";object.day_money = "500万";
				bank_array.push(object);
				object = {};
				object.name = "建设银行";object.time_money = "20万";object.day_money = "无";
				bank_array.push(object);
				object = {};
				object.name = "中国银行";object.time_money = "1万";object.day_money = "无";
				bank_array.push(object);
				object = {};
				object.name = "邮政储蓄";object.time_money = "1万";object.day_money = "1万";
				bank_array.push(object);
				object = {};
				object.name = "招商银行";object.time_money = "5万";object.day_money = "5万";
				bank_array.push(object);
				object = {};
				object.name = "光大银行";object.time_money = "20万";object.day_money = "无";
				bank_array.push(object);
				object = {};
				object.name = " 广发银行";object.time_money = "20万";object.day_money = "无";
				bank_array.push(object);
				object = {};
				object.name = "平安银行";object.time_money = "20万";object.day_money = "无";
				bank_array.push(object);
				
				var bank_name = $("#bank_name").val();
				console.log(bank_array)
				for(var i = 0;i< bank_array.length;i++)
				{
					if(bank_array[i].name==bank_name)
					{
						$("#time_money").html(bank_array[i].time_money);
						$("#day_money").html(bank_array[i].day_money);
						$("#bank_img_url").attr("src","/data/bank/mini/"+bank_name+".png");
					}
				}
			}
		}
	})

	//判断是否充值成功

	require.async('jquery.form',function(){
		require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
	    	$("#form1").validate({
				rules:{
					money: {
			    		required: true,
			    		min:1,
			    		digits:true
			    	},
			    	payPwd: {
			    		required: true,
			    		minlength: 6,
			    		number:true
			    	}
			    },
			    messages:{
			    	money:{
			    		required: "请输入充值金额",
			    		min:"最小充值金额为1元",
			    		digits:"必须输入整数"
			    	},
			    	payPwd: {
			    		required: "请输入交易密码",
			    		minlength: "交易密码为6位数字",
			    		number:"交易密码为6位数字"
			    	}
			    },
			    errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				},
				success:function(element){
					element.parents(".input-row").removeClass("input-error");
				},
				submitHandler:function(form){
//					document.form1.submit();
					require.async("jquery.form",function(){
					   	$(form).ajaxSubmit(function(data){
					     	if(data.result==false){
								require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
									//构造确认框DOM
									$.layer({
										type: 1,
										closeBtn: true,
						                title: "&nbsp;",
									    area: ['450px', '190px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/wx/account/newRecharge.html' class='okBtn'>"+"重新充值"+"</a></div></div>"
									    }
									});
								});
							}else{
								require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
									//构造确认框DOM
									$.layer({
										type: 1,
										closeBtn: true,
						                title: "&nbsp;",
									    area: ['450px', '190px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"恭喜您，充值成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/wx/account/main.html' class='okBtn'>"+"确定"+"</a></div></div>"
									    }
									});
								});
							}
	    				})
	 				});
				}
   	
			})
    	})
    })
		

	
	
	// require.async('radio',function(){
	// 	$('.underline-bank input[type="radio"]').minRadio({
	// 		checkedClass:"bank_radio_hover", 
	// 		Element:"bank_radio",	
	// 		showTxt:true 
	// 	});	
	// 	$('.payList input[type="radio"]').minRadio({
	// 		checkedClass:"bank_radio_hover", 
	// 		Element:"bank_radio",	
	// 		showTxt:false
	// 	});	
	// 	$('.bank_list input[type="radio"]').minRadio({
	// 		checkedClass:"bank_radio_hover", 
	// 		Element:"bank_radio",	
	// 		showTxt:false
	// 	});
	// });
	
	//设置选中效果
	
	
	//设置选中效果
	//$(".on_off:checked").siblings().find(".switchBtn").addClass("on");
	
	//TAB切换
	// require.async('common1',function(){
	// 	$(".user_right_border").tabChange({
	// 		isClick:true,
 //            isHover:false,
	// 		childLi:".recharge_list li",//tab选项卡
	// 		childContent:".recharge_content",//tab内容
	// 		hoverClassName:"active",//选中当前选项卡的样式
	// 		callBack:false
	// 	});
	// });
	//
	// $(".recharge_list  li").on("click",function(){
	// 	$("#J_reCharge").each(function() {   
	// 	   this.reset();
	// 	   $(".bank_radio").removeClass("bank_radio_hover");
	// 	}); 
	// 	$(".underline-bank .bank_radio").eq(0).addClass("bank_radio_hover");
	// 	var statusVal =$(this).find("span").attr("data-value");
	// 	$('.paymentStatus').val(statusVal)
	// 	if(statusVal==3){
	// 		$("#J_reCharge").attr("action","/member/recharge/doOfflineRecharge.html");
	// 	}else{
	// 		$("#J_reCharge").attr("action","/member/recharge/doRecharge.html");
	// 	}
	// })
	
    //提交验证
   //  require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
   //  	$("#J_reCharge").validate({
			// rules:{
				// payOfflinebank:{
				// 	required: true
	   //  		},
				// money: {
		  //   		required: true,
		  //   		min:1,
		  //   		remote:"^[1-9]\d*$"
		  //   	},
		  //   	valicode: {
		  //   		required: true,
		  //   		minlength: 4
		  //   	}
		  //   },
		  //   messages:{
		   //  	payOfflinebank:{
					// required: "请选择账号"
	    // 		},
		    // 	money:{
		    // 		required: "请输入充值金额",
		    // 		min:"最小充值金额为1元",
		    // 		remote:"充值金额为正整数"
		    // 	},
		    // 	valicode: {
		    // 		required: "请输入验证码",
		    // 		minlength: "验证码格式错误",
		    // 	}
		    // },
	// 	    errorPlacement:function(error, element){
	// 		  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
	// 		},
	// 		success:function(element){
	// 			element.parents(".input-row").removeClass("input-error");
	// 		},
	// 	   submitHandler:function(form){
	// 		   require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	// 		   if(depositVal==0){
	// 			   var val=$('.paymentStatus').val();
	// 			   if(val==3){//线下支付
	// 				   require.async('jquery.form',function(){
	// 					   $("form").ajaxSubmit({
	// 						   dataType:'json',
	// 						   success:function(data){
	// 							   if(data.result==true){
	// 				        			$.layer({
	// 										type: 1,
	// 									    closeBtn: [0,true],
	// 						                title: "&nbsp;",
	// 									    area: ['384px', '186px'],
	// 									    border: [1, 1, '#cecfd0'],
	// 									    time:3,
	// 									    page: {
	// 									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>充值申请提交成功，请耐心等待审核！</span></div><div class="tipsBtnBar"><a href="/wx/account/main.html" class="okBtn">确定</a></div></div>'
	// 									    },
	// 									    close: function(index){
	// 									    	window.location.reload();
	// 									    },
	// 									    end: function(){
	// 									    	window.location.reload();
	// 									    },
	// 	    							    success: function(layero){
	// 	    							    	var time =3; 
	// 	    							    	function closeTime () {
	// 	    										time--;
	// 	    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
	// 		    								}
	// 	    							    	setInterval(function(){
	//     											closeTime();
	//     										}, 1000);
	// 	    							    }
	// 									});
	// 									$(".okBtn").click(function(){
	// 										window.location.href = "/member/recharge/log.html";
	// 									});
	// 					        	}else{
	// 				        			$.layer({
	// 										type: 1,
	// 									    closeBtn: [0,true],
	// 						                title: "&nbsp;",
	// 									    area: ['384px', '186px'],
	// 									    border: [1, 1, '#cecfd0'],
	// 									    page: {
	// 									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="/wx/account/main.html" class="okBtn failBtn">确定</a></div></div>'
	// 									    },
	// 									    close: function(index){
	// 									    	layer.closeAll();
	// 									    	$(".valicode_img").each(function(){
	// 												$(this).attr("src",'/validimg.html?t=' + Math.random());
	// 							        		})
	// 									    }
	// 									});
	// 									$(".failBtn").click(function(){
	// 										layer.closeAll();
	// 										$(".valicode_img").each(function(){
	// 											$(this).attr("src",'/validimg.html?t=' + Math.random());
	// 						        		})
	// 									});
	// 					        	}
	// 						   }
	// 					   })
	// 				   })
	// 			   }else if(val==2){//网上支付
	// 				   form.submit();
	// 				   $.layer({
 //                           type: 1,
 //                           closeBtn: [0,true],
 //                           title: "&nbsp;",
 //                           area: ['460px', '194px'],
 //                           border: [1, 1, '#cecfd0'],
 //                           page: {
 //                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="/wx/account/main.html" class="okBtn">充值成功</a><a href="/wx/account/newRecharge.html" class="cancleBtn">充值失败</a></div></div>'
 //                           }
 //                       });
	// 					$(".okBtn").click(function(){
	// 						window.location.href = "/member/recharge/log.html";
	// 					});
	// 					$(".cancleBtn").click(function(){
	// 						layer.closeAll();
	// 					});
	// 			   }else{//网银直联
	// 				   form.submit();
	// 				   $.layer({
 //                           type: 1,
 //                           closeBtn: [0,true],
 //                           title: "&nbsp;",
 //                           area: ['460px', '194px'],
 //                           border: [1, 1, '#cecfd0'],
 //                           page: {
 //                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="/wx/account/main.html" class="okBtn">充值成功</a><a href="/wx/account/newRecharge.html" class="cancleBtn">充值失败</a></div></div>'
 //                           }
 //                       });
	// 					$(".okBtn").click(function(){
	// 						window.location.href = "/member/recharge/log.html";
	// 					});
	// 					$(".cancleBtn").click(function(){
	// 						layer.closeAll();
	// 						$(".valicode_img").each(function(){
	// 							$(this).attr("src",'/validimg.html?t=' + Math.random());
	// 		        		})
	// 					});
	// 			   }  	
	// 		   }else{
	// 			   	form.submit();
	// 				//构造确认框DOM
	// 			   	$.layer({
 //                        type: 1,
 //                        closeBtn: [0,true],
 //                        title: "&nbsp;",
 //                        area: ['460px', '194px'],
 //                        border: [1, 1, '#cecfd0'],
 //                        page: {
 //                            html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="/wx/account/main.html" class="okBtn">充值成功</a><a href="/wx/account/newRecharge.html" class="cancleBtn">充值失败</a></div></div>'
 //                        }
 //                    });
	// 				$(".okBtn").click(function(){
	// 					window.location.href = "/member/recharge/log.html";
	// 				});
	// 				$(".cancleBtn").click(function(){
	// 					layer.closeAll();
	// 					$(".valicode_img").each(function(){
	// 						$(this).attr("src",'/validimg.html?t=' + Math.random());
	// 	        		})
	// 				});
	// 		   }
	// 		   });
	// 	   }  
	// 	});
 //    })
    
	
	// function show_recharge(){
	// 	$(".J_valicode_img").each(function(){
	// 		$(this).attr("src",'/validimg.html?t=' + Math.random());
	// 	})
	// 	jQuery( "#modal_dialog" ).dialog({ autoOpen: false , modal: true ,height: 160,width:360 });
	// 	jQuery( "#modal_dialog" ).dialog( "open" );
	// }

});