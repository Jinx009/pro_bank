define(function(require,exports,module){
	require('jquery');
	
	$("#carStatus").val($('.carStatusVal').val());
    $("#houseStatus").val($('.houseStatusVal').val());
	
	
	//加载城市选择插件
	
	require.async('jquery-citySelect/jquery.cityselect',function(){
		var prov = $(".province").val();
		var city =$(".cityOpt").val();
		$("#city").citySelect({
			url:"../../../themes/theme_default/media/js/jquery-citySelect/city.json",
			prov:prov, //省份 
		    city:city, //城市 
		    required:true,
		    nodata:"none" //当子集无数据时，隐藏select 
		});
	})
	
	//表单验证
	require.async('/plugins/jquery-validation-1.13.0/jquery.validate.min',function(){
		require.async('/plugins/jquery-validation-1.13.0/additional-methods',function(){
			$("#detailForm").validate({
				rules:{
					birthday:{
						required:true
					}
				},
				messages:{
					birthday:{
						required:"请填写你的生日"
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
				submitHandler:function(form){
					require.async('jquery.form',function(){
						$(form).ajaxSubmit(function(data){
	    					require.async(['/plugins/layer-v1.8.4/skin/layer.css','/plugins/layer-v1.8.4/layer.min'],function(){
	    						if(data.result==true){
	    							//构造确认框DOM
	    							$.layer({
	    								type: 1,
	    							    closeBtn: false,
	    							    title: false,
	    							    area: ['460px', '240px'],
	    							    shade: [0.5, '#000'],
	    							    border: [10, 0.2, '#000'],
	    							    page: {
	    							        html: '<div class="fileconfirm"><div class="fileconfirm_title"><h1 class="float_left">提示</h1><span class="float_right cancleBtn">X</span></div><div class="fileconfirm_msg">'+data.msg+'</div><div class="fileconfirmbtn"><a href="javascript:;" class="okBtn">确认</a></div></div>'
	    							    }
	    							});	
	    							//确认操作
	    							$(".okBtn").click(function(){
	    								window.location.reload();				
	    							});
	    							//删除操作
	    							$(".cancleBtn").click(function(){
	    								layer.closeAll();
	    							});
		    					}else{
		    						//构造确认框DOM
	    							$.layer({
	    								type: 1,
	    							    closeBtn: false,
	    							    title: false,
	    							    area: ['460px', '240px'],
	    							    shade: [0.5, '#000'],
	    							    border: [10, 0.2, '#000'],
	    							    page: {
	    							        html: '<div class="fileconfirm"><div class="fileconfirm_title"><h1 class="float_left">提示</h1><span class="float_right cancleBtn">X</span></div><div class="fileconfirm_msg">'+data.msg+'</div><div class="fileconfirmbtn"><a href="javascript:;" class="okBtn">确认</a></div></div>'
	    							    }
	    							});	
	    							//确认操作
	    							$(".okBtn").click(function(){
	    								window.location.reload();				
	    							});
	    							//删除操作
	    							$(".cancleBtn").click(function(){
	    								layer.closeAll();
	    							});
		    					}
	    					})
		    			});
	    			});					
				}
			})
		})
	})
	
});