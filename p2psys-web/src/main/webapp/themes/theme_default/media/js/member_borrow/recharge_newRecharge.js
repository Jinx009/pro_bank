define(function(require,exports,module){
	require('jquery');
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
	})
	//判断是否充值成功

	require.async('jquery.form',function(){
		$(".cashBtn").click(function(){
		 $("#form1").ajaxSubmit({
		     type: "post",
		     url: "/member/recharge/doUnionPayRecharge.html",
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
						        html: "<div class='tipsWrap w450'>"+"<div class='tipsTxt'>"+"<i class='iconfont tipIco'>"+"&#xe63c;"+"</i><span>"+"恭喜您，充值成功！"+"</span></div>"+"<div class='tipsBtnBar'>"+"<a href='/member/recharge/newRecharge.html' class='okBtn'>"+"确定"+"</a></div></div>"
						    }
						});
				});
			}
		     }
		 });
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
		$(".user_right_border").tabChange({
			isClick:true,
            isHover:false,
			childLi:".recharge_list li",//tab选项卡
			childContent:".recharge_content",//tab内容
			hoverClassName:"active",//选中当前选项卡的样式
			callBack:false
		});
	});
	//
	$(".recharge_list  li").on("click",function(){
		$("#J_reCharge").each(function() {   
		   this.reset();
		   $(".bank_radio").removeClass("bank_radio_hover");
		}); 
		$(".underline-bank .bank_radio").eq(0).addClass("bank_radio_hover");
		var statusVal =$(this).find("span").attr("data-value");
		$('.paymentStatus').val(statusVal)
		if(statusVal==3){
			$("#J_reCharge").attr("action","/member/recharge/doOfflineRecharge.html");
		}else{
			$("#J_reCharge").attr("action","/member/recharge/doRecharge.html");
		}
	})
	
    //提交验证
    require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
    	$("#J_reCharge").validate({
			rules:{
				payOfflinebank:{
					required: true
	    		},
				money: {
		    		required: true,
		    		min:0.01
		    	},
		    	valicode: {
		    		required: true,
		    		minlength: 4
		    	}
		    },
		    messages:{
		    	payOfflinebank:{
					required: "请选择账号"
	    		},
		    	money:{
		    		required: "请输入充值金额",
		    		min:"最小充值金额为0.01元"
		    	},
		    	valicode: {
		    		required: "请输入验证码",
		    		minlength: "验证码格式错误",
		    	}
		   },
		   errorElement:"em",
		   errorPlacement:function(error,element){
		   	element.parent().find("span").html(error);
		   },
		   submitHandler:function(form){
			   require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			   if(depositVal==0){
				   var val=$('.paymentStatus').val();
				   if(val==3){//线下支付
					   require.async('jquery.form',function(){
						   $("form").ajaxSubmit({
							   dataType:'json',
							   success:function(data){
								   if(data.result==true){
					        			$.layer({
											type: 1,
										    closeBtn: [0,true],
							                title: "&nbsp;",
										    area: ['384px', '186px'],
										    border: [1, 1, '#cecfd0'],
										    time:3,
										    page: {
										        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>充值申请提交成功，请耐心等待审核！</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">确定</a></div></div>'
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
		    							    	setInterval(function(){
	    											closeTime();
	    										}, 1000);
		    							    }
										});
										$(".okBtn").click(function(){
											window.location.href = "/member/recharge/log.html";
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
										    	$(".valicode_img").each(function(){
													$(this).attr("src",'/validimg.html?t=' + Math.random());
								        		})
										    }
										});
										$(".failBtn").click(function(){
											layer.closeAll();
											$(".valicode_img").each(function(){
												$(this).attr("src",'/validimg.html?t=' + Math.random());
							        		})
										});
						        	}
							   }
						   })
					   })
				   }else if(val==2){//网上支付
					   form.submit();
					   $.layer({
                           type: 1,
                           closeBtn: [0,true],
                           title: "&nbsp;",
                           area: ['460px', '194px'],
                           border: [1, 1, '#cecfd0'],
                           page: {
                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
                           }
                       });
						$(".okBtn").click(function(){
							window.location.href = "/member/recharge/log.html";
						});
						$(".cancleBtn").click(function(){
							layer.closeAll();
						});
				   }else{//网银直联
					   form.submit();
					   $.layer({
                           type: 1,
                           closeBtn: [0,true],
                           title: "&nbsp;",
                           area: ['460px', '194px'],
                           border: [1, 1, '#cecfd0'],
                           page: {
                               html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
                           }
                       });
						$(".okBtn").click(function(){
							window.location.href = "/member/recharge/log.html";
						});
						$(".cancleBtn").click(function(){
							layer.closeAll();
							$(".valicode_img").each(function(){
								$(this).attr("src",'/validimg.html?t=' + Math.random());
			        		})
						});
				   }  	
			   }else{
				   	form.submit();
					//构造确认框DOM
				   	$.layer({
                        type: 1,
                        closeBtn: [0,true],
                        title: "&nbsp;",
                        area: ['460px', '194px'],
                        border: [1, 1, '#cecfd0'],
                        page: {
                            html: '<div class="tipsWrap"><dl><dt>充值完成前，请不要关闭本窗口;</dt><dd>充值完成后，请根据您的充值结果点击下面按钮。</dd></dl><div class="tipsBtnBar"><a href="javascript:;" class="okBtn">充值成功</a><a href="javascript:;" class="cancleBtn">充值失败</a></div></div>'
                        }
                    });
					$(".okBtn").click(function(){
						window.location.href = "/member/recharge/log.html";
					});
					$(".cancleBtn").click(function(){
						layer.closeAll();
						$(".valicode_img").each(function(){
							$(this).attr("src",'/validimg.html?t=' + Math.random());
		        		})
					});
			   }
			   });
		   }  
		});
    })
    
	
	function show_recharge(){
		$(".J_valicode_img").each(function(){
			$(this).attr("src",'/validimg.html?t=' + Math.random());
		})
		jQuery( "#modal_dialog" ).dialog({ autoOpen: false , modal: true ,height: 160,width:360 });
		jQuery( "#modal_dialog" ).dialog( "open" );
	}

});