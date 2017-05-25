define(function(require,exports,module){
	require('jquery');
	$.ajax({
		url:"/nb/wechat/recharge/querySupportBank.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
//				var str = "<tr height=\"25px\" ></tr><tr><td></td><td>单笔限额</td><td>单日限额</td></tr>";  
				var str = "<tbody><thead><th style=\"vertical-align: middle;\">银行名称</th><th>单笔<br>限额</th><th>单日<br>限额</th><th>单月<br>限额</th></thead>";
			    if(data.ret_code!='0000')
			    {
//			      str += "暂无银行卡"; 

			    }else{
			      	var length = data.support_banklist.length;
			        if(length>0){
			        	for (var i = 0; i < length; i++) {
			        		var bankName = data.support_banklist[i].bank_name;
			        		var numInput0 = parseFloat(data.support_banklist[i].single_amt)/10000 + "万";
				        	var numInput1 = parseFloat(data.support_banklist[i].day_amt)/10000 + "万";
				        	var numInput2 = parseFloat(data.support_banklist[i].month_amt)/10000 + "万";
				        	if(i%2==0){
				        		/*if(bankCode==="01020000" || bankCode==="03080000"){
				        			str += "<tr><td><img src=\"/data/bank/llbank/"+bankCode+".gif\" style=\"border: 1px solid #ddd;width: 118px;\"/></td><td>"+numInput0+"</td><td>"+numInput1+"</td></tr>";
				        		}else if(bankCode==="01050000"){
				        			str += "<tr><td><img src=\"/data/bank/llbank/"+bankCode+".gif\"/></td><td>"+numInput0+"(0:00-22:00)</td><td>"+numInput1+"(0:00-22:00)</td></tr>";
				        		}else{			        			
				        			str += "<tr><td><img src=\"/data/bank/llbank/"+bankCode+".gif\"/></td><td>"+numInput0+"</td><td>"+numInput1+"</td></tr>";
				        		}*/
				        		str += "<tr><td>"+bankName+"</td><td>"+numInput0+"</td><td>"+numInput1+"</td><td>"+numInput2+"</td></tr>";
				        	}else{				        		
				        		/*if(bankCode==="01020000" || bankCode==="03080000"){
				        			str += "<tr class=\"pure-table-odd\"><td><img src=\"/data/bank/llbank/"+bankCode+".gif\" style=\"border: 1px solid #ddd;width: 118px;\"/></td><td>"+numInput0+"</td><td>"+numInput1+"</td></tr>";
				        		}else if(bankCode==="01050000"){
				        			str += "<tr class=\"pure-table-odd\"><td><img src=\"/data/bank/llbank/"+bankCode+".gif\"/></td><td>"+numInput0+"(0:00-22:00)</td><td>"+numInput1+"(0:00-22:00)</td></tr>";
				        		}else{			        			
				        			str += "<tr class=\"pure-table-odd\"><td><img src=\"/data/bank/llbank/"+bankCode+".gif\"/></td><td>"+numInput0+"</td><td>"+numInput1+"</td></tr>";
				        		}*/
				        		str += "<tr class=\"pure-table-odd\"><td>"+bankName+"</td><td>"+numInput0+"</td><td>"+numInput1+"</td><td>"+numInput2+"</td></tr>";
				        	}
						}
			        }
			    }
			    str+="<tr class=\"pure-table-odd\"><td colspan=\"4\" style=\"color: red;height:20px;line-height:20px;width:100%;text-align: left;\" >备注：商户限额、用户银行卡本身限额、认证支付标准限额，3者取最低限额。限额表仅供参考，实际以支付界面提示为准。</td></tr></tbody>"
			    document.getElementById("bankTable").innerHTML=str;
		}
	})
	//判断是否已认证或开通第三方接口
	$.ajax({
		url:"/nb/wechat/cash/checkBank.html?random="+Math.random(),
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
				var bank_code = $("#bank_code").val();
				var bank_name = $("#bank_name").val();
				$.ajax({
					url:"/nb/wechat/recharge/querySupportBank.html?bank_code="+bank_code+"&random="+Math.random(),
					type:"get",
					dataType:"json",
					success:function(data){
						    if(data.ret_code==='0000'){
						      	var length = data.support_banklist.length;
						        if(length>0){
						        	var numInput0 = 0;
						        	var numInput1 = 0;
						        	if(bank_code.length>0 && bank_code!=""){	
							        	numInput0 = parseFloat(data.support_banklist[0].single_amt);
							        	numInput1 = parseFloat(data.support_banklist[0].day_amt);
						        	}else{
						        		for (var i = 0; i < length; i++) {
						        			var llbank_name = data.support_banklist[i].bank_name;
						        			if(llbank_name.length>4){
						        				if(llbank_name==="广东发展银行"){
						        					llbank_name = "广发银行";
						        				}else{						        					
						        					llbank_name = llbank_name.replace("中国", "");
						        				}
								        	}
						        			if(llbank_name===bank_name){
						        				bank_code = data.support_banklist[i].bank_code;
						        				numInput0 = parseFloat(data.support_banklist[i].single_amt);
									        	numInput1 = parseFloat(data.support_banklist[i].day_amt);
									        	break;
						        			}
										}
						        	}
						        	$("#time_money").html(numInput0/10000 + "万");
									$("#day_money").html(numInput1/10000 + "万");
									$("#bank_img_url").attr("src","/data/bank/llmini/"+bank_code+".png");
						        }
						        document.getElementById("bankTbl").style.display="block";
						    }
					}
				})
//				var bank_array = new Array();
//				var object = {};
//				object.name = "工商银行";object.time_money = "5万";object.day_money = "5万";
//				bank_array.push(object);
//				object = {};
//				object.name = "农业银行";object.time_money = "20万(6:00-22:00),2万(22:00-5.59)";object.day_money = "500万";
//				bank_array.push(object);
//				object = {};
//				object.name = "建设银行";object.time_money = "20万";object.day_money = "无";
//				bank_array.push(object);
//				object = {};
//				object.name = "中国银行";object.time_money = "1万";object.day_money = "无";
//				bank_array.push(object);
//				object = {};
//				object.name = "邮政储蓄";object.time_money = "1万";object.day_money = "1万";
//				bank_array.push(object);
//				object = {};
//				object.name = "招商银行";object.time_money = "5万";object.day_money = "5万";
//				bank_array.push(object);
//				object = {};
//				object.name = "光大银行";object.time_money = "20万";object.day_money = "无";
//				bank_array.push(object);
//				object = {};
//				object.name = " 广发银行";object.time_money = "20万";object.day_money = "无";
//				bank_array.push(object);
//				object = {};
//				object.name = "平安银行";object.time_money = "20万";object.day_money = "无";
//				bank_array.push(object);
//				
//				var bank_name = $("#bank_name").val();
//				console.log(bank_array)
//				for(var i = 0;i< bank_array.length;i++)
//				{
//					if(bank_array[i].name==bank_name)
//					{
//						$("#time_money").html(bank_array[i].time_money);
//						$("#day_money").html(bank_array[i].day_money);
//						$("#bank_img_url").attr("src","/data/bank/mini/"+bank_name+".png");
//					}
//				}
				
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
			    		min:0.01,
			    		number:true
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
			    		min:"最小充值金额为0.01元",
			    		number:"必须输入数字"
			    	},
			    	payPwd: {
			    		required: "请输入交易密码",
			    		minlength: "交易密码为6位数字",
			    		number:"交易密码为6位数字"
			    	}
			    },
			    errorPlacement:function(error, element){
//				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
				  	$("#money").addClass("border");
				},
				success:function(element){
//					element.parents(".input-row").removeClass("input-error");
					$("#money").removeClass("border");
				},
				submitHandler:function(form){
//					var money = document.getElementById("money").value;
//					alert(money);
//			  		if(money.length!=0 && parseFloat(money)>10){
//			  			wechatAlert("测试期间建议充值金额在10元以内！","0");
//			  		}else{			  			
//			  			document.form1.submit();
//			  		}
					/*require.async("jquery.form",function(){
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
	 				});*/
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