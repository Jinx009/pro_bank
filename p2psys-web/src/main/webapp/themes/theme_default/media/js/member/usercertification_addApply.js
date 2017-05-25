define(function(require,exports,module){
	require('jquery');
	
	//申请额度验证
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		$("#addAmountApply").validate({
			rules:{
				amount:{
					required:true,
					min:1,
					max:3000000
				},
				content:{
					required:true
				}
			},
			messages:{
				amount:{
					required:"请填写申请金额",
					min:"申请金额最小为1元",
					max:"申请金额不能超过300万元"
				},
				content:{
					required:"请写上详细信息"
				}
			},
			errorPlacement:function(error, element){
			  	element.parents(".ipt").find("em").html(error);	
			},
			success:function(element){
				element.parents(".ipt").find("em").html("");
			},
		    success:function(label){
		    },
		    submitHandler:function(form,event,validator){
				 require.async('jquery.form',function(){
					 $(form).ajaxSubmit({
	   					dataType:'json',
	   					success:function(data){
	   						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result){
	    							var closeLayer = $.layer({
										type: 1,
									    closeBtn: [0,true],
						                title: "&nbsp;",
									    area: ['384px', '186px'],
									    border: [1, 1, '#cecfd0'],
									    page: {
									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>申请提交成功，请耐心等待审核！</span></div><div class="tipsBtnBar"><a href="javascript:;" class="okBtn failBtn">确定</a></div></div>'
									    },
									    close: function(index){
									    	window.location.href="/member_borrow/borrow/creditApply.html";
									    }
									});
									$(".failBtn").click(function(){
										window.location.href="/member_borrow/borrow/creditApply.html";
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
									    	$("#addAmountApply")[0].reset();
									    }
									});
									$(".failBtn").click(function(){
										layer.closeAll();
										$("#addAmountApply")[0].reset();
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