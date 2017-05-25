define(function(require,exports,module){
	require('jquery.1.11.1');
	document.getElementById('cachMoney').value='';
	document.getElementById('payPwd').value='';
	
	/*$.ajax({
		url:"/member/cash/checkBank.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			if(data.result==false){
				require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
					//构造确认框DOM
					$.layer({
						type: 1,
						closeBtn: false,
		                title: "&nbsp;",
					    area: ['450px', '190px'],
					    border: [1, 1, '#cecfd0'],
					    page: {
					        html: '<div class="tipsWrap w450"><div class="tipsTxt"><i class="iconfont tipIco">&#xe63c;</i><span>您尚未通过绑定银行卡，请先绑定银行卡。</span></div><div class="tipsBtnBar"><a href="/member/cash/bank.html" class="okBtn">马上去绑定</a></div></div>'
					    }
					});
			});
			}
		}
	})*/
	//判断是否充值成功

	require.async('jquery.form',function(){
		var checkSubmitFlg = false;
		var hidWebRechargeUrl = $("#hidWebRechargeUrl").val();
		var hidWebChannelKey = $("#hidWebChannelKey").val();
		var channelType = $("#channelType").val();
		var cType = $("#cType").val();
		var xsRToken = $("#xsRToken").val();
//		alert(hidWebRechargeUrl+"-------"+hidWebChannelKey);
		$("#cashBtn1").click(function(){
			var cachMoney = $("#cachMoney").val();
			var payPwd = $("#payPwd").val();
			if(hidWebChannelKey==="llpay_channel_key"){
				if(checkSubmitFlg ==true){ 
					return false; //当表单被提交过一次后checkSubmitFlg将变为true,根据判断将无法进行提交。 
				} 
				checkSubmitFlg =true; 						
				$.ajax({
					type: "post",
					url: hidWebRechargeUrl+"?&payPwd="+payPwd+"&rechargeToken="+xsRToken,
					dataType: "json",
					success: function(data){
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
										html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/member/recharge/newRecharge.html' class='okBtn'>"+"重新充值"+"</a></div></div>"
									}
								});
							});
							checkSubmitFlg =false;
						}else{
//							window.location.href = "/member/recharge/subRecharge.html";
							document.reform1.submit();
						}
						checkSubmitFlg =false;
					}
				});
			}else{				
				$.ajax({
					url:"/member/cash/checkBank.html?random="+Math.random(),
					type:"get",
					dataType:"json",
					success:function(data){
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
										html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>&#xe63c;</i><span>您尚未通过绑定银行卡，请先绑定银行卡。</span></div>"+"<div class='tipsBtnBar'><a href='javascript:;' class='cancleBtn'>取消</a><a href='/member/cash/bank.html' class='okBtn'>马上去绑定</a></div></div>"
									}
								});
								$(".cancleBtn").click(function(){
									layer.closeAll();
								});
							});
						}else{
							//alert(checkSubmitFlg);
							if(checkSubmitFlg ==true){ 
								return false; //当表单被提交过一次后checkSubmitFlg将变为true,根据判断将无法进行提交。 
							} 
							checkSubmitFlg =true; 						
							$.ajax({
								type: "post",
								url: hidWebRechargeUrl+"?money="+cachMoney+"&payPwd="+payPwd+"&channelType="+channelType+"&type="+cType+"&rechargeToken="+xsRToken,
								dataType: "json",
								async:false,
								success: function(data){
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
													html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/member/recharge/newRecharge.html' class='okBtn'>"+"重新充值"+"</a></div></div>"
												}
											});
										});
										checkSubmitFlg =false;
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
													html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"恭喜您，充值成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/member/recharge/newRecharge.html' class='okBtn'>"+"确定"+"</a></div></div>"
												}
											});
										});
										checkSubmitFlg =false;
									}
								}
							});
						}
					}
				})
			}
		})
		
		
		
	$("#cashBtn2").click(function(){
		var money = document.getElementById("cachMoney2").value;
		var accname = document.getElementById("realName").value;
		var cardno = document.getElementById("infoyhzh").value;
		var minRechargeMoney = document.getElementById("minRechargeMoney").value;
		var channelType = $("#channelType").val();
		var cType = $("#cType").val();
		var xsRToken = $("#xsRToken").val();
		if(money==''){
	        alertInput("请输入充值金额！");
	        return;
	    }else if(money<parseFloat(minRechargeMoney)){
	    	alertInput("充值金额不能小于"+minRechargeMoney+"元！");
		    return;
	    }
		if(accname==''){
	        alertInput("请输入您的真实姓名！");
	        return;
	    }
		if(cardno==''){
	        alertInput("请输入您的银行卡号！");
	        return;
	    }
		
		document.getElementById('cachMoney').value='';
		document.getElementById('payPwd').value='';
		
		 $.ajax({
		     type: "post",
		     url: "/member/recharge/doOfflineRecharge.html?money="+money+"&realName="+accname+"&bankNo="+cardno+"&channelType="+channelType+"&type="+cType+"&rechargeToken="+xsRToken,
		     dataType: "json",
		     success: function(data){
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
						        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>&#xe63c;</i><span>"+data.msg+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='javascript:;' class='okBtn'>"+"重新充值"+"</a></div></div>"
						    }
						});
						$(".okBtn").click(function(){
							layer.closeAll();
						});
				});
			}
			else{
				require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
						//构造确认框DOM
						$.layer({
							type: 1,
							closeBtn: true,
			                title: "&nbsp;",
						    area: ['450px', '190px'],
						    border: [1, 1, '#cecfd0'],
						    page: {
						        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont okIco'>&#xe63d;</i><span>"+"线下充值预约已成功，请及时至银行转账"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/member/recharge/newRecharge.html?payOfflineVal=1' class='okBtn'>"+"确定"+"</a></div></div>"
						    }
						});
				});
				
				$("#offline").show();
				$("#online").hide();
				
			}
		     }
		 });
	});
	})
		
	$("#infoyhzh").blur(function(){
       		var cardval = $("#infoyhzh").val()
       		 $.ajax({
					url:"/member/cash/checkBankCanUse.html?cardNo="+cardval,
					type:"get",
					dataType:"json",
					success:function(data){
						if(data.result==false){
							$(".yhcard").html(data.message).css("color","red")
							$(".cardAddBtn").attr("disabled","disabled")	
							$(".card-tip").html()
							$(".card-tip").addClass('hide')
						}
						else{
							$(".yhcard").html('')
							$(".cardAddBtn").removeAttr("disabled"); 
							var html = "<img src="+data.supportBank.logo+"> " + data.supportBank.name
							$(".card-tip").html(html)
							$(".card-tip").removeClass('hide')
						}
					}
				})
       })
	
	
	require.async('radio',function(){
		$('.underline-bank input[type="radio"]').minRadio({
			checkedClass:"bank_radio_hover", 
			Element:"bank_radio",	
			showTxt:true 
		});	
		$('.payList input[type="radio"]').minRadio({
			checkedClass:"bank_radio_hover", 
			Element:"bank_radio",	
			showTxt:false
		});	
		$('.bank_list input[type="radio"]').minRadio({
			checkedClass:"bank_radio_hover", 
			Element:"bank_radio",	
			showTxt:false
		});
	});
	
	//设置选中效果
	
	
	//设置选中效果
	//$(".on_off:checked").siblings().find(".switchBtn").addClass("on");
	
	//TAB切换
	require.async('common1',function(){
		$(".user_right_main").tabChange({
			isClick:true,
            isHover:false,
			childLi:".cz-fs span",//tab选项卡
			childContent:".user_cash_box",//tab内容
			hoverClassName:"active",//选中当前选项卡的样式
			callBack:false
		});
		$(".cash_detail").show();
	});
	//
	$(".cz-fs span").on("click",function(){
		$("#J_reCharge").each(function() {   
		   this.reset();
		   $(".bank_radio").removeClass("bank_radio_hover");
		}); 
		$(".underline-bank .bank_radio").eq(0).addClass("bank_radio_hover");
		var statusVal =$(this).attr("data-value");
		$('.paymentStatus').val(statusVal)
		if(statusVal==3){
			$("#J_reCharge").attr("action","/member/recharge/doOfflineRecharge.html");
			$("#offline").show();
			$("#online").hide();
			$(".cash_detail").hide();
		}
		if(statusVal==2){
			$("#J_reCharge").attr("action","/member/recharge/doRecharge.html");
			$("#offline").hide();
			$("#online").show();
			$(".cash_detail").show();
		}
	})
	
    //提交验证
//    require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
//    	$("#J_reCharge").validate({
//			rules:{
//				payOfflinebank:{
//					required: true
//	    		},
//				money: {
//		    		required: true,
//		    		min:0.01
//		    	},
//		    	payPwd:{
//					required:true
//				}
//		    },
//		    messages:{
//		    	payOfflinebank:{
//					required: "请选择账号"
//	    		},
//		    	money:{
//		    		required: "请输入充值金额",
//		    		min:"最小充值金额为0.01元"
//		    	},
//		    	payPwd:{
//					required: "请输入支付密码",
//				}
//		   },
//		   errorElement:"em",
//		   errorPlacement:function(error,element){
//		   	element.parent().find("span").html(error);
//		   },
//		   submitHandler:function(form){
//			   require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
//			   if(depositVal==0){
//				   var val=$('.paymentStatus').val();
//				   if(val==3){//线下支付
//					   require.async('jquery.form',function(){
//						   $("form").ajaxSubmit({
//							   dataType:'json',
//							   success:function(data){
//								   if(data.result==true){
//					        			$.layer({
//											type: 1,
//										    closeBtn: [0,true],
//							                title: "&nbsp;",
//										    area: ['384px', '186px'],
//										    border: [1, 1, '#cecfd0'],
//										    time:3,
//										    page: {
//										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>充值申请提交成功，请耐心等待审核！</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确定</a></div></div>'
//										    },
//										    close: function(index){
//										    	window.location.reload();
//										    },
//										    end: function(){
//										    	window.location.reload();
//										    },
//		    							    success: function(layero){
//		    							    	var time =3; 
//		    							    	function closeTime () {
//		    										time--;
//		    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
//			    								}
//		    							    	setInterval(function(){
//	    											closeTime();
//	    										}, 1000);
//		    							    }
//										});
//										$(".okBtn").click(function(){
//											window.location.href = "/member/recharge/log.html";
//										});
//						        	}else{
//					        			$.layer({
//											type: 1,
//										    closeBtn: [0,true],
//							                title: "&nbsp;",
//										    area: ['384px', '186px'],
//										    border: [1, 1, '#cecfd0'],
//										    page: {
//										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
//										    },
//										    close: function(index){
//										    	layer.closeAll();
//										    	$(".valicode_img").each(function(){
//													$(this).attr("src",'/validimg.html?t=' + Math.random());
//								        		})
//										    }
//										});
//										$(".failBtn").click(function(){
//											layer.closeAll();
//											$(".valicode_img").each(function(){
//												$(this).attr("src",'/validimg.html?t=' + Math.random());
//							        		})
//										});
//						        	}
//							   }
//						   })
//					   })
//				   }else if(val==2){//网上支付
//					   form.submit();
//					   $.layer({
//                           type: 1,
//                           closeBtn: [0,true],
//                           title: "&nbsp;",
//                           area: ['460px', '194px'],
//                           border: [1, 1, '#cecfd0'],
//                           page: {
//                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
//                           }
//                       });
//						$(".okBtn").click(function(){
//							window.location.href = "/member/recharge/log.html";
//						});
//						$(".cancleBtn").click(function(){
//							layer.closeAll();
//						});
//				   }else{//网银直联
//					   form.submit();
//					   $.layer({
//                           type: 1,
//                           closeBtn: [0,true],
//                           title: "&nbsp;",
//                           area: ['460px', '194px'],
//                           border: [1, 1, '#cecfd0'],
//                           page: {
//                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
//                           }
//                       });
//						$(".okBtn").click(function(){
//							window.location.href = "/member/recharge/log.html";
//						});
//						$(".cancleBtn").click(function(){
//							layer.closeAll();
//							$(".valicode_img").each(function(){
//								$(this).attr("src",'/validimg.html?t=' + Math.random());
//			        		})
//						});
//				   }  	
//			   }else{
//				   	form.submit();
//					//构造确认框DOM
//				   	$.layer({
//                        type: 1,
//                        closeBtn: [0,true],
//                        title: "&nbsp;",
//                        area: ['460px', '194px'],
//                        border: [1, 1, '#cecfd0'],
//                        page: {
//                            html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
//                        }
//                    });
//					$(".okBtn").click(function(){
//						window.location.href = "/member/recharge/log.html";
//					});
//					$(".cancleBtn").click(function(){
//						layer.closeAll();
//						$(".valicode_img").each(function(){
//							$(this).attr("src",'/validimg.html?t=' + Math.random());
//		        		})
//					});
//			   }
//			   });
//		   }  
//		});
//    })
    
    function checkBank(){
		$.ajax({
			url:"/member/cash/checkBank.html?random="+Math.random(),
			type:"get",
			dataType:"json",
			success:function(data){
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
						        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>&#xe63c;</i><span>您尚未通过绑定银行卡，请先绑定银行卡。</span></div>"+"<div class='tipsBtnBar'><a href='javascript:;' class='cancleBtn'>取消</a><a href='/member/cash/bank.html' class='okBtn'>马上去绑定</a></div></div>"
						    }
						});
						$(".cancleBtn").click(function(){
							layer.closeAll();
						});
					});
					return false;
				}else{
					return true;
				}
			}
		})
	}
    
	function alertInput(obj){
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
				type: 1,
				closeBtn: true,
                title: "&nbsp;",
			    area: ['450px', '190px'],
			    border: [1, 1, '#cecfd0'],
			    page: {
			        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+obj+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='javascript:;' class='okBtn'>"+"确定"+"</a></div></div>"
			    }
			});
			$(".okBtn").click(function(){
				layer.closeAll();
			});
		});
	}
	
	function show_recharge(){
		$(".J_valicode_img").each(function(){
			$(this).attr("src",'/validimg.html?t=' + Math.random());
		})
		jQuery( "#modal_dialog" ).dialog({ autoOpen: false , modal: true ,height: 160,width:360 });
		jQuery( "#modal_dialog" ).dialog( "open" );
	}
	
});