define(function(require,exports,module){
	require('jquery');
	
	//表单验证
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		$("#addBankForm").validate({
			rules:{
				
				accName:{
			   		required: true,
			   		realName:true
				},
				accId:{
			   		required: true,
			   		isIdCardNo:true,
				},
				code:{
					required:true
				}
			},
			messages:{
				
				money:{
				   	required:"请填写您收到的随机金额"
					},
			},
			errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
			},
			success:function(element){
					element.parents(".input-row").removeClass("input-error");
			},
			submitHandler:function(form,event,validator){
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
	    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>添加银行卡成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
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
		    					}else{
		    						$.layer({
									    type: 1,
									    closeBtn: [0,true],
						                		    title: "&nbsp;",
									    area: ['384px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn cancleBtn">绑定新卡</a><a href="javascript:;" class="okBtn failBtn">重新验证</a></div></div>'
			                            },
									    close: function(index){
									    	layer.closeAll();
									    }
									});
		    						$(".failBtn").click(function(){
		    							layer.closeAll();
									});
		    						$(".cancleBtn").click(function(){
		    							window.location.href = "/wx/account/addBank.html";
		    						});
		    					}
	    					})
    					}
    				});				
				 })
			}
		});
	})
	
});