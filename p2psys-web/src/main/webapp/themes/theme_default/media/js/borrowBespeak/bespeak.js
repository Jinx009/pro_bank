define(function(require,exports,module){
	require('jquery');
	
	//页面导航滚动
	$(window).on('scroll',function(){
		var dTitle_h = $('.bespeakNavBd').offset().top;
		var dTitle_h_1= $('#bespeakStep1').offset().top;
		var dTitle_h_2= $('#bespeakStep2').offset().top;
		var dTitle_h_3= $('#bespeakFormBox').offset().top;
	    var st = $(document).scrollTop();
	    if(st>dTitle_h){
	        $('#scrollTop').show();
	        if (!window.XMLHttpRequest) {
	            $('#scrollTop').css("top", st);
	        }
	    }else{
	        $('#scrollTop').hide();
	    }

		if(st>dTitle_h_3-70)
		{
			$("#bespeakNavBd").find("li").removeClass("active");
			//$("#bespeakNavBdTop li").eq(2).addClass("active");
	     	$("#bespeakNavBd li").eq(2).addClass("active");
		}
		else if(st>dTitle_h_2-70)
		{			
			$("#bespeakNavBd").find("li").removeClass("active");
			//$("#bespeakNavBdTop li").eq(1).addClass("active");
	     	$("#bespeakNavBd li").eq(1).addClass("active");
		}
		else
		{
			$("#bespeakNavBd").find("li").removeClass("active");
			//$("#bespeakNavBdTop li").eq(0).addClass("active");
			$("#bespeakNavBd li").eq(0).addClass("active");
		}
	    
	})

	
	//滚动至在线申请借款
	$(".bespeakNavBdBtn").click(function(){
		$('body,html').animate({scrollTop: $('#bespeakFormBox')[0].offsetTop-50}, 1000);
		return false; 
	});

	//表单验证及提交
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate.min',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			require.async(['/plugins/poshytip/tip-yellowsimple/tip-yellowsimple.css','/plugins/poshytip/jquery.poshytip.min'],function(){
				$("#bespeakForm").validate({
					rules:{
						guoguoname:{
				    		required: true
				    	},
				    	money:{
				    		required: true
				    	},
				    	borrowUse:{
				    		required: true,
				    		isborrowUse:true
				    	}
				    },
				    messages:{
				    	guoguoname:{
				    		required: "请输入果果号"
				    	},
				    	money:{
				    		required: "请输入借款金额"
				    	},
				    	borrowUse:{
				    		required: "请输入借款用途",
				    		isborrowUse:'借款用途字数为200个以内'
				    	}
				   },
				   ignore: ".ignore",
				   errorPlacement:function(error,element){
						element.poshytip('hide');
						element.attr("title",error.html());
						element.poshytip({
							className: 'tip-yellowsimple',
							showOn: 'none',
							alignTo: 'target',
							alignX: 'right',
							alignY:'center',
							fade:false,
							slide:false,
							offsetX: 5,
							offsetY: 5,
							showTimeout: 1000
						});
						element.poshytip('show');
	
					},
					success:function(element){
						element.poshytip('hide');
					},
				    submitHandler:function(form){
				    	require.async('jquery.form',function(){
					    	$(form).ajaxSubmit(function(data){
					    		datatype:'json';
					        	var resultMsg = data.msg||""; 
					        	//引入弹出窗口插件
				        		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
						        	if(data.result){ 
						        		$.layer({
						        			type: 1,
						        			closeBtn: false,
					        			    title: false,
					        			    area: ['400px', '180px'],
					        			    shade: [0.5, '#000'],
					        			    border: [10, 0.3, '#ccc'],
					        			    time:10000,
					        			    page: {
					        			        html: '<div class="bespeakPop"><h2>谢谢！</h2><p>我们已经收到您发出的申请，会在短时间内给您回复！</p><a href="/borrow/index.html#borrow-appointment" class="borrow-ok">确定</a></div>'
					        			    },
					        			    success:function(){
					        			    	$(".borrow-ok").click(function(){
					        			    		$("#bespeakForm")[0].reset();
					        			    		window.location.reload();
					        			    	})
					        			    }
					        			});	
						        	}else{
						        		layer.msg(data.msg, 1, -1);
						        	}
				        		});
					    	}); 
				    	})
				     }  
				});


			})
		})
	})
})

