define(function(require,exports,module){
	require('jquery');
	
	//判断是否已认证或开通第三方接口
	$.ajax({
		url:"/member/cash/checkBank.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
				/*if(data.result==false){
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
				}*/
			});
		}
	})
	
   var cashCardListli = $(".cashCardList li");
    cashCardListli.eq(0).find(".bank_radio").addClass("bank_radio_hover");
    cashCardListli.eq(0).find("input[name='bankNo']").attr("checked","checked");
	cashCardListli.each(function(){
		$(this).click(function(){
			cashCardListli.find(".bank_radio").removeClass("bank_radio_hover");
			cashCardListli.find("input[name='bankNo']").removeAttr('checked');
			$(this).find(".bank_radio").addClass("bank_radio_hover");
			$(this).find("input[name='bankNo']").attr("checked","checked");
		})
	});

	
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		require.async("/plugins/jquery-validation-1.13.0/additional-methods",function(){
			$("#J_cash").validate({
				rules: {
					money: {
						required: true,
						max:parseFloat($("#available_Balance").text()),
						min:0.01
					},
					payPwd:{
						required:true
					}
				},
				messages:{
					money: {
						required: "请输入提现金额" ,
						max:"超过可提现金额",
						min:"不能小于0.01元"
					},
					payPwd:{
						required: "请输入支付密码",
					}
				},
				errorElement:"em",
				errorPlacement:function(error,element){
					error.appendTo(element.parent().find("span"));
				},
				submitHandler:function(form){
						require.async('jquery.form',function(){
							   $("form").ajaxSubmit(function(data){
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
			    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>提现成功，等候系统审核！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
			    							    },
			    							    close: function(index){
			    							    	window.location.href = "/member/cash/log.html";
			    							    },
			    							    end: function(){
			    							    	window.location.href = "/member/cash/log.html";
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
											        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
											    },
											    close: function(index){
											    	layer.closeAll();
											    }
											});
				    						$(".failBtn").click(function(){
				    							layer.closeAll();
											});
				    					}
						        });
						     });
						   })
					
			  }
		});
		});
	});
	
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		require.async("/plugins/jquery-validation-1.13.0/additional-methods",function(){
			$("#J_offline_cash").validate({
				rules: {
					money: {
						required: true,
						max:parseFloat($("#available_Balance").text()),
						min:parseFloat($("#minCashMoney").val())
					},
					xxbankNo:{
						required: true,
					},
					payPwd:{
						required:true
					}
				},
				messages:{
					money: {
						required: "请输入提现金额" ,
						max:"超过可提现金额",
						min:"线下提现"+$("#minCashMoney").val()+"元起"
					},
					xxbankNo:{
						required: "请输入银行卡号",
					},
					realName:{
						required: "请输入真实姓名",
					}
				},
				errorElement:"em",
				errorPlacement:function(error,element){
					error.appendTo(element.parent().find("span"));
				},
				submitHandler:function(form){
						require.async('jquery.form',function(){
							 $('#J_offline_cash').ajaxSubmit(function(data){
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
			    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>提现成功，等候系统审核！</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
			    							    },
			    							    close: function(index){
			    							    	window.location.href = "/member/cash/log.html";
			    							    },
			    							    end: function(){
			    							    	window.location.href = "/member/cash/log.html";
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
											        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
											    },
											    close: function(index){
											    	layer.closeAll();
											    }
											});
				    						$(".failBtn").click(function(){
				    							layer.closeAll();
											});
				    					}
						        });
						     });
						   })
					
			  }
		});
		});
	});
	
		
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
	});
	//
	$(".cz-fs span").on("click",function(){
		$("#J_reCharge").each(function() {   
		   this.reset();
		   $(".bank_radio").removeClass("bank_radio_hover");
		}); 
		$(".underline-bank .bank_radio").eq(0).addClass("bank_radio_hover");
		var statusVal =$(this).attr("data-value");
		$('.paymentStatus').val(statusVal);
		if(statusVal==3){
			$("#J_reCharge").attr("action","/member/recharge/doOfflineRecharge.html");
			$("#offline").show();
			$("#online").hide();
		}else{
			$("#J_reCharge").attr("action","/member/recharge/doRecharge.html");
		}
		if(statusVal==2){
			$("#offline").hide();
			$("#online").show();
		}
	})
	

	function show_recharge(){
		$(".J_valicode_img").each(function(){
			$(this).attr("src",'/validimg.html?t=' + Math.random());
		})
		jQuery( "#modal_dialog" ).dialog({ autoOpen: false , modal: true ,height: 160,width:360 });
		jQuery( "#modal_dialog" ).dialog( "open" );
	}
	

});