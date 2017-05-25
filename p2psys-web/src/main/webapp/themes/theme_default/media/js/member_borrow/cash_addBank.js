define(function(require,exports,module){
	require('jquery');
	$.ajax({
		url:"/member/cash/checkBank.html?random="+Math.random(),
		type:"get",
		dataType:"json",
		success:function(data){
			if(data.result==true){
				require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
					window.location="/member/cash/bank.html";
			});
			}
		}
	})
	//表单验证
	require.async("/plugins/jquery-validation-1.13.0/jquery.validate",function(){
		$("#addBankForm").validate({
			rules:{
				bank:{
					required:true
				},
				bankNo:{
					required:true,
					rangelength:[15,19]
				},
				comfirmAccount:{
					required:true,
					equalTo:"#infoyhzh"
				},
				code:{
					required:true
				}
			},
			messages:{
				bank:{
					required:"请输入开户银行名称",
				},
				bankNo:{
					required:"请输入银行账号",
					rangelength:"请输入{0}~{1}位银行账号"
				},
				comfirmAccount:{
					required:"确认银行账号",
					equalTo:"两次输入的银行卡不一致，请重新输入"
				},
				code:{
					required:"请输入验证码",
				}
			},
			errorElement:"em",
			errorPlacement:function(error,element){
				error.appendTo(element.parent().find("span"));
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
	    							    	window.location.href = "/member/cash/bank.html";
	    							    },
	    							    end: function(){
	    							    	window.location.href = "/member/cash/bank.html";
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
	    					})
    					}
    				});				
				 })
			}
		});
	})
	
	
	
	//地区级联
	require.async('area',function(){
		var Utils = {};
	Utils.addEvent = function(el, type, func) {
		if (document.addEventListener) {
			el.addEventListener(type, func, false);
		} else if (document.attachEvent) {
			el.attachEvent('on' + type, func);
		}
	};
	Utils.addEvent(window, 'load', function() {
		var region1 = document.getElementById("region1");
		var region2 = document.getElementById("region2");
		var region3 = document.getElementById("region3");
		for ( var i = 0; i < arrCity.length; i++) {
			region1.options[i] = new Option(arrCity[i].name, arrCity[i].name);
		}
		region2.options[0] = new Option("请选择", "请选择");
		region3.style.display = "none";
		Utils.addEvent(region1, 'change', function() {
			var region1_obj = arrCity[region1.selectedIndex];
			var region2_arr = region1_obj.sub;
			region2.options.length = 0;
			region3.options.length = 0;
			region3.style.display = "none";
			for ( var i = 0; i < region2_arr.length; i++) {
				region2.options[i] = new Option(region2_arr[i].name, region2_arr[i].name);
			}
		});
		Utils.addEvent(region2, 'change', function() {
			var region1_obj = arrCity[region1.selectedIndex];
			var region2_obj = region1_obj.sub[region2.selectedIndex];
			var region3_arr = region2_obj.sub;
			if (region2_obj.type == 0) {
				region3.options.length = 0;
				region3.style.display = "inline";
				for ( var i = 0; i < region3_arr.length; i++) {
					region3.options[i] = new Option(region3_arr[i].name, region3_arr[i].name);
				}
			} else {
				region3.style.display = "none";
			}
		});
	});
	});
});