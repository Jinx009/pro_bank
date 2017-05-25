define(function(require,exports,module){
	require('jquery');
	var isPayPwd = $("#isPayPwd").val();
	if(isPayPwd == "0") {
		require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
			//构造确认框DOM
			$.layer({
				type: 1,
				closeBtn: false,
                title: "&nbsp;",
			    area: ['450px', '190px'],
			    border: [1, 1, '#cecfd0'],
			    page: {
			        html: '<div class="tipsWrap w450"><div class="tipsTxt"><span>您尚未设置交易密码，请先设置交易密码。</span></div><div class="tipsBtnBar"><a href="/wx/account/modifyPaypwd.html" class="okBtn">马上去设置</a></div></div>'
			    }
			});
		});
	}
	/* 通过银联接口判断是哪家银行 */
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
				bankNo:{
					required:true,
					rangelength:[15,19]
				}
				// comfirmAccount:{
				// 	required:true,
				// 	equalTo:"#infoyhzh"
				// },
				// payPwd:{
				// 	required:true
				// }
			},
			messages:{
				accName:{
				   	required:"请填写您的真实姓名",
				   	realName:"真实姓名不正确"
				},
				accId:{
				   	required:"请填写您的身份证号码",
				},
				bankNo:{
					required:"请输入银行账号",
					rangelength:"请输入{0}~{1}位银行账号"
				}
				// comfirmAccount:{
				// 	required:"确认银行账号",
				// 	equalTo:"两次输入不一致，请重新输入"
				// },
				// payPwd:{
				// 	required:"请输入交易密码"
				// }
			},
			errorPlacement:function(error, element){
				  	element.parents(".input-row").addClass("input-error").find(".input-error-tip").html(error);	
			},
			success:function(element){
					element.parents(".input-row").removeClass("input-error");
			},
			submitHandler:function(form,event,validator){
				document.addBankForm.submit();
//				 require.async('jquery.form',function(){
//					 $(form).ajaxSubmit({
//    					dataType:'json',
//    					success:function(data){
//    						require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
//	    						if(data.result){
//	    							$.layer({
//	    							    type: 1,
//	    							    closeBtn: [0,true],
//	    				                title: "&nbsp;",
//	    							    area: ['384px', '186px'],
//	    							    border: [1, 1, '#cecfd0'],
//	    							    time:3,
//	    							    page: {
//	    							        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont okIco">&#xe63d;</i><span>添加银行卡成功</span></div><div class="tipsMsg">3秒后窗口自动关闭</div></div>'
//	    							    },
//	    							    close: function(index){
//	    							    	window.location.href = "/wx/account/setting.html";
//	    							    },
//	    							    end: function(){
//	    							    	window.location.href = "/wx/account/setting.html";
//	    							    },
//	    							    success: function(layero){
//	    							    	var time =3; 
//	    							    	function closeTime () {
//	    										time--;
//	    										$(".tipsMsg").html(time+"秒后窗口自动关闭");
//		    								}
//	    							    	setInterval(function(){
//    											closeTime();
//    										}, 1000);
//	    							    }
//	    							});
//		    					}else{
//		    						$.layer({
//									    type: 1,
//									    closeBtn: [0,true],
//						                		    title: "&nbsp;",
//									    area: ['384px', '186px'],
//									    border: [1, 1, '#cecfd0'],
//									    page: {
//									        html: '<div class="tipsWrap w384"><div class="tipsTxt"><i class="iconfont errIco">&#xe63e;</i><span>'+data.msg+'</span></div><div class="tipsBtnBar"><a href="javascript:void(0);" class="okBtn failBtn">确定</a></div></div>'
//									    },
//									    close: function(index){
//									    	layer.closeAll();
//									    }
//									});
//		    						$(".failBtn").click(function(){
//		    							layer.closeAll();
//									});
//		    					}
//	    					})
//    					}
//    				});				
//				 })
			}
		});
	})
	
	
	
	//地区级联
	// require.async('area',function(){
	// 	var Utils = {};
	// Utils.addEvent = function(el, type, func) {
	// 	if (document.addEventListener) {
	// 		el.addEventListener(type, func, false);
	// 	} else if (document.attachEvent) {
	// 		el.attachEvent('on' + type, func);
	// 	}
	// };
	// Utils.addEvent(window, 'load', function() {
	// 	var region1 = document.getElementById("region1");
	// 	var region2 = document.getElementById("region2");
	// 	var region3 = document.getElementById("region3");
	// 	for ( var i = 0; i < arrCity.length; i++) {
	// 		region1.options[i] = new Option(arrCity[i].name, arrCity[i].name);
	// 	}
	// 	region2.options[0] = new Option("请选择", "请选择");
	// 	region3.style.display = "none";
	// 	Utils.addEvent(region1, 'change', function() {
	// 		var region1_obj = arrCity[region1.selectedIndex];
	// 		var region2_arr = region1_obj.sub;
	// 		region2.options.length = 0;
	// 		region3.options.length = 0;
	// 		region3.style.display = "none";
	// 		for ( var i = 0; i < region2_arr.length; i++) {
	// 			region2.options[i] = new Option(region2_arr[i].name, region2_arr[i].name);
	// 		}
	// 	});
	// 	Utils.addEvent(region2, 'change', function() {
	// 		var region1_obj = arrCity[region1.selectedIndex];
	// 		var region2_obj = region1_obj.sub[region2.selectedIndex];
	// 		var region3_arr = region2_obj.sub;
	// 		if (region2_obj.type == 0) {
	// 			region3.options.length = 0;
	// 			region3.style.display = "inline";
	// 			for ( var i = 0; i < region3_arr.length; i++) {
	// 				region3.options[i] = new Option(region3_arr[i].name, region3_arr[i].name);
	// 			}
	// 		} else {
	// 			region3.style.display = "none";
	// 		}
	// 	});
	// });
	// });
});